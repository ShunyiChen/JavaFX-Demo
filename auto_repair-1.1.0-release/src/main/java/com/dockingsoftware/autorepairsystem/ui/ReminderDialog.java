/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.ui;

import com.dockingsoftware.autorepairsystem.MainApp;
import com.dockingsoftware.autorepairsystem.component.DatePickerCell;
import com.dockingsoftware.autorepairsystem.component.EditingCell;
import com.dockingsoftware.autorepairsystem.component.IntegerCell;
import com.dockingsoftware.autorepairsystem.component.model.CustomerModel;
import com.dockingsoftware.autorepairsystem.persistence.entity.Customer;
import com.dockingsoftware.autorepairsystem.ui.controller.ReminderDialogController;
import com.dockingsoftware.autorepairsystem.util.DateUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.springframework.beans.BeanUtils;

public class ReminderDialog extends Dialog {

    public ReminderDialog() {
        initComponents();
    }
    
    private void initComponents() {
        initOwner(MainApp.getInstance().getPrimaryStage());
        
        setTitle("客户提醒");
        setHeaderText("可选择条件提醒。");
        
        getDialogPane().getButtonTypes().addAll(ButtonType.OK);
        Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
        okButton.setText("关闭");
//        okButton.setDisable(true);
        // Do some validation (using the Java 8 lambda syntax).
//        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
//            okButton.setDisable(newValue.trim().isEmpty());
//        });

        initSystemParameters();

        VBox vbox = new VBox();
        vbox.getChildren().addAll(createGridPane(), createTablePane());
        getDialogPane().setContent(vbox);
    }
    
    private void initSystemParameters() {
        Map<String, String> keyValues = (Map<String, String>) controller.listAllParameters.call("");
        byts = Integer.parseInt(keyValues.get("BYTS").isEmpty() ? "0" : keyValues.get("BYTS"));
        bxts = Integer.parseInt(keyValues.get("BXTS").isEmpty() ? "0" : keyValues.get("BXTS"));
        nsts = Integer.parseInt(keyValues.get("NSTS").isEmpty() ? "0" : keyValues.get("NSTS"));
        hfts = Integer.parseInt(keyValues.get("HFTS").isEmpty() ? "0" : keyValues.get("HFTS"));
    }
    
