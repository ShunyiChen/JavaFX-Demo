/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.ui;

import com.dockingsoftware.autorepairsystem.Constants;
import com.dockingsoftware.autorepairsystem.MainApp;
import com.dockingsoftware.autorepairsystem.common.Generator;
import com.dockingsoftware.autorepairsystem.component.EditingCell;
import com.dockingsoftware.autorepairsystem.component.MessageBox;
import com.dockingsoftware.autorepairsystem.component.NumericTextField;
import com.dockingsoftware.autorepairsystem.component.model.TenantCarModel;
import com.dockingsoftware.autorepairsystem.persistence.entity.Rental;
import com.dockingsoftware.autorepairsystem.persistence.entity.Tenant;
import com.dockingsoftware.autorepairsystem.persistence.entity.TenantCar;
import com.dockingsoftware.autorepairsystem.ui.controller.RentalDetailsDialogController;
import com.dockingsoftware.autorepairsystem.util.DateUtils;
import com.dockingsoftware.autorepairsystem.util.ImageUtils;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;

public class RentalDetailsDialog extends Dialog {

    /**
     * Constructor.
     */
    public RentalDetailsDialog() {
        initComponents();
    }
    
    /**
     * Constructor.
     * 
     * @param editRental 
     */
    public RentalDetailsDialog(Rental editRental) {
        this.editRental = editRental;
        initComponents();
        
        edit();
    }
    
    private void initComponents() {
        initOwner(MainApp.getInstance().getPrimaryStage());
//        if (editSettlementDetails != null) {
//            setTitle("编辑");
//        } else {
            setTitle("租户开单");
//        }
        setHeaderText("请填写租户信息。");
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
//        if (editSettlementDetails == null) {
            okButton.setText("保存");
//        } else {
//            okButton.setText("保存");
//        }
        Button cancelButton = (Button) getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelButton.setText("取消");
//        VBox vbox = new VBox();
        
        okButton.setDisable(true);
        
        tenantNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            okButton.setDisable(tenantNameField.getText().isEmpty());
        });
        
