/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.ui;

import com.dockingsoftware.autorepairsystem.Constants;
import com.dockingsoftware.autorepairsystem.MainApp;
import com.dockingsoftware.autorepairsystem.component.BigDecimalCell;
import com.dockingsoftware.autorepairsystem.component.DatePickerCell;
import com.dockingsoftware.autorepairsystem.component.EditingCell;
import com.dockingsoftware.autorepairsystem.component.MessageBox;
import com.dockingsoftware.autorepairsystem.component.NumericTextField;
import com.dockingsoftware.autorepairsystem.common.Generator;
import com.dockingsoftware.autorepairsystem.component.model.SettlementModel;
import com.dockingsoftware.autorepairsystem.persistence.entity.Factory;
import com.dockingsoftware.autorepairsystem.persistence.entity.Settlement;
import com.dockingsoftware.autorepairsystem.persistence.entity.SettlementDetails;
import com.dockingsoftware.autorepairsystem.ui.controller.FactoryBillingTabContentController;
import com.dockingsoftware.autorepairsystem.util.DateUtils;
import com.dockingsoftware.autorepairsystem.util.ImageUtils;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.springframework.beans.BeanUtils;
import java.math.RoundingMode;

public class FactoryBillingTabContent extends VBox {

    public FactoryBillingTabContent(Tab tab) {
        this.tab = tab;
        initComponents();
    }
    
    private void initComponents() {
        this.getChildren().addAll(createToolBar(), createGridPane(), createTablePane());
    }
    
