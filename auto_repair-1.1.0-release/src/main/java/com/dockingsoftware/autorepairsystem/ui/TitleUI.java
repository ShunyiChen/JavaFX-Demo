/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.ui;

import com.dockingsoftware.autorepairsystem.MainApp;
import com.dockingsoftware.autorepairsystem.ui.controller.CommonController;
import com.dockingsoftware.autorepairsystem.util.ImageUtils;
import com.dockingsoftware.autorepairsystem.util.ZipHelper;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;

public class TitleUI extends HBox {

    public TitleUI() {
        setPrefHeight(40);
        setStyle("-fx-background-color: rgba(68, 68, 68);");
 
        btnPassword = createSystemButton("更改密码", ImageUtils.createImageView("user_16px_505076_easyicon.net.png"));
        btnReminder = createSystemButton("提醒", ImageUtils.createImageView("email_16px_505014_easyicon.net.png"));
        btnSettings = createSystemButton("设置", ImageUtils.createImageView("settings_16px_505061_easyicon.net.png"));
        btnLogout = createSystemButton("退出", ImageUtils.createImageView("logout_16px_505037_easyicon.net.png"));
        
        btnPassword.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                ChangePasswordDialog dia = new ChangePasswordDialog();
                dia.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> dia.change());
            }
        });
        
        btnReminder.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                ReminderDialog dia = new ReminderDialog();
                dia.show();
            }
        });
        
        btnSettings.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                SettingsDialog dia = new SettingsDialog();
                dia.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> dia.saveAll());
            }
        });
        
        btnLogout.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                Map<String, String> allParameters = (Map<String, String>) controller.listAllParameters.call("");
                String p1 = allParameters.get("RETAIN_FILES");
                String p2 = allParameters.get("SAVING_BEFORE_EXITING");
                String p3 = allParameters.get("SAVE_PATH");
                if (Boolean.parseBoolean(p2)) {
                    backup(Boolean.parseBoolean(p1), p3);
                } else {
                    System.exit(0);
                }
            }
        });
        
        setAlignment(Pos.BOTTOM_RIGHT);
        getChildren().addAll(btnPassword, btnReminder, btnSettings, btnLogout);
    }
    
    private Button createSystemButton(String text, ImageView iv) {
        Button btn = new Button(text);
        btn.setGraphic(iv);
        btn.setStyle("-fx-font-size:11pt;");
        btn.setPrefHeight(30);
        btn.setMinWidth(80);
        btn.setStyle("-fx-base: rgb(68, 68, 68);");
        btn.setTextFill(Color.rgb(250, 250, 250));
        return btn;
    }
    
    /**
     * 执行备份
     * 
     * @param retain
     * @param path 
     */
    public void backup(boolean retain, String path) {
        
        File dest = new File(path);
        if (dest.exists()) {
            
            showProgressBar();
            
            Callback callback = new Callback() {
                @Override
                public Object call(Object param) {
                    long[] p = (long[]) param;
                    readBytes += p[0];
                    p6.setProgress((double)readBytes / p[1]);
                    Platform.runLater(new Runnable() {
                        @Override public void run() {
                            if (p6.getProgress() == 1.0) {
                                progressInfo.setText("备份已完成。");
                            } else {
                                progressInfo.setText("正在备份文件....");
                            }
                        }
                    });
                    return "";
                }
            };
            SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmmss");
            ZipHelper helper = new ZipHelper(callback);
            String newName = f.format(new Date());
            Task<Void> t = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    helper.zipDir("data", dest.getPath()+"/"+newName);
                    System.exit(0);
                    return null;
                }
            };
            ExecutorService executor = Executors.newFixedThreadPool(1, new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(r);
                    t.setDaemon(true);
                    return t;
                }
            });
            executor.execute(t);
            
            if (!retain) {
                File[] backups = dest.listFiles();
                for (File backupFile : backups) {
                    if (!backupFile.getName().equals(newName)) {
                        
                        try {
                            Date d = f.parse(backupFile.getName());
                            if (d != null) {
                                backupFile.delete();
                            }
                        } catch (ParseException ex) {
                            org.apache.logging.log4j.Logger logger = LogManager.getLogger(TitleUI.class.getName());
                            logger.info("备份文件"+backupFile.getName()+"无法删除");
                        }
                    }
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.initOwner(MainApp.getInstance().getPrimaryStage());
            alert.getDialogPane().setContentText("备份目录不存在，是否重设？");
            alert.getDialogPane().setHeaderText("提示");
            alert.setTitle("消息");
            Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setText("确定");
            Button cancelButton = (Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL);
            cancelButton.setText("取消");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    SettingsDialog dia = new SettingsDialog();
                    dia.setSelectedIndex(6);
                    dia.showAndWait().filter(res -> res == ButtonType.OK).ifPresent(res -> dia.saveAll());
                } else {
                    System.exit(0);
                }
            });
        }
    }
    
    private void showProgressBar() {
        readBytes = 0L;
        p6.setProgress(0);
        p6.setPrefWidth(600);
        VBox vbox = new VBox();
        vbox.getChildren().addAll(progressInfo, p6);
        progressDialog.initOwner(MainApp.getInstance().getPrimaryStage());
        progressDialog.getDialogPane().setContent(vbox);
        progressDialog.setTitle("备份进度条");
        progressDialog.show();
    }
    
    private Button btnPassword;
    private Button btnReminder;
    private Button btnSettings;
    private Button btnLogout;
    
    private ProgressBar p6 = new ProgressBar();
    private Label progressInfo = new Label("");
    private long readBytes = 0L;
    private Dialog progressDialog = new Dialog();
    private CommonController controller = new CommonController();
}