//        vbox.getChildren().addAll(pane);
        getDialogPane().setContent(createRentalPane());
        getDialogPane().setPrefWidth(1200);
        getDialogPane().setPrefHeight(700);
    }
    
    private ToolBar createToolBar() {
        ToolBar bar = new ToolBar();
        
        btnTenantChooser.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                ChooseTenantDialog dia = new ChooseTenantDialog();
                dia.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> dia.returnTenant(t -> setTenantFieldValues(t)));
            }
        });
        
        btnTenantCreator.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                AddOrUpdateTenantDialog dia = new AddOrUpdateTenantDialog();
                dia.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> dia.saveOrUpdateTenant(t -> setTenantFieldValues(t)));
            }
        });
        
        bar.getItems().addAll(btnTenantChooser, btnTenantCreator);//, new Separator(), btnSave, btnCancel, btnStatement);
        return bar;
    }
    
    private VBox createRentalPane() {
        VBox vbox = new VBox();
        vbox.getChildren().addAll(createToolBar(), createInputPane(), createTablePane(), createAmountFieldPane());
        
        return vbox;
    }
    
    private GridPane createInputPane() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 20, 5, 10));
        
        grid.add(new Label("租户名:"), 0, 0);
        tenantNameField.setEditable(false);
        grid.add(tenantNameField, 1, 0);
        
        grid.add(new Label("联系人:"), 0, 1);
        contactsField.setEditable(false);
        grid.add(contactsField, 1, 1);
        
        grid.add(new Label("联系电话:"), 0, 2);
        phoneNoField.setEditable(false);
        phoneNoField.setPrefWidth(173);
        grid.add(phoneNoField, 1, 2);
        
        grid.add(btnEdit, 4, 0);
        
        grid.add(new Label("开单日期*:"), 0, 3);
        billingDateField.setEditable(false);
        billingDateField.setValue(LocalDate.now());
        billingDateField.setPromptText("开单日期");
        grid.add(billingDateField, 1, 3);
        
        grid.add(new Label("历史消费:"), 2, 0);
        historicalAmountField.setEditable(false);
        grid.add(historicalAmountField, 3, 0);
        
        grid.add(new Label("历史单数:"), 2, 1);
        historicalNumberField.setEditable(false);
        grid.add(historicalNumberField, 3, 1);
        
        grid.add(new Label("历史欠款:"), 2, 2);
        historicalOwingField.setEditable(false);
        grid.add(historicalOwingField, 3, 2);
        
        grid.add(new Label("结算状态*:"), 2, 3);
        settlementStateField.getItems().addAll("未结算", "结算中", "已结算");
        settlementStateField.getSelectionModel().select(0);
        settlementStateField.setPrefWidth(173);
        grid.add(settlementStateField, 3, 3);
        
        btnEdit.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                AddOrUpdateTenantDialog dia = new AddOrUpdateTenantDialog(editTenant);
                dia.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> dia.saveOrUpdateTenant(t -> setTenantFieldValues(t)));
            }
        });
        
        return grid;
    }
 
    private BorderPane createTablePane() {
        BorderPane pane = new BorderPane();
        pane.setPadding(Insets.EMPTY);
        
        ToolBar toolbar = new ToolBar();
        toolbar.setPadding(new Insets(5, 5, 5, 5));
        
        btnAdd.setOnAction(new EventHandler() { 
            @Override
            public void handle(Event event) {
                add();
            }
        });
        
        btnUpdate.setOnAction(new EventHandler() { 
            @Override
            public void handle(Event event) {
                update();
            }
        });
        
        btnRemove.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                remove();
            }
        });
        
        toolbar.getItems().addAll(btnAdd, btnUpdate, btnRemove);
        pane.setTop(toolbar);
        
        Callback<TableColumn, TableCell> cellFactory = new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn p) {
                return new EditingCell();
            }
        };
        
        TableColumn No = new TableColumn("序号");
        TableColumn licensePlateNumberCol = new TableColumn("车牌号*");
        TableColumn paintPartsCol = new TableColumn("烤漆部位*");
        TableColumn notesCol = new TableColumn("备注*");
        
        licensePlateNumberCol.setEditable(true);
        paintPartsCol.setEditable(true);
        notesCol.setEditable(true);
        
        // 设置列宽
        No.setPrefWidth(50);
        licensePlateNumberCol.setPrefWidth(120);
        paintPartsCol.setPrefWidth(220);
        notesCol.setPrefWidth(220);
        
        // 设置可编辑列
        No.setCellFactory(cellFactory);
        licensePlateNumberCol.setCellFactory(cellFactory);
        paintPartsCol.setCellFactory(cellFactory);
        notesCol.setCellFactory(cellFactory);
         
        No.setCellValueFactory(new PropertyValueFactory<TenantCarModel, String>("No"));
        licensePlateNumberCol.setCellValueFactory(new PropertyValueFactory<TenantCarModel, String>("licensePlateNumber"));
        paintPartsCol.setCellValueFactory(new PropertyValueFactory<TenantCarModel, BigDecimal>("paintParts"));
        notesCol.setCellValueFactory(new PropertyValueFactory<TenantCarModel, Float>("notes"));
        
        licensePlateNumberCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TenantCarModel, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TenantCarModel, String> t) {
                TenantCarModel m = ((TenantCarModel) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                m.setLicensePlateNumber(t.getNewValue());
            }
        });
        
        paintPartsCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TenantCarModel, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TenantCarModel, String> t) {
                TenantCarModel m = ((TenantCarModel) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                m.setPaintParts(t.getNewValue());
            }
        });
        
        notesCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TenantCarModel, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TenantCarModel, String> t) {
                TenantCarModel m = ((TenantCarModel) t.getTableView().getItems().get(t.getTablePosition().getRow()));
                m.setNotes(t.getNewValue());
            }
        });
        
        ContextMenu menu = new ContextMenu();
        menu.getItems().addAll(itemAdd, itemRemove);
        itemAdd.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                add();
            }
        });
        itemRemove.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                remove();
            }
        });
        table.setContextMenu(menu);
        table.setEditable(true);
        table.setItems(data);
        table.getColumns().addAll(No, licensePlateNumberCol, paintPartsCol, notesCol);
        table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY );
        pane.setCenter(table);
        
        return pane;
    }
    
    private HBox createAmountFieldPane() {
        HBox hbox = new HBox();
        hbox.getChildren().addAll(new Label("租金："), amountField);
        hbox.setAlignment(Pos.CENTER);
        HBox.setHgrow(amountField, Priority.ALWAYS);
        return hbox;
    }
    
    private void setTenantFieldValues(Tenant t) {
        editTenant = t;
        
        tenantNameField.setText(editTenant.getName());
        contactsField.setText(editTenant.getContacts());
        phoneNoField.setText(editTenant.getPhoneNo());
        
        // 历史消费记录查询
        Criterion c = Restrictions.eq("tenantId", editTenant.getId());
        List<Rental> lstRental = (List<Rental>) controller.listRental.call(c);
        BigDecimal historicalAmount = new BigDecimal(0);
        int num = 0;
        BigDecimal historicalOwing = new BigDecimal(0);
        for (Rental r : lstRental) {
            historicalAmount = historicalAmount.add(r.getActuallyPay());
            num ++;
            historicalOwing = historicalOwing.add(r.getOwingAmount());
        }
        historicalAmountField.setText(historicalAmount.setScale(2, RoundingMode.HALF_UP).toString());
        historicalNumberField.setText(num + "");
        historicalOwingField.setText(historicalOwing.setScale(2, RoundingMode.HALF_UP).toString());
    }
    
    private void add() {
        if (editTenant == null) {
            MessageBox.showMessage("请先选择一个租户。");
        } else {
            AddOrUpdateTenantCarDialog dia = new AddOrUpdateTenantCarDialog();
            dia.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> dia.returnTenantCar(t -> insertRow(t)));
        }
    }
    
    private void update() {
        TenantCarModel selectedModel = (TenantCarModel) table.getSelectionModel().getSelectedItem();
        if (selectedModel != null) {
            AddOrUpdateTenantCarDialog dia = new AddOrUpdateTenantCarDialog(selectedModel);
            dia.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> dia.returnTenantCar(t -> updateRow(t)));
        }
    }
    
    private void remove() {
        TenantCarModel selectedModel = (TenantCarModel) table.getSelectionModel().getSelectedItem();
        data.remove(selectedModel);
        resortTableRows();
    }
    
    public void saveOrUpdateRental(Consumer<Rental> consumer) {
        String msg = "";
        if (editRental == null) {
            editRental = new Rental();
            String SN = Generator.generateSN(Constants.KFCZ);
            editRental.setSN(SN);
            editRental.setDiscountAmount(BigDecimal.ZERO);
            editRental.setReceivableAmount(BigDecimal.ZERO);
            editRental.setActuallyPay(BigDecimal.ZERO);
            editRental.setAmountReceived(BigDecimal.ZERO);
            editRental.setOwingAmount(BigDecimal.ZERO);
            msg = "新订单："+SN+"已生成并保存。";
        } else {
            msg = "订单："+editRental.getSN()+"已保存。";
        }
        editRental.setTenantId(editTenant.getId());
        editRental.setTenantName(editTenant.getName());
        editRental.setContacts(editTenant.getContacts());
        editRental.setPhoneNo(editTenant.getPhoneNo());
        editRental.setBillingDate(DateUtils.LocalDate2Date(billingDateField.getValue()));
        editRental.setSettlementState(settlementStateField.getValue());
        editRental.setReceivableAmount(new BigDecimal(amountField.getText()));
        
        editRental.getTenantCars().clear();
        for (TenantCarModel m : data) {
            TenantCar car = new TenantCar();
            BeanUtils.copyProperties(m, car);
            editRental.getTenantCars().add(car);
        }
        
        controller.saveOrUpdateRental.call(editRental);
        
        consumer.accept(editRental);
    
        MessageBox.showMessage(msg);
    }
    
    private void resortTableRows() {
        int rowNum = 1;
        for (TenantCarModel rm : data) {
            rm.setNo((rowNum ++) + "");
        }
    }
    
    private void edit() {
        billingDateField.setValue(DateUtils.Date2LocalDate(editRental.getBillingDate()));
        settlementStateField.setValue(editRental.getSettlementState());
        
        Tenant t = (Tenant) controller.getTenantById.call(editRental.getTenantId());
        setTenantFieldValues(t);
        
        int rowNum = 1;
        for (TenantCar car : editRental.getTenantCars()) {
            TenantCarModel m = new TenantCarModel();
            BeanUtils.copyProperties(car, m);
            m.setNo("" + rowNum);
            data.add(m);
            rowNum ++;
        }
        
        amountField.setText(editRental.getReceivableAmount().toString());
    }
    
    private void insertRow(TenantCar car) {
        TenantCarModel m = new TenantCarModel();
        BeanUtils.copyProperties(car, m);
        m.setNo("" + (data.size() + 1));
        data.add(m);
    }
    
    private void updateRow(TenantCar car) {
        TenantCarModel selectedModel = (TenantCarModel) table.getSelectionModel().getSelectedItem();
        BeanUtils.copyProperties(car, selectedModel);
        table.refresh();
    }
    
    private Button btnTenantChooser = new Button("选择租户");
    private Button btnTenantCreator = new Button("新建租户");
    private Button btnEdit = new Button("", ImageUtils.createImageView("edit_16px_505013_easyicon.net.png"));
    
    private TextField tenantNameField = new TextField();
    private TextField contactsField = new TextField();
    private TextField phoneNoField = new TextField();
    private DatePicker billingDateField  = new DatePicker();
    private NumericTextField historicalAmountField  = new NumericTextField();
    private NumericTextField historicalNumberField  = new NumericTextField();
    private NumericTextField historicalOwingField  = new NumericTextField();
    private ComboBox<String> settlementStateField = new ComboBox<String>();
    private NumericTextField amountField = new NumericTextField();
    private Button btnAdd = new Button("添加车辆", ImageUtils.createImageView("billing_16px_504987_easyicon.net.png"));
    private Button btnUpdate = new Button("更改", ImageUtils.createImageView("edit_16px_505013_easyicon.net.png"));
    private Button btnRemove = new Button("删除", ImageUtils.createImageView("edit_16px_505013_easyicon.net.png"));
    private MenuItem itemAdd = new MenuItem("添加车辆", ImageUtils.createImageView("billing_16px_504987_easyicon.net.png"));
    private MenuItem itemRemove = new MenuItem("删除", ImageUtils.createImageView("edit_16px_505013_easyicon.net.png"));
    private TableView table = new TableView();
    private final ObservableList<TenantCarModel> data = FXCollections.observableArrayList();
    
    private Tenant editTenant;
    private Rental editRental;
    private RentalDetailsDialogController controller = new RentalDetailsDialogController();
}
