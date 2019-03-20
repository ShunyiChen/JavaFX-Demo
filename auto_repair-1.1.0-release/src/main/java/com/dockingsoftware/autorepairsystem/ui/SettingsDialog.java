/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.ui;

import com.dockingsoftware.autorepairsystem.MainApp;
import com.dockingsoftware.autorepairsystem.component.MessageBox;
import com.dockingsoftware.autorepairsystem.component.NumericTextField;
import com.dockingsoftware.autorepairsystem.persistence.entity.BusinessType;
import com.dockingsoftware.autorepairsystem.persistence.entity.Parameter;
import com.dockingsoftware.autorepairsystem.persistence.entity.Payment;
import com.dockingsoftware.autorepairsystem.persistence.entity.ProjectCategory;
import com.dockingsoftware.autorepairsystem.persistence.entity.VehicleType;
import com.dockingsoftware.autorepairsystem.ui.controller.SettingsDialogController;
import com.dockingsoftware.autorepairsystem.util.ValidateUtils;
import com.dockingsoftware.autorepairsystem.util.ZipHelper;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;

/**
 * 设置
 * 
 * @author Shunyi Chen
 */
public class SettingsDialog extends Dialog {
    
    public SettingsDialog() {
        initComponents();
    }
    
    private void initComponents() {
        initOwner(MainApp.getInstance().getPrimaryStage());
        
        setTitle("设置");
        setHeaderText("系统设置。");
 
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
        okButton.setText("确定");
//        okButton.setDisable(true);
        Button cancelButton = (Button) getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelButton.setText("取消");
         
        // Do some validation (using the Java 8 lambda syntax).
//        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
//            okButton.setDisable(newValue.trim().isEmpty());
//        });

        VBox vbox = new VBox();
        vbox.getChildren().addAll(createTabPane());
        getDialogPane().setContent(vbox);
    }
    
    private TabPane createTabPane() {
        allParameters = (Map<String, String>) controller.listAllParameters.call("");
        
        tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.getStyleClass().add(TabPane.STYLE_CLASS_FLOATING);
        tabPane.getTabs().addAll(getInsuranceRatesTab(), getReminderTab(), getVehicleTypeTab(), getBusinessTypeTab(), getProjectCategoryTab(), getPaymentTab(), getBackupTab(), getLicenseTab());
        tabPane.getSelectionModel().select(0);
        
        return tabPane;
    }
    
    private Tab getVehicleTypeTab() {
        Tab insuranceTab = new Tab("机动车种类");
        vehicleTypeList.setItems(vehicleTypeItems);
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(20, 20, 10, 10));
        HBox h1 = new HBox();
        Button btnAdd = new Button("添加");
        Button btnRemove = new Button("删除");
        h1.setAlignment(Pos.CENTER);
        h1.getChildren().addAll(new Label("种类名称:"), vehicleTypeField, btnAdd, btnRemove);
        HBox.setHgrow(vehicleTypeField, Priority.ALWAYS);
        vehicleTypeField.setText("");
        vehicleTypeField.setPromptText("机动车种类名称");
        
        listVehicleType = (List<VehicleType>) controller.listVehicleType.call("");
        for (VehicleType vt : listVehicleType) {
            vehicleTypeItems.add(vt);
        }
        vehicleTypeList.setPrefHeight(200);
        vbox.getChildren().addAll(h1, vehicleTypeList);
        
        insuranceTab.setContent(vbox);
        