    private GridPane createGridPane() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 10, 10));
        
        BY.setSelected(true);
        BX.setSelected(true);
        NS.setSelected(true);
        HF.setSelected(true);
        
        grid.add(BY, 0, 0);
        grid.add(BX, 1, 0);
        grid.add(NS, 2, 0);
        grid.add(HF, 3, 0);
        
        BY.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                loadData();
            }
        });
        BX.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                loadData();
            }
        });
        NS.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                loadData();
            }
        });
        HF.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                loadData();
            }
        });
        
        return grid;
    }
    
    private VBox createTablePane() {
        
        Callback<TableColumn, TableCell> cellFactory = new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn p) {
                if ("nextMaintenanceDate".equals(p.getId())) {
                    return new DatePickerCell();
                } else if ("nextMaintenanceMilage".equals(p.getId())) {
                    return new IntegerCell();
                } else {
                    return new EditingCell();
                }
            }
        };
        
        TableColumn No = new TableColumn("序号");
        TableColumn reminderType = new TableColumn("提醒类型");
        TableColumn licensePlateNumber = new TableColumn("车牌号");
        TableColumn model = new TableColumn("车型");
        TableColumn name = new TableColumn("客户名称");
        TableColumn phoneNo = new TableColumn("联系手机");
        TableColumn tel = new TableColumn("单位电话");
        TableColumn insuranceCompany = new TableColumn("保险公司");
        TableColumn nextMaintenanceMilage = new TableColumn("下次保养里程");
        TableColumn nextMaintenanceDate = new TableColumn("下次保养时间");
        
        No.setId("No");
        reminderType.setId("reminderType");
        licensePlateNumber.setId("licensePlateNumber");
        model.setId("model");
        name.setId("name");
        phoneNo.setId("phoneNo");
        tel.setId("tel");
        insuranceCompany.setId("insuranceCompany");
        nextMaintenanceMilage.setId("nextMaintenanceMilage");
        nextMaintenanceDate.setId("nextMaintenanceDate");
        
        No.setPrefWidth(50);
        reminderType.setPrefWidth(120);
        licensePlateNumber.setPrefWidth(120);
        model.setPrefWidth(120);
        name.setPrefWidth(120);
        phoneNo.setPrefWidth(120);
        tel.setPrefWidth(120);
        insuranceCompany.setPrefWidth(120);
        nextMaintenanceMilage.setPrefWidth(120);
        nextMaintenanceDate.setPrefWidth(120);

        No.setCellFactory(cellFactory);
        reminderType.setCellFactory(cellFactory);
        licensePlateNumber.setCellFactory(cellFactory);
        model.setCellFactory(cellFactory);
        name.setCellFactory(cellFactory);
        phoneNo.setCellFactory(cellFactory);
        tel.setCellFactory(cellFactory);
        insuranceCompany.setCellFactory(cellFactory);
        nextMaintenanceMilage.setCellFactory(cellFactory);
        nextMaintenanceDate.setCellFactory(cellFactory);
        
        No.setCellValueFactory(new PropertyValueFactory<CustomerModel, String>("No"));
        reminderType.setCellValueFactory(new PropertyValueFactory<CustomerModel, String>("reminderType"));
        licensePlateNumber.setCellValueFactory(new PropertyValueFactory<CustomerModel, String>("licensePlateNumber"));
        model.setCellValueFactory(new PropertyValueFactory<CustomerModel, String>("model"));
        name.setCellValueFactory(new PropertyValueFactory<CustomerModel, String>("name"));
        phoneNo.setCellValueFactory(new PropertyValueFactory<CustomerModel, String>("phoneNo"));
        tel.setCellValueFactory(new PropertyValueFactory<CustomerModel, String>("tel"));
        insuranceCompany.setCellValueFactory(new PropertyValueFactory<CustomerModel, String>("insuranceCompany"));
        nextMaintenanceMilage.setCellValueFactory(new PropertyValueFactory<CustomerModel, String>("nextMaintenanceMilage"));
        nextMaintenanceDate.setCellValueFactory(new PropertyValueFactory<CustomerModel, String>("nextMaintenanceDate"));
        
        table.setItems(data);
        table.getColumns().addAll(No, reminderType, licensePlateNumber, model, name, phoneNo, tel, insuranceCompany, nextMaintenanceMilage, nextMaintenanceDate);
        table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY );
        
