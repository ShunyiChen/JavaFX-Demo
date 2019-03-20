/*
 * Copyright (c) 2008, 2014, Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 *
 * This file is available and licensed under the following license:
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  - Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the distribution.
 *  - Neither the name of Oracle Corporation nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.dockingsoftware.autorepairsystem;

import com.dockingsoftware.autorepairsystem.ui.NavigationBar;
import com.dockingsoftware.autorepairsystem.config.AppInitializer;
import com.dockingsoftware.autorepairsystem.persistence.entity.Parameter;
import com.dockingsoftware.autorepairsystem.persistence.entity.User;
import com.dockingsoftware.autorepairsystem.ui.InputLicenseDialog;
import com.dockingsoftware.autorepairsystem.ui.LoginDialog;
import com.dockingsoftware.autorepairsystem.ui.TabContentUI;
import com.dockingsoftware.autorepairsystem.ui.TitleUI;
import com.dockingsoftware.autorepairsystem.ui.controller.CommonController;
import com.dockingsoftware.autorepairsystem.util.DAOUtils;
import static com.dockingsoftware.autorepairsystem.util.PrintUtils.p;
import com.dockingsoftware.autorepairsystem.util.ValidateUtils;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * A sample with a control that creates a decorated stage that is centered on
 * your desktop.
 *
 */
