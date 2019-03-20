/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.ui;

import com.dockingsoftware.autorepairsystem.MainApp;
import com.dockingsoftware.autorepairsystem.common.Excel;
import com.dockingsoftware.autorepairsystem.component.BigDecimalCell;
import com.dockingsoftware.autorepairsystem.component.DatePickerCell;
import com.dockingsoftware.autorepairsystem.component.EditingCell;
import com.dockingsoftware.autorepairsystem.component.FloatCell;
import com.dockingsoftware.autorepairsystem.component.MessageBox;
import com.dockingsoftware.autorepairsystem.component.model.ItemModel;
import com.dockingsoftware.autorepairsystem.persistence.entity.Payment;
import com.dockingsoftware.autorepairsystem.ui.controller.ItemSalesReportTabContentController;
import com.dockingsoftware.autorepairsystem.util.DateUtils;
import com.dockingsoftware.autorepairsystem.util.ImageUtils;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
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
import javafx.stage.FileChooser;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;

public class ItemSalesReportTabContent extends VBox {

    public ItemSalesReportTabContent() {
        initComponents();
    }
    
    private void initComponents() {
        BorderPane tablePane = createTablePane();
        this.getChildren().addAll(createToolBar(), createSearchPane(), tablePane);
        VBox.setVgrow(tablePane, Priority.ALWAYS);
    }
    