//        table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
//            @Override
//            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
//                disable(false);
//            }
//        });
        
        // 表格双击
        table.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (t.getClickCount() == 2) {
                    CustomerModel selectedModel = (CustomerModel) table.getSelectionModel().getSelectedItem();
                    if (selectedModel != null) {
                        Customer customer = (Customer) controller.getCustomerByID.call(selectedModel.getId());
                        AddOrUpdateCustomerDialog dia = new AddOrUpdateCustomerDialog(customer);
                        dia.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> dia.saveOrUpdateCustomer(c -> loadData()));
                    }
                }
            }
        });

        loadData();
 
        VBox vbox = new VBox();
        vbox.getChildren().addAll(table);
        VBox.setVgrow(table, Priority.ALWAYS);
        return vbox;
    }
    
    private void loadData() {
        
        data.clear();
        
        Date currentDate = new Date();
        // 保养提醒-剩余毫秒
        long ms1 = byts * 24 * 60 * 60L * 1000L;
        // 保险提醒-剩余毫秒
        long ms2 = bxts * 24 * 60 * 60L * 1000L;
        // 年审提醒-剩余毫秒
        long ms3 = nsts * 24 * 60 * 60L * 1000L;
        // 回访提醒-剩余毫秒
        long ms4 = hfts * 24 * 60 * 60L * 1000L;
        
        List<Customer> lstCustomer = (List<Customer>) controller.listCustomer.call(null);
        int num = 1;
        for (Customer customer : lstCustomer) {
            
            if (BY.isSelected()) {
                Date nextMaintenanceDate = customer.getNextMaintenanceDate();
                if (nextMaintenanceDate.getTime() > currentDate.getTime()) {
                    if ((nextMaintenanceDate.getTime() - currentDate.getTime()) < ms1) {
                        CustomerModel customerModel = new CustomerModel();
                        
                        BeanUtils.copyProperties(customer, customerModel);

                        customerModel.setRegistrationDate(DateUtils.Date2LocalDate(customer.getRegistrationDate()));
                        customerModel.setNextMaintenanceDate(DateUtils.Date2LocalDate(customer.getNextMaintenanceDate()));
                        customerModel.setNextVisitDate(DateUtils.Date2LocalDate(customer.getNextVisitDate()));
                        customerModel.setNextAnnualReviewDate(DateUtils.Date2LocalDate(customer.getNextAnnualReviewDate()));

                        customerModel.setNo(""+(num++));
                        customerModel.setReminderType("保养提醒");

                        data.add(customerModel);
                    }
                }
            }
            if (BX.isSelected()) {
                Calendar a = Calendar.getInstance();
                String[] monthAndDay = customer.getInsuranceMonthDay().split("/");
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-d");
                Date insuranceDate = null;
                try {
                    insuranceDate = formatter.parse(a.get(Calendar.YEAR)+"-"+monthAndDay[0]+"-"+monthAndDay[1]);
                } catch (ParseException ex) {
                    Logger.getLogger(ReminderDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                if (insuranceDate.getTime() > currentDate.getTime()) {
                    if ((insuranceDate.getTime() - currentDate.getTime()) < ms2) {
                        CustomerModel customerModel = new CustomerModel();
                        
                        BeanUtils.copyProperties(customer, customerModel);

                        customerModel.setRegistrationDate(DateUtils.Date2LocalDate(customer.getRegistrationDate()));
                        customerModel.setNextMaintenanceDate(DateUtils.Date2LocalDate(customer.getNextMaintenanceDate()));
                        customerModel.setNextVisitDate(DateUtils.Date2LocalDate(customer.getNextVisitDate()));
                        customerModel.setNextAnnualReviewDate(DateUtils.Date2LocalDate(customer.getNextAnnualReviewDate()));

                        customerModel.setNo(""+(num++));
                        customerModel.setReminderType("保险提醒");

                        data.add(customerModel);
                    }
                }
            }
            // 年审提醒判断
            if (NS.isSelected()) {
                Date nextAnnualReviewDate = customer.getNextAnnualReviewDate();
                if (nextAnnualReviewDate.getTime() > currentDate.getTime()) {
                    if ((nextAnnualReviewDate.getTime() - currentDate.getTime()) < ms3) {
                        CustomerModel customerModel = new CustomerModel();

                        BeanUtils.copyProperties(customer, customerModel);

                        customerModel.setRegistrationDate(DateUtils.Date2LocalDate(customer.getRegistrationDate()));
                        customerModel.setNextMaintenanceDate(DateUtils.Date2LocalDate(customer.getNextMaintenanceDate()));
                        customerModel.setNextVisitDate(DateUtils.Date2LocalDate(customer.getNextVisitDate()));
                        customerModel.setNextAnnualReviewDate(DateUtils.Date2LocalDate(customer.getNextAnnualReviewDate()));

                        customerModel.setNo(""+(num++));
                        customerModel.setReminderType("年审提醒");

                        data.add(customerModel);
                    }
                }
            }
            
            // 回访提醒判断
            if (HF.isSelected()) {
                Date nextVisitDate = customer.getNextVisitDate();
                if (nextVisitDate.getTime() > currentDate.getTime()) {
                    if ((nextVisitDate.getTime() - currentDate.getTime()) < ms4) {
                        CustomerModel customerModel = new CustomerModel();

                        BeanUtils.copyProperties(customer, customerModel);

                        customerModel.setRegistrationDate(DateUtils.Date2LocalDate(customer.getRegistrationDate()));
                        customerModel.setNextMaintenanceDate(DateUtils.Date2LocalDate(customer.getNextMaintenanceDate()));
                        customerModel.setNextVisitDate(DateUtils.Date2LocalDate(customer.getNextVisitDate()));
                        customerModel.setNextAnnualReviewDate(DateUtils.Date2LocalDate(customer.getNextAnnualReviewDate()));

                        customerModel.setNo(""+(num++));
                        customerModel.setReminderType("回访提醒");

                        data.add(customerModel);
                    }
                }
            }
        }
    }
    
    private int byts = 0;
    private int bxts = 0;
    private int nsts = 0;
    private int hfts = 0;
    private CheckBox BY = new CheckBox("保养提醒");
    private CheckBox BX = new CheckBox("保险提醒");
    private CheckBox NS = new CheckBox("年审提醒");
    private CheckBox HF = new CheckBox("回访提醒");
    private TableView table = new TableView();
    private final ObservableList<CustomerModel> data = FXCollections.observableArrayList();
    private ReminderDialogController controller = new ReminderDialogController();
}