        btnAdd.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                if (!vehicleTypeField.getText().isEmpty()) {
                    vehicleTypeItems.add(VehicleType.create(vehicleTypeField.getText()));
                }
            }
        });
        
        btnRemove.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                VehicleType vt = vehicleTypeList.getSelectionModel().getSelectedItem();
                vehicleTypeItems.remove(vt);
            }
        });
        
        return insuranceTab;
    }
    
    public void saveVehicleTypes() {
        // 先清空后添加
        Iterator<VehicleType> iter = listVehicleType.iterator();
        while (iter.hasNext()) {
            VehicleType vt = iter.next();
            controller.deleteVehicleType.call(vt);
            iter.remove();
        }
        for (VehicleType vt : vehicleTypeItems) {
            vt.setId(null);
            controller.saveOrUpdateVehicleType.call(vt);
        }
    }
    
    private Tab getPaymentTab() {
        Tab insuranceTab = new Tab("支付方式");
        
        paymentList.setItems(paymentItems);
        
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(20, 20, 10, 10));
        HBox h1 = new HBox();
        Button btnAdd = new Button("添加");
        Button btnRemove = new Button("删除");
        h1.setAlignment(Pos.CENTER);
        h1.getChildren().addAll(new Label("支付方式:"), paymentField, btnAdd, btnRemove);
        HBox.setHgrow(paymentField, Priority.ALWAYS);
        paymentField.setText("");
        paymentField.setPromptText("支付方式");
        
        listPayment = (List<Payment>) controller.listPayment.call("");
        for (Payment bt : listPayment) {
            paymentItems.add(bt);
        }
        paymentList.setPrefHeight(200);
        vbox.getChildren().addAll(h1, paymentList);
        
        insuranceTab.setContent(vbox);
        
        btnAdd.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                if (!paymentField.getText().isEmpty()) {
                    paymentItems.add(Payment.create(paymentField.getText()));
                }
            }
        });
        btnRemove.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                Payment bt = paymentList.getSelectionModel().getSelectedItem();
                paymentItems.remove(bt);
            }
        });
        
        return insuranceTab;
    }
    
    public void savePayments() {
        // 先清空后添加
        Iterator<Payment> iter = listPayment.iterator();
        while (iter.hasNext()) {
            Payment bt = iter.next();
            controller.deletePayment.call(bt);
            iter.remove();
        }
        for (Payment bt : paymentItems) {
            bt.setId(null);
            controller.saveOrUpdatePayment.call(bt);
        }
    }
    
    private Tab getBackupTab() {
        Tab backupTab = new Tab("数据备份");
        HBox hbox = new HBox();
        hbox.setSpacing(5);
        hbox.setAlignment(Pos.CENTER_LEFT);
        Label pathLabel = new Label("备份到路径：");
        pathField.setPrefWidth(400);
        
        String p1 = allParameters.get("RETAIN_FILES");
        String p2 = allParameters.get("SAVING_BEFORE_EXITING");
        String p3 = allParameters.get("SAVE_PATH");
        
        retain.setSelected(Boolean.parseBoolean(p1));
        exit.setSelected(Boolean.parseBoolean(p2));
        pathField.setText(p3);
        
        Button btnChoose = new Button("选择");
        Button btnBackup = new Button("现在备份");
        btnChoose.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                DirectoryChooser chooser = new DirectoryChooser();
                File chosenDir = chooser.showDialog(MainApp.getInstance().getPrimaryStage());
                if (chosenDir != null) {
                    pathField.setText(chosenDir.getPath());
                }
            }
        });
        btnBackup.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                backup();
            }
        });
        hbox.getChildren().addAll(pathLabel, pathField, btnChoose);
        
        p6.setPrefWidth(520);
        p6.setVisible(false);
        progressInfo.setVisible(false);
        
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.TOP_LEFT);
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(20, 20, 10, 10));
        vbox.getChildren().addAll(retain, exit, hbox, btnBackup, progressInfo, p6);
        
        backupTab.setContent(vbox);
        
        return backupTab;
    }
    
    private void saveBackup() {
        Parameter p1 = new Parameter();
        p1.setId("RETAIN_FILES");
        p1.setValue(retain.isSelected()+"");
        controller.saveOrUpdateParameters.call(p1);
        
        Parameter p2 = new Parameter();
        p2.setId("SAVING_BEFORE_EXITING");
        p2.setValue(exit.isSelected()+"");
        controller.saveOrUpdateParameters.call(p2);
        
        Parameter p3 = new Parameter();
        p3.setId("SAVE_PATH");
        p3.setValue(pathField.getText());
        controller.saveOrUpdateParameters.call(p3);
    }
    
    private Tab getLicenseTab() {
        String p1 = allParameters.get("LICENSE");
        snField.setText(p1);
        
        Tab licenseTab = new Tab("许可证");
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.setSpacing(5);
        Label snLabel = new Label("序列号：");
        snField.setPrefWidth(400);
        Button verifyButton = new Button("验证");
        verifyButton.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                if (ValidateUtils.checkInputSerialNumber(snField.getText())) {
                    MessageBox.showMessage("序列号正确。");
                } else {
                    MessageBox.showMessage("序列号错误。", Alert.AlertType.ERROR);
                }
            }
        });
        hbox.getChildren().addAll(snLabel, snField, verifyButton);
        
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.TOP_LEFT);
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(20, 20, 10, 10));
        vbox.getChildren().addAll(hbox);
        
        licenseTab.setContent(vbox);
        
        return licenseTab;
    }
    
    private void saveLicense() {
        Parameter p1 = new Parameter();
        p1.setId("LICENSE");
        p1.setValue(snField.getText());
        controller.saveOrUpdateParameters.call(p1);
    }
    
    private Tab getBusinessTypeTab() {
        Tab insuranceTab = new Tab("业务类型");
        
        businessTypeList.setItems(businessTypeItems);
        
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(20, 20, 10, 10));
        HBox h1 = new HBox();
        Button btnAdd = new Button("添加");
        Button btnRemove = new Button("删除");
        h1.setAlignment(Pos.CENTER);
        h1.getChildren().addAll(new Label("业务类型名称:"), businessTypeField, btnAdd, btnRemove);
        HBox.setHgrow(businessTypeField, Priority.ALWAYS);
        businessTypeField.setText("");
        businessTypeField.setPromptText("业务类型名称");
        
        listBusinessType = (List<BusinessType>) controller.listBusinessType.call("");
        for (BusinessType bt : listBusinessType) {
            businessTypeItems.add(bt);
        }
        businessTypeList.setPrefHeight(200);
        vbox.getChildren().addAll(h1, businessTypeList);
        
        insuranceTab.setContent(vbox);
        
        btnAdd.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                if (!businessTypeField.getText().isEmpty()) {
                    businessTypeItems.add(BusinessType.create(businessTypeField.getText()));
                }
            }
        });
        btnRemove.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                BusinessType bt = businessTypeList.getSelectionModel().getSelectedItem();
                businessTypeItems.remove(bt);
            }
        });
        
        return insuranceTab;
    }
    
    public void saveBusinessTypes() {
        // 先清空后添加
        Iterator<BusinessType> iter = listBusinessType.iterator();
        while (iter.hasNext()) {
            BusinessType bt = iter.next();
            controller.deleteBusinessType.call(bt);
            iter.remove();
        }
        for (BusinessType bt : businessTypeItems) {
            bt.setId(null);
            controller.saveOrUpdateBusinessType.call(bt);
        }
    }
    
    private Tab getProjectCategoryTab() {
        Tab insuranceTab = new Tab("项目分类");
        
        projectCategoryList.setItems(projectCategoryItems);
        
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(20, 20, 10, 10));
        HBox h1 = new HBox();
        Button btnAdd = new Button("添加");
        Button btnRemove = new Button("删除");
        h1.setAlignment(Pos.CENTER);
        h1.getChildren().addAll(new Label("项目分类名称:"), projectCategoryField, btnAdd, btnRemove);
        HBox.setHgrow(projectCategoryField, Priority.ALWAYS);
        projectCategoryField.setText("");
        projectCategoryField.setPromptText("项目分类名称");
        
        listProjectCategory = (List<ProjectCategory>) controller.listProjectCategory.call("");
        for (ProjectCategory bt : listProjectCategory) {
            projectCategoryItems.add(bt);
        }
        projectCategoryList.setPrefHeight(200);
        vbox.getChildren().addAll(h1, projectCategoryList);
        
        insuranceTab.setContent(vbox);
        
        btnAdd.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                if (!projectCategoryField.getText().isEmpty()) {
                    projectCategoryItems.add(ProjectCategory.create(projectCategoryField.getText()));
                }
            }
        });
        btnRemove.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                ProjectCategory bt = projectCategoryList.getSelectionModel().getSelectedItem();
                projectCategoryItems.remove(bt);
            }
        });
        
        return insuranceTab;
    }
    
    public void saveProjectCategory() {
        // 先清空后添加
        Iterator<ProjectCategory> iter = listProjectCategory.iterator();
        while (iter.hasNext()) {
            ProjectCategory bt = iter.next();
            controller.deleteProjectCategory.call(bt);
            iter.remove();
        }
        for (ProjectCategory bt : projectCategoryItems) {
            bt.setId(null);
            controller.saveOrUpdateProjectCategory.call(bt);
        }
    }
    
    private Tab getInsuranceRatesTab() {
        Tab insuranceTab = new Tab("保险费率");
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 10, 10));
        grid.add(new Label("商业险费率:"), 0, 0);
        commercialInsuranceRate.setText(allParameters.get("COMMERCIALINSURANCERATE"));
        commercialInsuranceRate.setPromptText("商业险费率");
        grid.add(commercialInsuranceRate, 1, 0);
        
        grid.add(new Label("交强险费率:"), 0, 1);
        compulsoryInsuranceRate.setText(allParameters.get("COMPULSORYINSURANCERATE"));
        compulsoryInsuranceRate.setPromptText("交强险费率");
        grid.add(compulsoryInsuranceRate, 1, 1);
        
        insuranceTab.setContent(grid);
        
        return insuranceTab;
    }
    
    public void saveInsuranceRates() {
        Parameter p1 = new Parameter();
        p1.setId("COMMERCIALINSURANCERATE");
        p1.setValue(commercialInsuranceRate.getText());
        controller.saveOrUpdateParameters.call(p1);
        
        Parameter p2 = new Parameter();
        p2.setId("COMPULSORYINSURANCERATE");
        p2.setValue(compulsoryInsuranceRate.getText());
        controller.saveOrUpdateParameters.call(p2);
    }
    
    private Tab getReminderTab() {
        Tab insuranceTab = new Tab("提醒剩余天数");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 10, 10));
        
        grid.add(new Label("保养剩余天数:"), 0, 0);
        BYTS.setText(allParameters.get("BYTS"));
        BYTS.setPromptText("保养剩余天数");
        grid.add(BYTS, 1, 0);
        
        grid.add(new Label("保险剩余天数:"), 0, 1);
        BXTS.setText(allParameters.get("BXTS"));
        BXTS.setPromptText("保险剩余天数");
        grid.add(BXTS, 1, 1);
        
        grid.add(new Label("年审剩余天数:"), 0, 2);
        NSTS.setText(allParameters.get("NSTS"));
        NSTS.setPromptText("年审剩余天数");
        grid.add(NSTS, 1, 2);
        
        grid.add(new Label("回访剩余天数:"), 0, 3);
        HFTS.setText(allParameters.get("HFTS"));
        HFTS.setPromptText("回访剩余天数");
        grid.add(HFTS, 1, 3);
        
        insuranceTab.setContent(grid);
        
        return insuranceTab;
    }
    
    public void saveRemainderParameters() {
        Parameter p1 = new Parameter();
        p1.setId("BYTS");
        p1.setValue(BYTS.getText());
        controller.saveOrUpdateParameters.call(p1);
        
        Parameter p2 = new Parameter();
        p2.setId("BXTS");
        p2.setValue(BXTS.getText());
        controller.saveOrUpdateParameters.call(p2);
        
        Parameter p3 = new Parameter();
        p3.setId("NSTS");
        p3.setValue(NSTS.getText());
        controller.saveOrUpdateParameters.call(p3);
        
        Parameter p4 = new Parameter();
        p4.setId("HFTS");
        p4.setValue(HFTS.getText());
        controller.saveOrUpdateParameters.call(p4);
    }
    
    public void saveAll() {
        saveInsuranceRates();
        saveRemainderParameters();
        saveVehicleTypes();
        saveBusinessTypes();
        saveProjectCategory();
        savePayments();
        saveBackup();
        saveLicense();
    }
    
    private void backup() {
        if (checkPath()) {
            readBytes = 0L;
            progressInfo.setVisible(true);
            p6.setVisible(true);
            p6.setProgress(0);
            File dest = new File(pathField.getText());
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
                                p6.setVisible(false);
//                                progressInfo.setVisible(false);
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
            
            if (!retain.isSelected()) {
                File[] backups = dest.listFiles();
                for (File backupFile : backups) {
                    if (!backupFile.getName().equals(newName)) {
                        
                        try {
                            Date d = f.parse(backupFile.getName());
                            if (d != null) {
                                backupFile.delete();
                            }
                        } catch (ParseException ex) {
                            org.apache.logging.log4j.Logger logger = LogManager.getLogger(SettingsDialog.class.getName());
                            logger.info("备份文件"+backupFile.getName()+"无法删除");
                        }
                    }
                }
            }
        }
    }
    
    private boolean checkPath() {
        if (pathField.getText().isEmpty()) {
            MessageBox.showMessage("请输入备份目标目录。");
            return false;
        } else {
            File dir = new File(pathField.getText());
            if (!dir.exists()) {
                MessageBox.showMessage("目标目录不存在。");
                return false;
            }
        }
        return true;
    }
    
    public void setSelectedIndex(int index) {
        tabPane.getSelectionModel().select(index);
    }
    
    private TabPane tabPane;
    
    // 机动车种类
    private List<VehicleType> listVehicleType;
    private TextField vehicleTypeField = new TextField();
    private ListView<VehicleType> vehicleTypeList = new ListView<VehicleType>();
    private ObservableList<VehicleType> vehicleTypeItems = FXCollections.observableArrayList ();
    
    // 业务类型
    private List<BusinessType> listBusinessType;
    private TextField businessTypeField = new TextField();
    private ListView<BusinessType> businessTypeList = new ListView<BusinessType>();
    private ObservableList<BusinessType> businessTypeItems = FXCollections.observableArrayList ();
    
    // 支付方式
    private List<Payment> listPayment;
    private TextField paymentField = new TextField();
    private ListView<Payment> paymentList = new ListView<Payment>();
    private ObservableList<Payment> paymentItems = FXCollections.observableArrayList ();
    
    // 项目分类
    private List<ProjectCategory> listProjectCategory;
    private TextField projectCategoryField = new TextField();
    private ListView<ProjectCategory> projectCategoryList = new ListView<ProjectCategory>();
    private ObservableList<ProjectCategory> projectCategoryItems = FXCollections.observableArrayList ();
    
    // 保险费率
    private NumericTextField commercialInsuranceRate = new NumericTextField();
    private NumericTextField compulsoryInsuranceRate = new NumericTextField();
    
    // 提醒剩余天数
    private NumericTextField BYTS = new NumericTextField();
    private NumericTextField BXTS = new NumericTextField();
    private NumericTextField NSTS = new NumericTextField();
    private NumericTextField HFTS = new NumericTextField();
    
    /** 备份路径 */
    private TextField pathField = new TextField();
    private Label progressInfo = new Label("");
    private ProgressBar p6 = new ProgressBar(0);
    private CheckBox retain = new CheckBox("保留上次备份文件");
    private CheckBox exit = new CheckBox("退出前备份");
    private long readBytes = 0L;
    
    /** 序列号 */
    private TextField snField = new TextField();
    
    private Map<String, String> allParameters;
    private SettingsDialogController controller = new SettingsDialogController();
    
}