    private ToolBar createToolBar() {
        
        btnExport.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                export();
            }
        });

        ToolBar bar = new ToolBar();
        bar.getItems().addAll(btnExport);

        return bar;
    }
    
    private GridPane createSearchPane() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 20, 5, 10));
        
        grid.add(new Label("单号:"), 0, 0);
        SNField.setPromptText("单号");
        grid.add(SNField, 1, 0);
        
        grid.add(new Label("开单日期:"), 2, 0);
        billFromDateField.setEditable(true);
        grid.add(billFromDateField, 3, 0);
        
        grid.add(new Label("到:"), 4, 0);
        billToDateField.setEditable(true);
        grid.add(billToDateField, 5, 0);

        grid.add(new Label("结算方式:"), 0, 1);
        paymentField.getItems().add("");
        List<Payment> lstPayment = (List<Payment>) controller.listPayment.call("");
        for (Payment p : lstPayment) {
            paymentField.getItems().add(p.getName());
        }
        paymentField.getSelectionModel().select(0);
        paymentField.setPrefWidth(173);
        grid.add(paymentField, 1, 1);
        
        grid.add(new Label("结算日期:"), 2, 1);
        settleFromDateField.setEditable(true);
        grid.add(settleFromDateField, 3, 1);
        
        grid.add(new Label("到:"), 4, 1);
        settleToDateField.setEditable(true);
        grid.add(settleToDateField, 5, 1);
        
        grid.add(new Label("客户名:"), 0, 2);
        customerNameField.setPromptText("客户名");
        grid.add(customerNameField, 1, 2);
        
        grid.add(new Label("车牌号:"), 2, 2);
        licensePlateNumberField.setPromptText("车牌号");
        grid.add(licensePlateNumberField, 3, 2);
        
        grid.add(new Label("商品名:"), 4, 2);
        itemNameField.setPromptText("商品名");
        grid.add(itemNameField, 5, 2);
        
        HBox h = new HBox();
        h.setSpacing(5);
        h.setAlignment(Pos.CENTER);
        h.getChildren().addAll(btnQuery, btnBackAll);
        grid.add(h, 6, 2);
        
        btnBackAll.setPrefWidth(60);
        btnQuery.setGraphic(ImageUtils.createImageView("job_search_16px_1192818_easyicon.net.png"));
        btnQuery.setStyle("-fx-font: 12 arial; -fx-base: rgb(0, 150, 201);");

        btnQuery.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                String snVal = SNField.getText();
                String billFromDateVal = billFromDateField.getEditor().getText();
                String billToDateVal = billToDateField.getEditor().getText();
                String paymentVal = paymentField.getValue();
                String settleFromDateVal = settleFromDateField.getEditor().getText();
                String settleToDateVal = settleToDateField.getEditor().getText();
                String customerNameVal = customerNameField.getText();
                String licensePlateNumberVal = licensePlateNumberField.getText();
                String itemNameVal = itemNameField.getText();
                
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("SN", snVal);
                try {
                    SimpleDateFormat f = new SimpleDateFormat("M/d/yyyy");
                    param.put("billFromDate",  DateUtils.String2Date(billFromDateVal, f));
                    param.put("billToDate",  DateUtils.String2Date(billToDateVal, f));
                    param.put("settleFromDate",  DateUtils.String2Date(settleFromDateVal, f));
                    param.put("settleToDate",  DateUtils.String2Date(settleToDateVal, f));
                } catch (ParseException ex) {
                    Logger.getLogger(ItemSalesReportTabContent.class.getName()).log(Level.SEVERE, null, ex);
                    MessageBox.showMessage("日期格式不正确。", Alert.AlertType.WARNING);
                    return;
                }
                param.put("payment", paymentVal);
                param.put("customerName", customerNameVal);
                param.put("licensePlateNumber", licensePlateNumberVal);
                param.put("itemName", itemNameVal);
                
                loadTableData(param);
            }
        });

        btnBackAll.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                loadTableData(null);
            }
        });

        return grid;
    }
    
    private BorderPane createTablePane() {
        BorderPane pane = new BorderPane();
        pane.setPadding(Insets.EMPTY);
        
        Callback<TableColumn, TableCell> cellFactory = new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn p) {
                if ("salePriceCol".equals(p.getId())
                        || "discountAmountCol".equals(p.getId())
                        || "amountCol".equals(p.getId())
                        || "costPriceCol".equals(p.getId())
                        || "profitCol".equals(p.getId())) {
                    return new BigDecimalCell();
                }
                else if ("settlementDateCol".equals(p.getId())
                        || "billingDateCol".equals(p.getId())) {
                    return new DatePickerCell();
                } 
                else if ("quantityCol".equals(p.getId())) {
                    return new FloatCell();
                } 
                else {
                    return new EditingCell();
                }
            }
        };
        
        TableColumn NoCol = new TableColumn("序号");
        TableColumn SNCol = new TableColumn("单号");
        TableColumn billingDateCol = new TableColumn("开单日期");
        TableColumn itemNameCol = new TableColumn("商品名称");
        TableColumn salePriceCol = new TableColumn("销售单价");
        TableColumn quantityCol = new TableColumn("数量");
        TableColumn discountAmountCol = new TableColumn("优惠金额");
        TableColumn amountCol = new TableColumn("金额");
        TableColumn itemNotesCol = new TableColumn("商品备注");
        TableColumn costPriceCol = new TableColumn("成本价");
        TableColumn profitCol = new TableColumn("利润");
        TableColumn nameCol = new TableColumn("客户名称");
        TableColumn licensePlateNumberCol = new TableColumn("号码号牌");
        TableColumn paymentCol = new TableColumn("结算方式");
        TableColumn settlementDateCol = new TableColumn("结算日期");
        TableColumn settlementNotesCol = new TableColumn("结算备注");
        TableColumn settlementStateCol = new TableColumn("结算状态");

        // 渲染列ID
        NoCol.setId("NoCol");
        SNCol.setId("SNCol");
        billingDateCol.setId("billingDateCol");
        itemNameCol.setId("itemNameCol");
        salePriceCol.setId("salePriceCol");
        quantityCol.setId("quantityCol");
        discountAmountCol.setId("discountAmountCol");
        amountCol.setId("amountCol");
        itemNotesCol.setId("itemNotesCol");
        costPriceCol.setId("costPriceCol");
        profitCol.setId("profitCol");
        nameCol.setId("nameCol");
        licensePlateNumberCol.setId("licensePlateNumberCol");
        paymentCol.setId("paymentCol");
        settlementDateCol.setId("settlementDateCol");
        settlementNotesCol.setId("settlementNotesCol");
        settlementStateCol.setId("settlementStateCol");
      
        NoCol.setCellFactory(cellFactory);
        SNCol.setCellFactory(cellFactory);
        billingDateCol.setCellFactory(cellFactory);
        itemNameCol.setCellFactory(cellFactory);
        salePriceCol.setCellFactory(cellFactory);
        quantityCol.setCellFactory(cellFactory);
        discountAmountCol.setCellFactory(cellFactory);
        amountCol.setCellFactory(cellFactory);
        itemNotesCol.setCellFactory(cellFactory);
        costPriceCol.setCellFactory(cellFactory);
        profitCol.setCellFactory(cellFactory);
        nameCol.setCellFactory(cellFactory);
        licensePlateNumberCol.setCellFactory(cellFactory);
        paymentCol.setCellFactory(cellFactory);
        settlementDateCol.setCellFactory(cellFactory);
        settlementNotesCol.setCellFactory(cellFactory);
        settlementStateCol.setCellFactory(cellFactory);

        // 设置列宽
        NoCol.setPrefWidth(50);
        SNCol.setPrefWidth(120);
        billingDateCol.setPrefWidth(120);
        itemNameCol.setPrefWidth(120);
        salePriceCol.setPrefWidth(120);
        quantityCol.setPrefWidth(120);
        discountAmountCol.setPrefWidth(120);
        amountCol.setPrefWidth(120);
        itemNotesCol.setPrefWidth(120);
        costPriceCol.setPrefWidth(120);
        profitCol.setPrefWidth(120);
        nameCol.setPrefWidth(120);
        licensePlateNumberCol.setPrefWidth(120);
        paymentCol.setPrefWidth(120);
        settlementDateCol.setPrefWidth(120);
        settlementNotesCol.setPrefWidth(120);
        settlementStateCol.setPrefWidth(120);

        NoCol.setCellValueFactory(new PropertyValueFactory<ItemModel, String>("No"));
        SNCol.setCellValueFactory(new PropertyValueFactory<ItemModel, String>("SN"));
        billingDateCol.setCellValueFactory(new PropertyValueFactory<ItemModel, String>("billingDate"));
        itemNameCol.setCellValueFactory(new PropertyValueFactory<ItemModel, String>("name"));
        salePriceCol.setCellValueFactory(new PropertyValueFactory<ItemModel, String>("salesPrice"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<ItemModel, String>("quantity"));
        discountAmountCol.setCellValueFactory(new PropertyValueFactory<ItemModel, String>("discount"));
        amountCol.setCellValueFactory(new PropertyValueFactory<ItemModel, String>("amount"));
        itemNotesCol.setCellValueFactory(new PropertyValueFactory<ItemModel, String>("notes"));
        costPriceCol.setCellValueFactory(new PropertyValueFactory<ItemModel, String>("costPrice"));
        profitCol.setCellValueFactory(new PropertyValueFactory<ItemModel, String>("profit"));
        nameCol.setCellValueFactory(new PropertyValueFactory<ItemModel, String>("customerName"));
        licensePlateNumberCol.setCellValueFactory(new PropertyValueFactory<ItemModel, String>("licensePlateNumber"));
        paymentCol.setCellValueFactory(new PropertyValueFactory<ItemModel, String>("payment"));
        settlementDateCol.setCellValueFactory(new PropertyValueFactory<ItemModel, String>("settlementDate"));
        settlementNotesCol.setCellValueFactory(new PropertyValueFactory<ItemModel, String>("settlementNotes"));
        settlementStateCol.setCellValueFactory(new PropertyValueFactory<ItemModel, String>("settlementState"));

        table.setItems(data);
        table.getColumns().addAll(NoCol, SNCol, billingDateCol, itemNameCol, salePriceCol, quantityCol, discountAmountCol, amountCol, itemNotesCol, costPriceCol, profitCol, nameCol, licensePlateNumberCol, paymentCol, settlementDateCol, settlementNotesCol, settlementStateCol); 
        table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
//                disable(false);
            }
        });

        // 设置表格右键菜单
        ContextMenu menu = new ContextMenu();
        menu.getItems().addAll(itemExport);
        itemExport.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                export();
            }
        });
        
        table.setContextMenu(menu);

        loadTableData(null);
        
        pane.setCenter(table);
        return pane;
    }
    
    private void loadTableData(Map<String, Object> param) {
        
        data.clear();
        
        List<Object[]> results = (List<Object[]>) controller.listItemDetails.call(param);
        
        // 数量合计
        float totalQuantity = 0;
        // 金额合计
        BigDecimal totalAmount = new BigDecimal(0);
        // 利润合计
        BigDecimal totalProfit = new BigDecimal(0);
        // 优惠合计
        BigDecimal totalDiscount = new BigDecimal(0);
        // 销售合计
        BigDecimal totalSalesPrice= new BigDecimal(0);
        // 成本
        BigDecimal totalCostPrice= new BigDecimal(0);
        
        int rowNum = 1;
        for (Object[] item : results) {
            ItemModel itemModel = new ItemModel();
            
            itemModel.setNo((rowNum ++ ) + "");
            itemModel.setSN(item[0].toString());
            itemModel.setBillingDate(DateUtils.Date2LocalDate((Date) item[1]));
            itemModel.setName(item[2].toString());
            itemModel.setSalesPrice(new BigDecimal(item[3].toString()));
            itemModel.setQuantity(Float.parseFloat(item[4].toString()));
            itemModel.setDiscount(new BigDecimal(item[5].toString()));
            itemModel.setAmount(new BigDecimal(item[6].toString()));
            itemModel.setNotes(item[7].toString());
            itemModel.setCostPrice(new BigDecimal(item[8].toString()));
            itemModel.setProfit(new BigDecimal(item[9].toString()));
            itemModel.setCustomerName(item[10].toString());
            itemModel.setLicensePlateNumber(item[11].toString());
            
            itemModel.setPayment(item[12] == null ? "" : item[12].toString());
            itemModel.setSettlementDate(item[13] == null ? null : DateUtils.Date2LocalDate((Date) item[13]));
            itemModel.setSettlementNotes(item[14] == null ? "" : item[14].toString());
            itemModel.setSettlementState(item[15] == null ? "" : item[15].toString());
            
            data.add(itemModel);
            
            totalQuantity += itemModel.getQuantity();
            totalAmount = totalAmount.add(itemModel.getAmount());
            totalProfit = totalProfit.add(itemModel.getProfit());
            totalDiscount = totalDiscount.add(itemModel.getDiscount());
            totalSalesPrice = totalSalesPrice.add(itemModel.getSalesPrice());
            totalCostPrice = totalCostPrice.add(itemModel.getCostPrice());
        }
        
        totalModel.setNo("合计");
        totalModel.setAmount(totalAmount);
        totalModel.setProfit(totalProfit);
        totalModel.setQuantity(totalQuantity);
        totalModel.setDiscount(totalDiscount);
        totalModel.setSalesPrice(totalSalesPrice);
        totalModel.setCostPrice(totalCostPrice);
        data.add(totalModel);
    }
    
    private void export() {
        FileChooser fc = new FileChooser();
        fc.setInitialFileName(DateUtils.Date2String(new Date())+".xls");
        File selectedFile = fc.showSaveDialog(MainApp.getInstance().getPrimaryStage());
        if (selectedFile != null) {
            try {
                Excel.getInstance().exportItemSalesReport(table, selectedFile.getAbsolutePath());
            } catch (IOException ex) {
                org.apache.logging.log4j.Logger logger = LogManager.getLogger(ItemSalesReportTabContent.class.getName());
                logger.error(ex);
                MessageBox.showMessage("由于文件被占用无法导出。");
            }
        }
    }
    
    private TextField SNField = new TextField();
    private TextField customerNameField = new TextField();
    private TextField licensePlateNumberField = new TextField();
    private TextField itemNameField = new TextField();
    
    private ComboBox<String> paymentField = new ComboBox<String>(); 
    private DatePicker billFromDateField = new DatePicker();
    private DatePicker billToDateField = new DatePicker();
    private DatePicker settleFromDateField = new DatePicker();
    private DatePicker settleToDateField = new DatePicker();
    private Button btnExport = new Button("导出", ImageUtils.createImageView("future_projects_16px_505023_easyicon.net.png"));
    private MenuItem itemExport = new MenuItem("导出", ImageUtils.createImageView("future_projects_16px_505023_easyicon.net.png"));
    private Button btnQuery = new Button("查询");
    private Button btnBackAll = new Button("返回");
    private TableView table = new TableView();
    private final ObservableList<ItemModel> data = FXCollections.observableArrayList();
    private ItemSalesReportTabContentController controller = new ItemSalesReportTabContentController();
    private ItemModel totalModel = new ItemModel();
}
