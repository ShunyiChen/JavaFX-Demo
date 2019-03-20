/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dockingsoftware.autorepairsystem.ui;

import com.dockingsoftware.autorepairsystem.MainApp;
import com.dockingsoftware.autorepairsystem.component.BigDecimalCell;
import com.dockingsoftware.autorepairsystem.component.EditingCell;
import com.dockingsoftware.autorepairsystem.component.StringConverterExt;
import com.dockingsoftware.autorepairsystem.component.model.SettlementModel;
import com.dockingsoftware.autorepairsystem.persistence.entity.Payment;
import com.dockingsoftware.autorepairsystem.persistence.entity.SettlementDetails;
import com.dockingsoftware.autorepairsystem.ui.controller.SettlementDialogController;
import com.dockingsoftware.autorepairsystem.util.DateUtils;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.springframework.beans.BeanUtils;

public class SettlementDialog extends Dialog {
    
    /**
     * 结算单的结算
     * 
     * @param lstDetails
     */
    public SettlementDialog(List<SettlementDetails> lstDetails) {
        this.lstDetails = lstDetails;
        initComponents();
    }

    private void initComponents() {
        initOwner(MainApp.getInstance().getPrimaryStage());
        
        setTitle("结算");
        setHeaderText("请核对金额再结算。");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        
        grid.add(fullAmount, 0, 0);
        
        grid.add(new Label("结算日期:"), 0, 1);
        settlementDate.setEditable(false);
        settlementDate.setValue(LocalDate.now());
        grid.add(settlementDate, 1, 1);
        
        grid.add(new Label("结算方式:"), 2, 1);
        List<Payment> lstPayment = (List<Payment>) controller.listPayment.call("");
        for (Payment p : lstPayment) {
            payment.getItems().add(p.getName());
        }
        payment.getSelectionModel().select(0);
        payment.setPrefWidth(173);
        grid.add(payment, 3, 1);
        
        // 设置表格内日期格式
        settlementDate.setConverter(new StringConverterExt());
        
        fullAmount.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                if (fullAmount.isSelected()) {
                    for (SettlementModel m : data) {
                        if (m != totalModel) {
                            m.setActuallyPay(m.getReceivableAmount());
                            m.setAmountReceived(m.getActuallyPay());
                            m.setOwingAmount(m.getActuallyPay().subtract(m.getAmountReceived()).setScale(2, RoundingMode.HALF_UP));
                        }
                    }
                    updateAmount();
                } else {
                    
                    loadTableData();
                }
            }
        });
        
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
        okButton.setText("确定");
        Button cancelButton = (Button) getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelButton.setText("取消");
 
        VBox vbox = new VBox();
        BorderPane tablePane = tablePane();
        grid.setPadding(new Insets(8, 0, 0, 0));
        vbox.getChildren().addAll(tablePane, grid);
        getDialogPane().setContent(vbox);
    }

    private BorderPane tablePane() {
        BorderPane pane = new BorderPane();
        pane.setPadding(Insets.EMPTY);

        loadTableData();

        Callback<TableColumn, TableCell> cellFactory = new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn p) {
                if ("receivableAmountCol".equals(p.getId())
                        || "actuallyPayCol".equals(p.getId())
                        || "amountReceivedCol".equals(p.getId())
                        || "owingAmountCol".equals(p.getId())
                        || "discountAmountCol".equals(p.getId())) {
                    return new BigDecimalCell();
                } 
                else {
                    return new EditingCell();
                }
            }
        };
        
        table.setEditable(true);
        
        TableColumn No = new TableColumn("序号");
        TableColumn licensePlateNumberCol = new TableColumn("车牌号");
        TableColumn nameCol = new TableColumn("客户名称");
        TableColumn discountAmountCol = new TableColumn("优惠金额");
        TableColumn discountReasonCol = new TableColumn("优惠原因*");
        TableColumn receivableAmountCol = new TableColumn("应收金额");
        TableColumn actuallyPayCol = new TableColumn("实际支付*");
        TableColumn amountReceivedCol = new TableColumn("已收金额*");
        TableColumn owingAmountCol = new TableColumn("尚欠金额");
        TableColumn settlementNotesCol = new TableColumn("结算备注*");
        
        No.setEditable(false);
        licensePlateNumberCol.setEditable(false);
        nameCol.setEditable(false);
        receivableAmountCol.setEditable(false);
        discountAmountCol.setEditable(false);
        discountReasonCol.setEditable(true);
        actuallyPayCol.setEditable(true);
        owingAmountCol.setEditable(false);
        
        // 设置不同的ID,渲染不同的单元格。
        discountAmountCol.setId("discountAmountCol");
        receivableAmountCol.setId("receivableAmountCol");
        actuallyPayCol.setId("actuallyPayCol");
        amountReceivedCol.setId("amountReceivedCol");
        owingAmountCol.setId("owingAmountCol");
        
        // 设置列宽
        No.setPrefWidth(50);
        licensePlateNumberCol.setPrefWidth(120);
        nameCol.setPrefWidth(120);
        discountAmountCol.setPrefWidth(120);
        discountReasonCol.setPrefWidth(120);
        receivableAmountCol.setPrefWidth(120);
        actuallyPayCol.setPrefWidth(120);
        amountReceivedCol.setPrefWidth(120);
        owingAmountCol.setPrefWidth(120);
        settlementNotesCol.setPrefWidth(120);
        
        // 设置可编辑列
        No.setCellFactory(cellFactory);
        licensePlateNumberCol.setCellFactory(cellFactory);
        nameCol.setCellFactory(cellFactory);
        discountAmountCol.setCellFactory(cellFactory);
        discountReasonCol.setCellFactory(cellFactory);
        receivableAmountCol.setCellFactory(cellFactory);
        actuallyPayCol.setCellFactory(cellFactory);
        amountReceivedCol.setCellFactory(cellFactory);
        owingAmountCol.setCellFactory(cellFactory);
        settlementNotesCol.setCellFactory(cellFactory);
        
        No.setCellValueFactory(new PropertyValueFactory<SettlementModel, String>("No"));
        licensePlateNumberCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, String>("licensePlateNumber"));
        nameCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, String>("customerName"));
        discountAmountCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, BigDecimal>("discountAmount"));
        discountReasonCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, String>("discountReason"));
        receivableAmountCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, BigDecimal>("receivableAmount"));
        actuallyPayCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, BigDecimal>("actuallyPay"));
        amountReceivedCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, BigDecimal>("amountReceived"));
        owingAmountCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, BigDecimal>("owingAmount"));
        settlementNotesCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, String>("settlementNotes"));

        table.setItems(data);
        table.getColumns().addAll(No, licensePlateNumberCol, nameCol, discountAmountCol, discountReasonCol, receivableAmountCol, actuallyPayCol, amountReceivedCol, owingAmountCol, settlementNotesCol);
        table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        pane.setCenter(table);

        discountReasonCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<SettlementModel, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<SettlementModel, String> t) {
                SettlementModel m =  t.getTableView().getItems().get(t.getTablePosition().getRow());
                m.setDiscountReason(t.getNewValue());
            }
        });
        
        actuallyPayCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<SettlementModel, BigDecimal>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<SettlementModel, BigDecimal> t) {
                SettlementModel m =  t.getTableView().getItems().get(t.getTablePosition().getRow());
                m.setActuallyPay(t.getNewValue());
                m.setDiscountAmount(m.getReceivableAmount().subtract(m.getActuallyPay()).setScale(2, RoundingMode.HALF_UP));
                m.setOwingAmount(m.getActuallyPay().subtract(m.getAmountReceived()).setScale(2, RoundingMode.HALF_UP));
                
                updateAmount();
            }
        });
        
        amountReceivedCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<SettlementModel, BigDecimal>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<SettlementModel, BigDecimal> t) {
                SettlementModel m =  t.getTableView().getItems().get(t.getTablePosition().getRow());
                m.setAmountReceived(t.getNewValue());
                m.setOwingAmount(m.getActuallyPay().subtract(m.getAmountReceived()).setScale(2, RoundingMode.HALF_UP));
                updateAmount();
            }
        });
        
        settlementNotesCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<SettlementModel, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<SettlementModel, String> t) {
                SettlementModel m =  t.getTableView().getItems().get(t.getTablePosition().getRow());
                m.setSettlementNotes(t.getNewValue());
            }
        });
        
        return pane;
    }
    
    public void pay(Callback cb) {
        for (SettlementDetails sd : lstDetails) {
            for (SettlementModel m : data) {
                if (m != totalModel && m.getId().equals(sd.getId())) {
                    sd.setDiscountAmount(m.getDiscountAmount());
                    sd.setDiscountReason(m.getDiscountReason());
                    sd.setReceivableAmount(m.getReceivableAmount());
                    sd.setActuallyPay(m.getActuallyPay());
                    sd.setAmountReceived(m.getAmountReceived());
                    sd.setOwingAmount(m.getOwingAmount());
                    sd.setSettlementDate(DateUtils.LocalDate2Date(settlementDate.getValue()));
                    sd.setPayment(payment.getValue());
                    sd.setSettlementNotes(m.getSettlementNotes());
                    
                    if (m.getOwingAmount().intValue() > 0) {
                        sd.setSettlementState("结算中");
                    } else {
                        sd.setSettlementState("已结算");
                    }
                    
                    controller.saveOrUpdateSettlementDetails.call(sd);
                    
                    break;
                }                                            
            }
        }
        if (cb != null) {
            cb.call(lstDetails);
        }
    }
    
    private void loadTableData() {
        data.clear();
        int num = 1;
        BigDecimal totalDiscountAmount = new BigDecimal(0);
        BigDecimal totalReceivableAmount = new BigDecimal(0);
        BigDecimal actuallyPayAmount = new BigDecimal(0);
        BigDecimal totalAmountReceived = new BigDecimal(0);
        BigDecimal totalOwingAmount = new BigDecimal(0);
        for (SettlementDetails sd : lstDetails) {
            SettlementModel m = new SettlementModel();
            BeanUtils.copyProperties(sd, m);
            m.setNo((num++) +"");
            
            totalDiscountAmount = totalDiscountAmount.add(m.getDiscountAmount() == null ? new BigDecimal(0) : m.getDiscountAmount());
            totalReceivableAmount = totalReceivableAmount.add(m.getReceivableAmount() == null ? new BigDecimal(0) : m.getReceivableAmount());
            actuallyPayAmount = actuallyPayAmount.add(m.getActuallyPay() == null ? new BigDecimal(0) : m.getActuallyPay());
            totalAmountReceived = totalAmountReceived.add(m.getAmountReceived() == null ? new BigDecimal(0) : m.getAmountReceived());
            totalOwingAmount = totalOwingAmount.add(m.getOwingAmount() == null ? new BigDecimal(0) : m.getOwingAmount());
            
            data.add(m);
        }
        
        totalModel.setNo("合计：");
        totalModel.setDiscountAmount(totalDiscountAmount);
        totalModel.setReceivableAmount(totalReceivableAmount);
        totalModel.setActuallyPay(actuallyPayAmount);
        totalModel.setAmountReceived(totalAmountReceived);
        totalModel.setOwingAmount(totalOwingAmount);
        
        data.add(totalModel);
    }
    
    private void updateAmount() {
        BigDecimal totalDiscountAmount = new BigDecimal(0);
        BigDecimal totalReceivableAmount = new BigDecimal(0);
        BigDecimal actuallyPayAmount = new BigDecimal(0);
        BigDecimal totalAmountReceived = new BigDecimal(0);
        BigDecimal totalOwingAmount = new BigDecimal(0);
        int rowNum = 1;
        for (SettlementModel m : data) {
            if (m != totalModel) {
                m.setNo((rowNum ++) + "");
                
                totalDiscountAmount = totalDiscountAmount.add(m.getDiscountAmount() == null ? new BigDecimal(0) : m.getDiscountAmount());
                totalReceivableAmount = totalReceivableAmount.add(m.getReceivableAmount() == null ? new BigDecimal(0) : m.getReceivableAmount());
                actuallyPayAmount = actuallyPayAmount.add(m.getActuallyPay() == null ? new BigDecimal(0) : m.getActuallyPay());
                totalAmountReceived = totalAmountReceived.add(m.getAmountReceived() == null ? new BigDecimal(0) : m.getAmountReceived());
                totalOwingAmount = totalOwingAmount.add(m.getOwingAmount() == null ? new BigDecimal(0) : m.getOwingAmount());
            }
        }
        totalModel.setNo("合计：");
        totalModel.setDiscountAmount(totalDiscountAmount);
        totalModel.setReceivableAmount(totalReceivableAmount);
        totalModel.setActuallyPay(actuallyPayAmount);
        totalModel.setAmountReceived(totalAmountReceived);
        totalModel.setOwingAmount(totalOwingAmount);
        
        table.refresh();
    }
    
    private CheckBox fullAmount = new CheckBox("是否收全款");
    private DatePicker settlementDate = new DatePicker();
    private ComboBox<String> payment = new ComboBox<String>();
    private TableView table = new TableView();
    private final ObservableList<SettlementModel> data = FXCollections.observableArrayList();
    private SettlementModel totalModel = new SettlementModel();

    private SettlementDialogController controller = new SettlementDialogController();
    private List<SettlementDetails> lstDetails;
}