public class MainApp extends Application implements Runnable {
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        try {
            server = new ServerSocket(PORT);
            new Thread(this).start();
            firstMain();
        } catch (IOException ex) {
            relaunch(new String[]{"只允许运行一个实例好吧。。"});
        }
        
//        File bkg = new File(Constants.IMAGE_BASE, "bkg.jpg");
//        Image imgBkg = new Image("file:"+bkg.toString());
//        background = new ImageView(imgBkg);
//        borderPane.getChildren().add(background);
    }

    /**
     * Java main for when running without JavaFX launcher
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    public void changeUI(VBox vbox) {
        borderPane.getChildren().remove(borderPane.getCenter());
        borderPane.setCenter(vbox);
    }
    
    public static MainApp getInstance() {
        return singleton;
    }
    
    @Override
    public void run() {
        while (true) {
            try {
                Socket sock = server.accept();
                InputStreamReader in = new InputStreamReader(sock.getInputStream());
                StringBuilder sb = new StringBuilder();
                char[] buf = new char[256];
                while (true) {
                    int n = in.read(buf);
                    if (n < 0) {
                        break;
                    }
                    sb.append(buf, 0, n);
                }
                String[] results = sb.toString().split("\\n");
                otherMain(results);
                
            } catch (IOException ex) {
            }
            
        }
    }
    
    /**
     * Re-launch program.
     * 
     * @param args 
     */
    protected void relaunch(String[] args) {
        try {
            Socket sock = new Socket("localhost", PORT);
            try (OutputStreamWriter out = new OutputStreamWriter(sock.getOutputStream())) {
                for (String arg : args) {
                    out.write(arg + "\n");
                    System.out.println("wrote: " + arg);
                }
                out.flush();
            }
        } catch (IOException ex) {
        } finally {
            createAlert();
        }
    }
    
    /**
     * Create alert.
     */
    protected void createAlert() {
        AlertType type = AlertType.INFORMATION;
        Alert alert = new Alert(type, "");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.getDialogPane().setContentText("一次只能运行一个程序。");
        alert.getDialogPane().setHeaderText(null);
        alert.showAndWait()
            .filter(response -> response == ButtonType.OK)
            .ifPresent(response -> System.out.println("The alert was approved"));
        System.exit(0);
    }
    
    /**
     * Other main.
     * 
     * @param args 
     */
    protected void otherMain(final String[] args) {
        if (args.length >= 1) {
            System.out.println("另一个实例正在启动");
        }
    }
    
    /**
     * First main.
     * 
     */
    protected void firstMain() {
        
        singleton = this;
        
        if (!ValidateUtils.isSerialNumberExpired()) {
            login();
        } else {
            askLicense();
        }
        
        titleUI = new TitleUI();
        navigationBar = new NavigationBar();
        tabContentUI = new TabContentUI();
        
        borderPane.setTop(titleUI);
        borderPane.setLeft(navigationBar);
        borderPane.setCenter(tabContentUI);
        
        borderPane.setBackground(Background.EMPTY);
        
        primaryStage.setOnCloseRequest(new EventHandler() {
            @Override
            public void handle(Event event) {
                Map<String, String> allParameters = (Map<String, String>) controller.listAllParameters.call("");
                String p1 = allParameters.get("RETAIN_FILES");
                String p2 = allParameters.get("SAVING_BEFORE_EXITING");
                String p3 = allParameters.get("SAVE_PATH");
                if (Boolean.parseBoolean(p2)) {
                    titleUI.backup(Boolean.parseBoolean(p1), p3);
                } else {
                    System.exit(0);
                }
            }
        });
        
        // 在这可以设置全局样式
//        scene.getStylesheets().add("styles/styled-unique-expanded-tree.css");
        primaryStage.setScene(scene);
        primaryStage.setTitle(Constants.SYSTEM_NAME);
        primaryStage.show();
//        stage.setFullScreen(true);
    }
    
    private void login() {
        Service<Void> service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {           
                    @Override
                    protected Void call() throws Exception {
                        //Background work                       
                        final CountDownLatch latch = new CountDownLatch(1);
                        Platform.runLater(new Runnable() {                          
                            @Override
                            public void run() {
                                try{
                                    LoginDialog dia = new LoginDialog();
                                    dia.showAndWait().ifPresent(response -> {
                                        if (response == ButtonType.OK) {
                                            dia.login(user -> loginCheck(user));
                                        } else {
                                            System.exit(0);
                                        }
                                    });
                                }finally{
                                    latch.countDown();
                                }
                            }
                        });
                        latch.await();                      
                        //Keep with the background work
                        return null;
                    }
                };
            }
        };
        service.start();
    }
    
    private void askLicense() {
        Service<Void> service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {           
                    @Override
                    protected Void call() throws Exception {
                        //Background work                       
                        final CountDownLatch latch = new CountDownLatch(1);
                        Platform.runLater(new Runnable() {                          
                            @Override
                            public void run() {
                                try{
                                    InputLicenseDialog dia = new InputLicenseDialog();
                                    dia.showAndWait().ifPresent(response -> {
                                        if (response == ButtonType.OK) {
                                            
                                            if (ValidateUtils.checkInputSerialNumber(dia.returnSerialNumber())) {
                                                Parameter param = new Parameter();
                                                param.setId("LICENSE");
                                                param.setValue(dia.returnSerialNumber());
                                                DAOUtils.getInstance().getParameterDAO().saveOrUpdate(param);
                                                
                                                // 输入序列号后登录
                                                login();
                                                
                                            } else {
                                                askLicense();
                                            }
                                            
                                        } else {
                                            System.exit(0);
                                        }
                                    });
                                }finally{
                                    latch.countDown();
                                }
                            }
                        });
                        latch.await();                      
                        //Keep with the background work
                        return null;
                    }
                };
            }
        };
        service.start();
    }
    
    private void loginCheck(User user) {
        if (user.getId() == null) {
            login();
        }
        else {
            loggedUser = user;
            navigationBar.getBtnDown().setText(loggedUser.getDisplayName());
            p("Welcome login the system at time "+ new Date());
        }
    }
    
    public User getLoggedUser() {
        return loggedUser;
    }
    
    public Stage getPrimaryStage() {
        return primaryStage;
    }
    
    public TabContentUI getTabContentUI() {
        return tabContentUI;
    }

    public NavigationBar getNavigationBar() {
        return navigationBar;
    }
    
    public TitleUI getTitleUI() {
        return titleUI;
    }
    
    static {
        new AppInitializer().onStartup();
    }
    
    private static MainApp singleton;
    private static final int PORT = 38629;
    private ServerSocket server;
    private ImageView background;
    private Stage primaryStage;
    private BorderPane borderPane = new BorderPane();
    private Scene scene = new Scene(borderPane, 1524, 768);
    private TabContentUI tabContentUI;
    private NavigationBar navigationBar;
    private TitleUI titleUI;
    
    private User loggedUser;
    private CommonController controller = new CommonController();
}