    private ToolBar createToolBar() {
        ToolBar bar = new ToolBar();
        btnFactoryChooser.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                ChooseFactoryDialog dia = new ChooseFactoryDialog();
                dia.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> dia.returnFactory(f -> setFactoryFieldValues(f)));
            }
        });
        btnFactoryCreator.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                AddOrUpdateFactoryDialog dia = new AddOrUpdateFactoryDialog();
                dia.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> dia.saveOrUpdateFactory(f -> setFactoryFieldValues(f)));
            }
        });
        btnSave.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                if (checkFactoryInfo()) {
                    saveOrUpdateSettlement(true);
                }
            }
        });
        btnCancel.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                Callback callback = new Callback() {
                    @Override
                    public Object call(Object param) {
 
                        if (editSettlement != null) {
                            controller.deleteSettlement.call(editSettlement);
                        }
                        
                        tab.getTabPane().getTabs().remove(tab);
                        
                        return "";
                    }
                };
                
                MessageBox.showMessage("请确认是否作废该订单。", Alert.AlertType.CONFIRMATION, callback);
            }
        });
        btnStatement.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                List<SettlementDetails> lstDetails = new ArrayList<SettlementDetails>();
                for (SettlementModel m : tableData) {
                    if (m == totalModel) {
                        continue;
                    }
                    if (m.invitedProperty().getValue()) {
                        SettlementDetails d = (SettlementDetails) controller.getSettlementDetailsById.call(m.getId());
                        lstDetails.add(d);
                    }
                }
                Callback cb = new Callback() {
                    @Override
                    public Object call(Object param) {
                        loadTableData();
                        return "";
                    }
                };
                
                // 选中行Check
                int selectedRowNum = 0;
                for (SettlementModel m : tableData) {
                    if (m == totalModel) {
                        continue;
                    }
                    if (m.invitedProperty().getValue()) {
                        selectedRowNum ++;
                    }
                }
                if (selectedRowNum > 0) {
                    SettlementDialog dia = new SettlementDialog(lstDetails);
                    dia.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> dia.pay(cb));
                } else {
                    MessageBox.showMessage("请选中一行或多行结算。");
                }
            }
        });
        
        bar.getItems().addAll(btnFactoryChooser, btnFactoryCreator, new Separator(), btnSave, btnCancel, btnStatement);
        return bar;
    }
    
    private GridPane createGridPane() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 20, 5, 10));
        
        grid.add(new Label("送修厂名:"), 0, 0);
        factoryField.setEditable(false);
        grid.add(factoryField, 1, 0);
        
        grid.add(new Label("联系人:"), 0, 1);
        contactsField.setEditable(false);
        grid.add(contactsField, 1, 1);
        
        grid.add(new Label("联系人电话:"), 0, 2);
        phoneNoField.setEditable(false);
        grid.add(phoneNoField, 1, 2);
        
        grid.add(new Label("历史消费:"), 2, 0);
        historicalAmountField.setEditable(false);
        grid.add(historicalAmountField, 3, 0);
        
        grid.add(new Label("历史单数:"), 2, 1);
        historicalNumberField.setEditable(false);
        grid.add(historicalNumberField, 3, 1);
        
        grid.add(new Label("历史欠款:"), 2, 2);
        historicalOwingField.setEditable(false);
        grid.add(historicalOwingField, 3, 2);
        
        grid.add(new Label("开单日期:"), 0, 3);
        billingDateField.setValue(LocalDate.now());
        billingDateField.setEditable(false);
        grid.add(billingDateField, 1, 3);
        
        grid.add(btnEdit, 4, 0);
        grid.add(btnShowHistory, 4, 1);
        
        btnEdit.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                AddOrUpdateFactoryDialog dia = new AddOrUpdateFactoryDialog(editFactory);
                dia.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> dia.saveOrUpdateFactory(f -> setFactoryFieldValues(f)));
            }
        });
        
        btnShowHistory.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                if (checkFactoryInfo()) {
                    MainApp.getInstance().getTabContentUI().addTab("历史单据", new ViewHistoryTabContent(editFactory.getId()));
                }
            }
        });
        
        return grid;
    }
        
    private BorderPane createTablePane() {
        BorderPane pane = new BorderPane();
        pane.setPadding(Insets.EMPTY);
        
        table.setEditable(true);

        Callback<TableColumn, TableCell> cellFactory = new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn p) {
                if ("enteringDateCol".equals(p.getId())
                        || "settlementDateCol".equals(p.getId())) {
                    return new DatePickerCell();
                }
                else if ("discountAmountCol".equals(p.getId())
                        || "actuallyPayCol".equals(p.getId())
                        || "receivableAmountCol".equals(p.getId())
                        || "amountReceivedCol".equals(p.getId())
                        || "owingAmountCol".equals(p.getId())) {
                    return new BigDecimalCell();
                }
                else {
                    return new EditingCell();
                }
            }
        };
        
        TableColumn invitedCol = new TableColumn<>();
        invitedCol.setText("选择");
        invitedCol.setPrefWidth(50);
        invitedCol.setCellValueFactory(new PropertyValueFactory("invited"));
        invitedCol.setCellFactory(CheckBoxTableCell.forTableColumn(invitedCol));
        
        TableColumn No = new TableColumn("序号");
        TableColumn enteringDateCol = new TableColumn("进厂日期");
        TableColumn customerNameCol = new TableColumn("客户名称");
        TableColumn phoneNoCol = new TableColumn("联系手机");
        TableColumn licensePlateNumberCol = new TableColumn("车牌号");
        TableColumn modelCol = new TableColumn("车型");
        TableColumn businessTypeCol = new TableColumn("业务类型");
        TableColumn senderCol = new TableColumn("送修人");
        TableColumn receptionistCol = new TableColumn("接车人");
        
        TableColumn discountAmountCol = new TableColumn("优惠金额");
        TableColumn discountReasonCol = new TableColumn("优惠原因");
        TableColumn receivableAmountCol = new TableColumn("应收金额");
        TableColumn actuallyPayCol = new TableColumn("实际支付");
        TableColumn amountReceivedCol = new TableColumn("已收金额");
        TableColumn owingAmountCol = new TableColumn("尚欠金额");
        TableColumn settlementDateCol = new TableColumn("结算日期");
        TableColumn paymentCol = new TableColumn("结算方式");
        TableColumn settlementNotesCol = new TableColumn("结算备注");
        TableColumn businessStateCol = new TableColumn("业务状态");
        TableColumn settlementStateCol = new TableColumn("结算状态");
        
        TableColumn descriptionCol = new TableColumn("故障描述");
        TableColumn recommendCol = new TableColumn("维修建议");
        TableColumn notesCol = new TableColumn("备注");

        // 设置渲染ID
        enteringDateCol.setId("enteringDateCol");
        discountAmountCol.setId("discountAmountCol");
        receivableAmountCol.setId("receivableAmountCol");
        actuallyPayCol.setId("actuallyPayCol");
        amountReceivedCol.setId("amountReceivedCol");
        owingAmountCol.setId("owingAmountCol");
        settlementDateCol.setId("settlementDateCol");
        
        No.setEditable(false);
        enteringDateCol.setEditable(false);
        customerNameCol.setEditable(false);
        phoneNoCol.setEditable(false);
        licensePlateNumberCol.setEditable(false);
        modelCol.setEditable(false);
        businessTypeCol.setEditable(false);
        senderCol.setEditable(false);
        receptionistCol.setEditable(false);
        
        discountAmountCol.setEditable(false);
        discountReasonCol.setEditable(false);
        receivableAmountCol.setEditable(false);
        actuallyPayCol.setEditable(false);
        amountReceivedCol.setEditable(false);
        owingAmountCol.setEditable(false);
        settlementDateCol.setEditable(false);
        paymentCol.setEditable(false);
        settlementNotesCol.setEditable(false);
        businessStateCol.setEditable(false);
        settlementStateCol.setEditable(false);
        
        descriptionCol.setEditable(false);
        recommendCol.setEditable(false);
        notesCol.setEditable(false);
        
        // 设置列宽
        No.setPrefWidth(50);
        enteringDateCol.setPrefWidth(120);
        customerNameCol.setPrefWidth(120);
        phoneNoCol.setPrefWidth(120);
        licensePlateNumberCol.setPrefWidth(120);
        modelCol.setPrefWidth(120);
        businessTypeCol.setPrefWidth(120);
        senderCol.setPrefWidth(120);
        receptionistCol.setPrefWidth(120);
        
        discountAmountCol.setPrefWidth(120);
        actuallyPayCol.setPrefWidth(120);
        discountReasonCol.setPrefWidth(120);
        receivableAmountCol.setPrefWidth(120);
        amountReceivedCol.setPrefWidth(120);
        owingAmountCol.setPrefWidth(120);
        settlementDateCol.setPrefWidth(120);
        paymentCol.setPrefWidth(120);
        settlementNotesCol.setPrefWidth(120);
        businessStateCol.setPrefWidth(120);
        settlementStateCol.setPrefWidth(120);
        
        descriptionCol.setPrefWidth(120);
        recommendCol.setPrefWidth(120);
        notesCol.setPrefWidth(120);

        // 设置可编辑列
        No.setCellFactory(cellFactory);
        enteringDateCol.setCellFactory(cellFactory);
        customerNameCol.setCellFactory(cellFactory);
        phoneNoCol.setCellFactory(cellFactory);
        licensePlateNumberCol.setCellFactory(cellFactory);
        modelCol.setCellFactory(cellFactory);
        businessTypeCol.setCellFactory(cellFactory);
        senderCol.setCellFactory(cellFactory);
        receptionistCol.setCellFactory(cellFactory);
        
        discountAmountCol.setCellFactory(cellFactory);
        discountReasonCol.setCellFactory(cellFactory);
        receivableAmountCol.setCellFactory(cellFactory);
        actuallyPayCol.setCellFactory(cellFactory);
        amountReceivedCol.setCellFactory(cellFactory);
        owingAmountCol.setCellFactory(cellFactory);
        settlementDateCol.setCellFactory(cellFactory);
        paymentCol.setCellFactory(cellFactory);
        settlementNotesCol.setCellFactory(cellFactory);
        businessStateCol.setCellFactory(cellFactory);
        settlementStateCol.setCellFactory(cellFactory);
        
        descriptionCol.setCellFactory(cellFactory);
        recommendCol.setCellFactory(cellFactory);
        notesCol.setCellFactory(cellFactory);
        
        No.setCellValueFactory(new PropertyValueFactory<SettlementModel, String>("No"));
        enteringDateCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, LocalDate>("enteringDate"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, String>("customerName"));
        phoneNoCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, String>("phoneNo"));
        licensePlateNumberCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, String>("licensePlateNumber"));
        modelCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, String>("model"));
        businessTypeCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, String>("businessType"));
        senderCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, String>("sender"));
        receptionistCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, String>("receptionist"));
        
        discountAmountCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, BigDecimal>("discountAmount"));
        discountReasonCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, String>("discountReason"));
        receivableAmountCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, BigDecimal>("receivableAmount"));
        actuallyPayCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, BigDecimal>("actuallyPay"));
        amountReceivedCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, BigDecimal>("amountReceived"));
        owingAmountCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, BigDecimal>("owingAmount"));
        settlementDateCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, LocalDate>("settlementDate"));
        paymentCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, String>("payment"));
        settlementNotesCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, String>("settlementNotes"));
        businessStateCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, String>("businessState"));
        settlementStateCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, String>("settlementState"));
        
        descriptionCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, String>("description"));
        recommendCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, String>("recommend"));
        notesCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, String>("notes"));

        table.setItems(tableData);
        table.getColumns().addAll(invitedCol, No, enteringDateCol, customerNameCol, phoneNoCol, licensePlateNumberCol,  modelCol, businessTypeCol, senderCol, receptionistCol,
                discountAmountCol, discountReasonCol, receivableAmountCol, actuallyPayCol, amountReceivedCol, owingAmountCol, settlementDateCol, paymentCol, settlementNotesCol, businessStateCol, settlementStateCol, 
                descriptionCol, recommendCol, notesCol);
        table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                disable(false);
            }
        });

        // 表格双击
        table.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (t.getClickCount() == 2) {
                    if (tableData.size() > 0) {
                        updateSettlementDetails();
                    }
                }
            }
        });
        
        btnAddCar.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                if (checkFactoryInfo()) {
                    SettlementDetailsDialog dia = new SettlementDetailsDialog();
                    dia.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> add(dia));
                }
            }
        });
        
        btnUpdate.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                updateSettlementDetails();
            }
        });
        btnRemovingCar.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                
                Callback callback = new Callback() {
                    @Override
                    public Object call(Object param) {
                        List<String> deletable = new ArrayList<String>();
                        Iterator<SettlementModel> iter = tableData.iterator();
                        while (iter.hasNext()) {
                            SettlementModel m = iter.next();
                            if (m == totalModel) {
                                continue;
                            }
                            if (m.invitedProperty().getValue()) {
                                deletable.add(m.getId());
                                iter.remove();
                            }
                        }
                        
                        updateTotalAmount();
                        
                        if (tableData.size() == 0) {
                            disable(true);
                        }
                        
                        // 删除数据库数据
                        Iterator<SettlementDetails> iter2 = editSettlement.getDetails().iterator();
                        while (iter2.hasNext()) {
                            SettlementDetails d = iter2.next();
                            if (deletable.contains(d.getId())) {
                                iter2.remove();
                            }
                        }
                        controller.saveOrUpdateSettlement.call(editSettlement);
                        
                        return "";
                    }
                };
                int deleteRowNum = 0;
                for (SettlementModel m : tableData) {
                    if (m == totalModel) {
                        continue;
                    }
                    if (m.invitedProperty().getValue()) {
                        deleteRowNum ++;
                    }
                }
                if (deleteRowNum > 0) {
                    MessageBox.showMessage("请确认是否删除这 "+deleteRowNum+" 条记录。", Alert.AlertType.CONFIRMATION, callback);
                } else {
                    MessageBox.showMessage("请选中一行或多行删除。");
                }
                
            }
        });
        
        ToolBar bar = new ToolBar();
        bar.getItems().addAll(btnAddCar, new Separator(), btnUpdate, btnRemovingCar);

        pane.setTop(bar);
        pane.setCenter(table);
        
        VBox.setVgrow(pane, Priority.ALWAYS);
        
        return pane;
    }
    
    private void setFactoryFieldValues(Factory factory) {
        editFactory = factory;
        
        factoryField.setText(factory.getName());
        contactsField.setText(factory.getContacts());
        phoneNoField.setText(factory.getPhoneNo());
 
        setHistoricalValues();
    }
    
    private void setHistoricalValues() {
        // 历史消费记录查询
        BigDecimal historicalAmount = new BigDecimal(0);
        int num = 0;
        BigDecimal historicalOwing = new BigDecimal(0);
        List<Settlement> lstSettlement = (List<Settlement>) controller.listSettlementByFactoryId.call(editFactory.getId());
        for (Settlement s : lstSettlement) {
            List<SettlementDetails> lstDetails = s.getDetails();
            for (SettlementDetails d : lstDetails) {
                historicalAmount = historicalAmount.add(d.getActuallyPay());
                historicalOwing = historicalOwing.add(d.getOwingAmount());
            }
            num ++;
        }
        historicalAmountField.setText(historicalAmount.setScale(2, RoundingMode.HALF_UP).toString());
        historicalNumberField.setText(num + "");
        historicalOwingField.setText(historicalOwing.setScale(2, RoundingMode.HALF_UP).toString());
    }
    
    private boolean checkFactoryInfo() {
        if (factoryField.getText().isEmpty()) {
            MessageBox.showMessage("请选择或新建一个送修厂。");
            return false;
        }
        return true;
    }
    
    private void saveOrUpdateSettlement(boolean prompt) {
        String msg = "";
        if (editSettlement == null) {
            editSettlement = new Settlement();
            String SN = Generator.generateSN(Constants.WXKD);
            editSettlement.setSN(SN);
            msg = "新订单："+SN+"已生成并保存。";
        } else {
            msg = "订单："+editSettlement.getSN()+"已保存。";
        }
        editSettlement.setBillingObject(Constants.OBJ_2);
        editSettlement.setBillingDate(DateUtils.LocalDate2Date(billingDateField.getValue()));
        editSettlement.setClientId(editFactory.getId());
        editSettlement.setClientName(editFactory.getName());
        editSettlement.setContacts(editFactory.getContacts());
        editSettlement.setPhoneNo(editFactory.getPhoneNo());
        
        controller.saveOrUpdateSettlement.call(editSettlement);
        if (prompt) {
            MessageBox.showMessage(msg);
        }
        tab.setText(editSettlement.getSN());
    }
    
    private void updateTotalAmount() {
        BigDecimal discount = new BigDecimal(0);
        BigDecimal receivableAmount = new BigDecimal(0);
        BigDecimal actuallyPayAmount = new BigDecimal(0);
        BigDecimal amountReceived = new BigDecimal(0);
        BigDecimal owingAmount = new BigDecimal(0);
        
        int rowNo = 1;
        for (SettlementModel m : tableData) {
            if (m == totalModel) {
                m.setNo("合计：");
            } else {
                m.setNo((rowNo++) + "");
                discount = discount.add(m.getDiscountAmount());
                receivableAmount = receivableAmount.add(m.getReceivableAmount());
                actuallyPayAmount = actuallyPayAmount.add(m.getActuallyPay());
                amountReceived = amountReceived.add(m.getAmountReceived());
                owingAmount = owingAmount.add(m.getOwingAmount());
            }
        }
        discount.setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
        receivableAmount.setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
        actuallyPayAmount.setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
        amountReceived.setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
        owingAmount.setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
        
        totalModel.setDiscountAmount(discount);
        totalModel.setReceivableAmount(receivableAmount);
        totalModel.setActuallyPay(actuallyPayAmount);
        totalModel.setAmountReceived(amountReceived);
        totalModel.setOwingAmount(owingAmount);
    }
    
    private void disable(boolean b) {
        btnUpdate.setDisable(b);
        btnRemovingCar.setDisable(b);
    }
    
    private void updateSettlementDetails() {
        SettlementModel selectedModel = (SettlementModel) table.getSelectionModel().getSelectedItem();
        if (selectedModel != null && selectedModel != totalModel) {
            
            SettlementDetails details = (SettlementDetails) controller.getSettlementDetailsById.call(selectedModel.getId());
            SettlementDetailsDialog dia = new SettlementDetailsDialog(details);
            dia.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> dia.updateSettlementDetails(sd -> loadTableData()));
        }
    }
    
    public void edit(String id) {
        editSettlement = (Settlement) controller.getSettlementById.call(id);
        billingDateField.setValue(DateUtils.Date2LocalDate(editSettlement.getBillingDate()));
        
        Factory f = (Factory) controller.getFactoryById.call(editSettlement.getClientId());
        setFactoryFieldValues(f);
        
        loadTableData();
    }
    
    private void add(SettlementDetailsDialog dia) {
        saveOrUpdateSettlement(false);
        dia.setEditSettlement(editSettlement);
        dia.insertSettlementDetails(sd -> loadTableData());
    }
    
    private void loadTableData() {
        tableData.clear();
        editSettlement = (Settlement) controller.getSettlementById.call(editSettlement.getId());
        for (SettlementDetails sd : editSettlement.getDetails()) {
            SettlementModel m = new SettlementModel();
            BeanUtils.copyProperties(sd, m);
            
            m.setEnteringDate(DateUtils.Date2LocalDate(sd.getEnteringDate()));
            m.setNextMaintenanceDate(DateUtils.Date2LocalDate(sd.getNextMaintenanceDate()));
            m.setRegistrationDate(DateUtils.Date2LocalDate(sd.getRegistrationDate()));
            m.setSettlementDate(DateUtils.Date2LocalDate(sd.getSettlementDate()));
            
            tableData.add(m);
        }
        updateTotalAmount();
        table.refresh();
    }
    
    private Button btnEdit = new Button("", ImageUtils.createImageView("edit_16px_505013_easyicon.net.png"));
    private Button btnShowHistory = new Button("", ImageUtils.createImageView("publish_16px_505058_easyicon.net.png"));
    private TextField factoryField  = new TextField();
    private TextField contactsField  = new TextField();
    private TextField phoneNoField  = new TextField();
    private NumericTextField historicalAmountField  = new NumericTextField();
    private NumericTextField historicalNumberField  = new NumericTextField();
    private NumericTextField historicalOwingField  = new NumericTextField();
    private DatePicker billingDateField  = new DatePicker();
    private TableView table = new TableView();
    private final ObservableList<SettlementModel> tableData = FXCollections.observableArrayList();
    private SettlementModel totalModel = new SettlementModel();
    
    private Button btnFactoryChooser = new Button("选择送修厂");
    private Button btnFactoryCreator = new Button("新建送修厂");
    private Button btnSave = new Button("保存");
    private Button btnCancel = new Button("作废");
    private Button btnStatement = new Button("结算");
    private Button btnAddCar = new Button("添加车辆");
    private Button btnUpdate = new Button("更改");
    private Button btnRemovingCar = new Button("删除");
    private Tab tab;
    private FactoryBillingTabContentController controller = new FactoryBillingTabContentController();
    
    private Factory editFactory;
    private Settlement editSettlement;
}
