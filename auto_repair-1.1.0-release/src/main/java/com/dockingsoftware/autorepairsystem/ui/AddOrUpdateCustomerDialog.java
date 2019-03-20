/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.ui;

import com.dockingsoftware.autorepairsystem.MainApp;
import com.dockingsoftware.autorepairsystem.component.NumericTextField;
import com.dockingsoftware.autorepairsystem.component.StringConverterExt;
import com.dockingsoftware.autorepairsystem.persistence.entity.Customer;
import com.dockingsoftware.autorepairsystem.persistence.entity.VehicleType;
import com.dockingsoftware.autorepairsystem.ui.controller.AddOrUpdateCustomerDialogController;
import com.dockingsoftware.autorepairsystem.util.DateUtils;
import com.dockingsoftware.autorepairsystem.util.ValidateUtils;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class AddOrUpdateCustomerDialog extends Dialog {

    /**
     * Constructor.
     * 
     */
    public AddOrUpdateCustomerDialog() {
        this(null);
    }
    
    /**
     * Constructor.
     * 
     * @param customer 
     */
    public AddOrUpdateCustomerDialog(Customer customer) {
        this.customer = customer;
        initComponents();
    }
    
    private void initComponents() {
        initOwner(MainApp.getInstance().getPrimaryStage());
        
        setTitle("新建客户");
        setHeaderText("请填写新客户信息。注意车牌号格式：辽BB8K57。");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        
        grid.add(new Label("客户名称:"), 0, 0);
        grid.add(name, 1, 0);
        
        grid.add(new Label("联系电话:"), 2, 0);
        grid.add(phoneNo, 3, 0);
        
        grid.add(new Label("地址:"), 4, 0);
        grid.add(address, 5, 0);
        
        grid.add(new Label("客户单位:"), 6, 0);
        grid.add(company, 7, 0);
        
        grid.add(new Label("单位联系电话:"), 0, 1);
        grid.add(tel, 1, 1);
        
        grid.add(new Label("最新公里数:"), 2, 1);
        grid.add(latestMileageField, 3, 1);
        
        grid.add(new Label("客户备注:"), 4, 1);
        grid.add(notes, 5, 1);
        
        grid.add(new Label("车辆备注:"), 6, 1);
        grid.add(carNotes, 7, 1);
        
        grid.add(new Label("号码号牌:"), 0, 2);
        licensePlateNumber.setText("辽B");
        grid.add(licensePlateNumber, 1, 2);
        
        grid.add(new Label("厂牌型号:"), 2, 2);
        grid.add(factoryPlateModel, 3, 2); 
        
        grid.add(new Label("VIN码:"), 4, 2);
        grid.add(vinCode, 5, 2);
        
        grid.add(new Label("机动车种类:"), 6, 2);
        List<VehicleType> lstVehicleType = (List<VehicleType>) controller.listVehicleType.call("");
        for (VehicleType b : lstVehicleType) {
            vehicleType.getItems().add(b.getName());
        }
        vehicleType.setPrefWidth(173);
        vehicleType.getSelectionModel().select(0);
        grid.add(vehicleType, 7, 2);
        
        grid.add(new Label("发动机号:"), 0, 3);
        grid.add(engineNo, 1, 3);
        
        grid.add(new Label("登记日期:"), 2, 3);
        registrationDate.setEditable(false);
        registrationDate.setValue(LocalDate.now());
        grid.add(registrationDate, 3, 3);
        
        grid.add(new Label("车型:"), 4, 3);
        grid.add(modelField, 5, 3);
        
        grid.add(new Label("车身颜色:"), 6, 3);
        grid.add(color, 7, 3);
        
        grid.add(new Label("下次保养里程:"), 0, 4);
        grid.add(nextMaintenanceMilage, 1, 4);
        
        grid.add(new Label("下次保养时间:"), 2, 4);
        nextMaintenanceDate.setEditable(false);
        nextMaintenanceDate.setValue(LocalDate.now());
        grid.add(nextMaintenanceDate, 3, 4);
        
        grid.add(new Label("保险月日:"), 4, 4);
        HBox hbox = new HBox();
        hbox.getChildren().addAll(spinnerMonth, spinnerDay);
        spinnerMonth.setPrefWidth(74);
        spinnerMonth.setValueFactory(svfMonth);
        spinnerMonth.setEditable(false);
        spinnerDay.setPrefWidth(74);
        spinnerDay.setValueFactory(svfDay);
        spinnerDay.setEditable(false);
        
        svfDay.setWrapAround(true);
        svfMonth.setWrapAround(true);
        svfMonth2.setWrapAround(true);
        
        grid.add(hbox, 5, 4);
        
        grid.add(new Label("保险公司:"), 6, 4);
        grid.add(insuranceCompany, 7, 4);
        
        grid.add(new Label("保险备注:"), 0, 5);
        grid.add(insuranceNotes, 1, 5);
        
        grid.add(new Label("检车月份:"), 2, 5);
        inspectionMonth.setValueFactory(svfMonth2);
        inspectionMonth.setPrefWidth(173);
        inspectionMonth.setEditable(false);
        grid.add(inspectionMonth, 3, 5);
        
        grid.add(new Label("下次回访时间:"), 4, 5);
        nextVisitDate.setEditable(false);
        nextVisitDate.setValue(LocalDate.now());
        grid.add(nextVisitDate, 5, 5);
        
        grid.add(new Label("下次年审时间:"), 6, 5);
        nextAnnualReviewDate.setEditable(false);
        nextAnnualReviewDate.setValue(LocalDate.now());
        grid.add(nextAnnualReviewDate, 7, 5);
        
        registrationDate.setConverter(new StringConverterExt());
        nextMaintenanceDate.setConverter(new StringConverterExt());
        nextVisitDate.setConverter(new StringConverterExt());
        nextAnnualReviewDate.setConverter(new StringConverterExt());         
 
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
        okButton.setText("确定");
        Button cancelButton = (Button) getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelButton.setText("取消");
        licensePlateNumber.textProperty().addListener((observable, oldValue, newValue) -> {
            boolean bool = ValidateUtils.isCarnumberNO(licensePlateNumber.getText());
            okButton.setDisable(!bool);
        });
        
        getDialogPane().setContent(grid);
        
        if (customer != null) {
            setTitle("更改客户信息");
            setHeaderText("请填写客户信息。");
            name.setText(customer.getName());
            phoneNo.setText(customer.getPhoneNo());
            address.setText(customer.getAddress());
            company.setText(customer.getCompany());
            tel.setText(customer.getTel());
            notes.setText(customer.getNotes());
            carNotes.setText(customer.getCarNotes());
            latestMileageField.setText(customer.getLatestMileage() + "");
            licensePlateNumber.setText(customer.getLicensePlateNumber());
            factoryPlateModel.setText(customer.getFactoryPlateModel());
            vinCode.setText(customer.getVinCode());
            vehicleType.setValue(customer.getVehicleTypeName());
            engineNo.setText(customer.getEngineNo());
            registrationDate.setValue(DateUtils.Date2LocalDate(customer.getRegistrationDate()));
            modelField.setText(customer.getModel());
            color.setText(customer.getColor());
            // 以下是提醒用字段
            nextMaintenanceMilage.setText(customer.getNextMaintenanceMilage()+"");
            nextMaintenanceDate.setValue(DateUtils.Date2LocalDate(customer.getNextMaintenanceDate()));
            nextVisitDate.setValue(DateUtils.Date2LocalDate(customer.getNextVisitDate()));
            nextAnnualReviewDate.setValue(DateUtils.Date2LocalDate(customer.getNextAnnualReviewDate()));
            // 提醒保险月和日
            String[] monthDay = customer.getInsuranceMonthDay().split("/");
            spinnerMonth.getValueFactory().setValue(Integer.parseInt(monthDay[0]));
            spinnerDay.getValueFactory().setValue(Integer.parseInt(monthDay[1]));
            // 提醒检车月
            inspectionMonth.getValueFactory().setValue(customer.getInspectionMonth());
            insuranceCompany.setText(customer.getInsuranceCompany());
            insuranceNotes.setText(customer.getInsuranceNotes());
            
            okButton.setDisable(false);
        } else {
            okButton.setDisable(true);
        }
        
    }

    public void saveOrUpdateCustomer(Consumer<Customer> consumer) {
        if (customer == null) {
            customer = new Customer();
        }
        customer.setName(name.getText());
        customer.setPhoneNo(phoneNo.getText());
        customer.setAddress(address.getText());
        customer.setCompany(company.getText());
        customer.setTel(tel.getText());
        customer.setNotes(notes.getText());
        customer.setCarNotes(carNotes.getText());
        if (!latestMileageField.getText().isEmpty()) {
            customer.setLatestMileage(Integer.parseInt(latestMileageField.getText()));
        }
        customer.setLicensePlateNumber(licensePlateNumber.getText());
        customer.setFactoryPlateModel(factoryPlateModel.getText());
        customer.setVinCode(vinCode.getText());
        customer.setVehicleTypeName(vehicleType.getValue());
        customer.setEngineNo(engineNo.getText());
        customer.setModel(modelField.getText());
        customer.setColor(color.getText());
        customer.setInsuranceCompany(insuranceCompany.getText());
        customer.setInsuranceNotes(insuranceNotes.getText());
        if (!nextMaintenanceMilage.getText().isEmpty()) {
            customer.setNextMaintenanceMilage(Integer.parseInt(nextMaintenanceMilage.getText()));
        }
        customer.setNextMaintenanceDate(DateUtils.LocalDate2Date(nextMaintenanceDate.getValue()));
        customer.setRegistrationDate(DateUtils.LocalDate2Date(registrationDate.getValue()));
        customer.setNextVisitDate(DateUtils.LocalDate2Date(nextVisitDate.getValue()));
        customer.setNextAnnualReviewDate(DateUtils.LocalDate2Date(nextAnnualReviewDate.getValue()));
        customer.setInsuranceMonthDay(spinnerMonth.getValue()+"/"+spinnerDay.getValue());
        customer.setInspectionMonth(Integer.parseInt(inspectionMonth.getValue().toString()));
        controller.saveOrUpdateCustomer.call(customer);
        consumer.accept(customer);
    }
    
    /** 客户姓名 */
    private TextField name = new TextField();
    /** 联系手机 */
    private TextField phoneNo = new TextField();
    /** 客户地址 */
    private TextField address = new TextField();
    /** 客户单位 */
    private TextField company = new TextField();
    /** 客户单位电话 */
    private TextField tel = new TextField();
    /** 最新里程 */
    private NumericTextField latestMileageField = new NumericTextField();
    /** 客户备注 */
    private TextField notes = new TextField();
    /** 车辆备注 */
    private TextField carNotes = new TextField();
    /** 号码号牌 */
    private TextField licensePlateNumber = new TextField();
    /** 厂牌型号 */
    private TextField factoryPlateModel  = new TextField();
     /** VIN码 */
    private TextField vinCode = new TextField();
    /** 机动车种类 */
    private ComboBox<String> vehicleType = new ComboBox<String>();
    /** 发动机号 */
    private TextField engineNo = new TextField();
    /** 登记日期 */
    private DatePicker registrationDate = new DatePicker();
    /** 车型 */
    private TextField modelField = new TextField();
    /** 车身颜色 */
    private TextField color = new TextField();
    /** 下次保养里程 */
    private NumericTextField nextMaintenanceMilage = new NumericTextField();
    /** 下次保养时间 */
    private DatePicker nextMaintenanceDate = new DatePicker();
    /** 提醒保险日期（月,日）*/
    private SpinnerValueFactory svfMonth = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12);
    private Spinner spinnerMonth = new Spinner();
    private SpinnerValueFactory svfDay = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 31);
    private Spinner spinnerDay = new Spinner();
    
    /** 提醒检车月份（月） */
    private SpinnerValueFactory svfMonth2 = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12);
    private Spinner inspectionMonth = new Spinner();
    
    /** 保险公司 */
    private TextField insuranceCompany = new TextField();
    /** 保险备注 */
    private TextField insuranceNotes = new TextField();
    /** 下次回访时间 */
    private DatePicker nextVisitDate = new DatePicker();
    /** 下次年审时间 */
    private DatePicker nextAnnualReviewDate = new DatePicker();
    
    private AddOrUpdateCustomerDialogController controller = new AddOrUpdateCustomerDialogController();
    private Customer customer;
}
