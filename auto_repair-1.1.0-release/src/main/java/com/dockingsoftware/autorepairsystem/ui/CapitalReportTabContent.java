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
import com.dockingsoftware.autorepairsystem.component.MessageBox;
import com.dockingsoftware.autorepairsystem.component.model.SettlementModel;
import com.dockingsoftware.autorepairsystem.persistence.entity.ItemDetails;
import com.dockingsoftware.autorepairsystem.persistence.entity.ProjectDetails;
import com.dockingsoftware.autorepairsystem.persistence.entity.SettlementDetails;
import com.dockingsoftware.autorepairsystem.ui.controller.CapitalReportTabContentController;
import com.dockingsoftware.autorepairsystem.util.DateUtils;
import com.dockingsoftware.autorepairsystem.util.ImageUtils;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;

/**
 * 消费收款明细表
 * 
 * @author Shunyi Chen
 */
public class CapitalReportTabContent extends VBox {

    public CapitalReportTabContent() {
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
        
        grid.add(new Label("结算日期:"), 2, 0);
        settleFromDateField.setEditable(true);
        grid.add(settleFromDateField, 3, 0);
        
        grid.add(new Label("到:"), 4, 0);
        settleToDateField.setEditable(true);
        grid.add(settleToDateField, 5, 0);

        grid.add(new Label("结算方式:"), 0, 1);
        paymentField.setPromptText("结算方式");
        grid.add(paymentField, 1, 1);
        
        grid.add(new Label("客户名:"), 2, 1);
        customerNameField.setPromptText("客户名");
        grid.add(customerNameField, 3, 1);
        
        grid.add(new Label("单据类型:"), 4, 1);
        billTypeField.getItems().add("开单");
        billTypeField.getSelectionModel().select(0);
        billTypeField.setPrefWidth(173);
        grid.add(billTypeField, 5, 1);
        
        HBox h = new HBox();
        h.setSpacing(5);
        h.setAlignment(Pos.CENTER);
        h.getChildren().addAll(btnQuery, btnBackAll);
        grid.add(h, 6, 1);
        
        btnBackAll.setPrefWidth(60);
        btnQuery.setGraphic(ImageUtils.createImageView("job_search_16px_1192818_easyicon.net.png"));
        btnQuery.setStyle("-fx-font: 12 arial; -fx-base: rgb(0, 150, 201);");

        btnQuery.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                try {
                    SimpleDateFormat f = new SimpleDateFormat("M/d/yyyy");
                    String snVal = SNField.getText();
                    Date settleFromDateVal = DateUtils.String2Date(settleFromDateField.getEditor().getText(), f);
                    Date settleToDateVal = DateUtils.String2Date(settleToDateField.getEditor().getText(), f);
                    String paymentVal = paymentField.getText();
                    String customerNameVal = customerNameField.getText();

                    Criterion c1 = Restrictions.ne("id", "0");
                    if (!snVal.isEmpty()) {
                        c1 = Restrictions.ilike("SN", snVal, MatchMode.ANYWHERE);
                    }
                    Criterion c2 = Restrictions.ne("id", "0");
                    if (!paymentVal.isEmpty()) {
                        c2 = Restrictions.ilike("payment", paymentVal, MatchMode.ANYWHERE);
                    }
                    Criterion c4 = Restrictions.ne("id", "0");
                    if (!customerNameVal.isEmpty()) {
                        c4 = Restrictions.ilike("customerName", customerNameVal, MatchMode.ANYWHERE);
                    }
                    
                    Criterion c8 = Restrictions.ne("id", "0");
                    if (settleFromDateVal != null && settleToDateVal != null) {
                        c8 = Restrictions.between("settlementDate", settleFromDateVal, settleToDateVal);
                    }
                    
                    Criterion c = Restrictions.and(c1, c2, c4, c8);
                    
                    loadTableData(c);
                
                } catch (ParseException ex) {
                    Logger.getLogger(ItemSalesReportTabContent.class.getName()).log(Level.SEVERE, null, ex);
                    MessageBox.showMessage("日期格式不正确。", Alert.AlertType.WARNING);
                    return;
                }
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
                if ("amountCol".equals(p.getId()) ) {
                    return new BigDecimalCell();
                }
                else if ("settlementDateCol".equals(p.getId())
                        || "billingDateCol".equals(p.getId())) {
                    return new DatePickerCell();
                } 
                else {
                    return new EditingCell();
                }
            }
        };
        
        TableColumn NoCol = new TableColumn("序号");
        TableColumn SNCol = new TableColumn("单号");
        TableColumn amountCol = new TableColumn("结算金额");
        TableColumn paymentCol = new TableColumn("结算方式");
        TableColumn settlementDateCol = new TableColumn("结算日期");
        TableColumn customerNameCol = new TableColumn("客户名称");
        TableColumn licensePlateNumberCol = new TableColumn("车牌号");
        TableColumn phoneNoCol = new TableColumn("联系手机");
        TableColumn billingDateCol = new TableColumn("开单日期");
        TableColumn settlementNotesCol = new TableColumn("结算备注");

        // 渲染列ID
        NoCol.setId("NoCol");
        SNCol.setId("SNCol");
        amountCol.setId("amountCol");
        licensePlateNumberCol.setId("licensePlateNumberCol");
        paymentCol.setId("paymentCol");
        billingDateCol.setId("billingDateCol");
        settlementDateCol.setId("settlementDateCol");
        customerNameCol.setId("customerNameCol");
        phoneNoCol.setId("phoneNoCol");
        settlementNotesCol.setId("settlementNotesCol");
      
        NoCol.setCellFactory(cellFactory);
        SNCol.setCellFactory(cellFactory);
        amountCol.setCellFactory(cellFactory);
        paymentCol.setCellFactory(cellFactory);
        settlementDateCol.setCellFactory(cellFactory);
        customerNameCol.setCellFactory(cellFactory);
        licensePlateNumberCol.setCellFactory(cellFactory);
        phoneNoCol.setCellFactory(cellFactory);
        billingDateCol.setCellFactory(cellFactory);
        settlementNotesCol.setCellFactory(cellFactory);

        // 设置列宽
        NoCol.setPrefWidth(50);
        SNCol.setPrefWidth(120);
        amountCol.setPrefWidth(120);
        paymentCol.setPrefWidth(120);
        settlementDateCol.setPrefWidth(120);
        customerNameCol.setPrefWidth(120);
        licensePlateNumberCol.setPrefWidth(120);
        phoneNoCol.setPrefWidth(120);
        billingDateCol.setPrefWidth(120);
        settlementNotesCol.setPrefWidth(120);

        NoCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, String>("No"));
        SNCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, String>("SN"));
        amountCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, String>("amountReceived"));
        paymentCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, String>("payment"));
        settlementDateCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, String>("settlementDate"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, String>("customerName"));
        licensePlateNumberCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, String>("licensePlateNumber"));
        phoneNoCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, String>("phoneNo"));
        billingDateCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, String>("billingDate"));
        settlementNotesCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, String>("settlementNotes"));
        
        table.setItems(data);
        table.getColumns().addAll(NoCol, SNCol, amountCol, paymentCol, settlementDateCol, customerNameCol, licensePlateNumberCol, phoneNoCol, billingDateCol, settlementNotesCol); 
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
    
    private void loadTableData(Criterion c) {
        
        data.clear();
        
        if (c == null) {
            // 默认查询当月的所有记录
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE,0);
            cal.set(Calendar.SECOND,0);
            cal.set(Calendar.MILLISECOND,0);
            
            Criterion c1 = Restrictions.or(Restrictions.eq("settlementState", "结算中"), Restrictions.eq("settlementState", "已结算"));
            Criterion c2 = Restrictions.between("settlementDate", cal.getTime(), new Date());
            c = Restrictions.and(c1, c2);
        }
        
        List<SettlementDetails> results = (List<SettlementDetails>) controller.listSettlementDetails.call(c);
   
        // 已收合计
        BigDecimal totalAmountReceived = new BigDecimal(0);
        
        int rowNum = 1;
        for (SettlementDetails sd : results) {
            
            SettlementModel m = new SettlementModel();
            BeanUtils.copyProperties(sd, m);
            
            m.setNo((rowNum ++ ) + "");
            m.setBillingDate(DateUtils.Date2LocalDate(sd.getBillingDate()));
            m.setSettlementDate(DateUtils.Date2LocalDate(sd.getSettlementDate()));
            
            // 项目金额
            BigDecimal projectAmount = new BigDecimal(0);
            for (ProjectDetails pd : sd.getProjects()) {
                projectAmount = projectAmount.add(pd.getAmount());
            }
            m.setProjectAmount(projectAmount);
            
            // 商品成本
            BigDecimal costPrice = new BigDecimal(0);
            BigDecimal itemAmount = new BigDecimal(0);
            for (ItemDetails item : sd.getItems()) {
                costPrice = costPrice.add(item.getCostPrice());
                itemAmount = itemAmount.add(item.getAmount());
            }
            m.setCostPrice(costPrice);
            m.setItemAmount(itemAmount);
            // 利润
            BigDecimal profit = new BigDecimal(0);
            if (sd.getActuallyPay().intValue() != 0) {
                profit = sd.getActuallyPay().subtract(costPrice).setScale(2, RoundingMode.HALF_UP);
            }
            m.setProfit(profit);
            
            data.add(m);
        
            totalAmountReceived = totalAmountReceived.add(m.getAmountReceived());
        }
        
        totalModel.setNo("合计");
        totalModel.setAmountReceived(totalAmountReceived);
        data.add(totalModel);
    }
    
    private void export() {
        FileChooser fc = new FileChooser();
        fc.setInitialFileName(DateUtils.Date2String(new Date())+".xls");
        File selectedFile = fc.showSaveDialog(MainApp.getInstance().getPrimaryStage());
        if (selectedFile != null) {
            try {
                Excel.getInstance().exportCapitalReport(table, selectedFile.getAbsolutePath());
            } catch (IOException ex) {
                org.apache.logging.log4j.Logger logger = LogManager.getLogger(CapitalReportTabContent.class.getName());
                logger.error(ex);
                MessageBox.showMessage("由于文件被占用无法导出。");
            }
        }
    }
    
    private TextField SNField = new TextField();
    private TextField customerNameField = new TextField();
    private TextField paymentField = new TextField();
    private ComboBox<String> billTypeField = new ComboBox<String>(); 
    private DatePicker settleFromDateField = new DatePicker();
    private DatePicker settleToDateField = new DatePicker();
    private Button btnExport = new Button("导出", ImageUtils.createImageView("future_projects_16px_505023_easyicon.net.png"));
    private MenuItem itemExport = new MenuItem("导出", ImageUtils.createImageView("future_projects_16px_505023_easyicon.net.png"));
    private Button btnQuery = new Button("查询");
    private Button btnBackAll = new Button("返回");
    private TableView table = new TableView();
    private final ObservableList<SettlementModel> data = FXCollections.observableArrayList();
    private CapitalReportTabContentController controller = new CapitalReportTabContentController();
    private SettlementModel totalModel = new SettlementModel();
}
