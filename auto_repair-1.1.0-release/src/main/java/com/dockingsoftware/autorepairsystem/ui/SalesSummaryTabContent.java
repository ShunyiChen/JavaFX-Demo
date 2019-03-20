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
import com.dockingsoftware.autorepairsystem.persistence.entity.BusinessType;
import com.dockingsoftware.autorepairsystem.persistence.entity.ItemDetails;
import com.dockingsoftware.autorepairsystem.persistence.entity.ProjectDetails;
import com.dockingsoftware.autorepairsystem.persistence.entity.SettlementDetails;
import com.dockingsoftware.autorepairsystem.ui.controller.SalesSummaryTabContentController;
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
 * 销售单汇总表
 * 
 * @author Shunyi Chen
 */
public class SalesSummaryTabContent extends VBox {

    public SalesSummaryTabContent() {
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

        grid.add(new Label("业务类型:"), 0, 1);
        businessTypeField.getItems().add("");
        List<BusinessType> lstBusinessType = (List<BusinessType>) controller.listBusinessType.call("");
        for (BusinessType bt : lstBusinessType) {
            businessTypeField.getItems().add(bt.getName());
        }
        businessTypeField.getSelectionModel().select(0);
        businessTypeField.setPrefWidth(173);
        grid.add(businessTypeField, 1, 1);
        
        grid.add(new Label("接待人:"), 2, 1);
        receptionistField.setPromptText("接待人");
        grid.add(receptionistField, 3, 1);
        
        grid.add(new Label("备注:"), 4, 1);
        notesField.setPromptText("备注");
        grid.add(notesField, 5, 1);
        
        grid.add(new Label("客户名:"), 0, 2);
        customerNameField.setPromptText("客户名");
        grid.add(customerNameField, 1, 2);
        
        grid.add(new Label("车牌号:"), 2, 2);
        licensePlateNumberField.setPromptText("车牌号");
        grid.add(licensePlateNumberField, 3, 2);
        
        grid.add(new Label("车型:"), 4, 2);
        modelField.setPromptText("车型");
        grid.add(modelField, 5, 2);
        
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
                try {
                    SimpleDateFormat f = new SimpleDateFormat("M/d/yyyy");
                    String snVal = SNField.getText();
                    Date billFromDateVal = DateUtils.String2Date(billFromDateField.getEditor().getText(), f);
                    Date billToDateVal = DateUtils.String2Date(billToDateField.getEditor().getText(), f);
                    String businessType = businessTypeField.getValue();
                    String receptionistVal = receptionistField.getText();
                    String notesVal = notesField.getText();
                    String customerNameVal = customerNameField.getText();
                    String licensePlateNumberVal = licensePlateNumberField.getText();
                    String modelVal = modelField.getText();

                    Criterion c1 = Restrictions.ne("id", "0");
                    if (!snVal.isEmpty()) {
                        c1 = Restrictions.ilike("SN", snVal, MatchMode.ANYWHERE);
                    }
                    Criterion c2 = Restrictions.ne("id", "0");
                    if (!businessType.isEmpty()) {
                        c2 = Restrictions.ilike("businessType", businessType, MatchMode.ANYWHERE);
                    }
                    Criterion c3 = Restrictions.ne("id", "0");
                    if (!receptionistVal.isEmpty()) {
                        c3 = Restrictions.ilike("receptionist", receptionistVal, MatchMode.ANYWHERE);
                    }
                    Criterion c4 = Restrictions.ne("id", "0");
                    if (!customerNameVal.isEmpty()) {
                        c4 = Restrictions.ilike("customerName", customerNameVal, MatchMode.ANYWHERE);
                    }
                    Criterion c5 = Restrictions.ne("id", "0");
                    if (!notesVal.isEmpty()) {
                        c5 = Restrictions.ilike("notes", notesVal, MatchMode.ANYWHERE);
                    }
                    Criterion c6 = Restrictions.ne("id", "0");
                    if (!licensePlateNumberVal.isEmpty()) {
                        c6 = Restrictions.ilike("licensePlateNumber", licensePlateNumberVal, MatchMode.ANYWHERE);
                    }
                    Criterion c7 = Restrictions.ne("id", "0");
                    if (!modelVal.isEmpty()) {
                        c7 = Restrictions.ilike("model", modelVal, MatchMode.ANYWHERE);
                    }
                
                    Criterion c8 = Restrictions.ne("id", "0");
                    if (billFromDateVal != null && billToDateVal != null) {
                        c8 = Restrictions.between("billingDate", billFromDateVal, billToDateVal);
                    }
                    
                    Criterion c = Restrictions.and(c1, c2, c3, c4, c5, c6, c7, c8);
                    
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
                if ("discountAmountCol".equals(p.getId())
                        || "receivableAmountCol".equals(p.getId())
                        || "actuallyPayCol".equals(p.getId())
                        || "amountReceivedCol".equals(p.getId())
                        || "owingAmountCol".equals(p.getId())
                        || "costPriceCol".equals(p.getId())
                        || "itemAmountCol".equals(p.getId())
                        || "projectAmountCol".equals(p.getId())
                        || "profitCol".equals(p.getId())) {
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
        TableColumn nameCol = new TableColumn("客户名称");
        TableColumn licensePlateNumberCol = new TableColumn("车牌号");
        TableColumn modelCol = new TableColumn("车型");
        TableColumn billingDateCol = new TableColumn("开单日期");
        TableColumn discountAmountCol = new TableColumn("优惠金额");
        TableColumn receivableAmountCol = new TableColumn("应收金额");
        TableColumn actuallyPayCol = new TableColumn("实际支付");
        TableColumn amountReceivedCol = new TableColumn("已收金额");
        TableColumn owingAmountCol = new TableColumn("尚欠金额");
        TableColumn costPriceCol = new TableColumn("商品成本");
        TableColumn itemAmountCol = new TableColumn("商品金额");
        TableColumn projectAmountCol = new TableColumn("项目金额");
        TableColumn profitCol = new TableColumn("利润");
        TableColumn receptionistCol = new TableColumn("接待人");
        TableColumn businessTypeCol = new TableColumn("业务类型");
        TableColumn notesCol = new TableColumn("备注");

        // 渲染列ID
        NoCol.setId("NoCol");
        SNCol.setId("SNCol");
        nameCol.setId("nameCol");
        licensePlateNumberCol.setId("licensePlateNumberCol");
        modelCol.setId("modelCol");
        billingDateCol.setId("billingDateCol");
        discountAmountCol.setId("discountAmountCol");
        receivableAmountCol.setId("receivableAmountCol");
        actuallyPayCol.setId("actuallyPayCol");
        amountReceivedCol.setId("amountReceivedCol");
        owingAmountCol.setId("owingAmountCol");
        costPriceCol.setId("costPriceCol");
        itemAmountCol.setId("itemAmountCol");
        projectAmountCol.setId("projectAmountCol");
        profitCol.setId("profitCol");
        receptionistCol.setId("receptionistCol");
        businessTypeCol.setId("businessTypeCol");
        notesCol.setId("notesCol");
      
        NoCol.setCellFactory(cellFactory);
        SNCol.setCellFactory(cellFactory);
        nameCol.setCellFactory(cellFactory);
        licensePlateNumberCol.setCellFactory(cellFactory);
        modelCol.setCellFactory(cellFactory);
        billingDateCol.setCellFactory(cellFactory);
        discountAmountCol.setCellFactory(cellFactory);
        receivableAmountCol.setCellFactory(cellFactory);
        actuallyPayCol.setCellFactory(cellFactory);
        amountReceivedCol.setCellFactory(cellFactory);
        owingAmountCol.setCellFactory(cellFactory);
        costPriceCol.setCellFactory(cellFactory);
        itemAmountCol.setCellFactory(cellFactory);
        projectAmountCol.setCellFactory(cellFactory);
        profitCol.setCellFactory(cellFactory);
        receptionistCol.setCellFactory(cellFactory);
        businessTypeCol.setCellFactory(cellFactory);
        notesCol.setCellFactory(cellFactory);

        // 设置列宽
        NoCol.setPrefWidth(50);
        SNCol.setPrefWidth(120);
        nameCol.setPrefWidth(120);
        licensePlateNumberCol.setPrefWidth(120);
        modelCol.setPrefWidth(120);
        billingDateCol.setPrefWidth(120);
        discountAmountCol.setPrefWidth(120);
        receivableAmountCol.setPrefWidth(120);
        actuallyPayCol.setPrefWidth(120);
        amountReceivedCol.setPrefWidth(120);
        owingAmountCol.setPrefWidth(120);
        costPriceCol.setPrefWidth(120);
        itemAmountCol.setPrefWidth(120);
        projectAmountCol.setPrefWidth(120);
        profitCol.setPrefWidth(120);
        receptionistCol.setPrefWidth(120);
        businessTypeCol.setPrefWidth(120);
        notesCol.setPrefWidth(120);

        NoCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, String>("No"));
        SNCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, String>("SN"));
        nameCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, String>("customerName"));
        licensePlateNumberCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, String>("licensePlateNumber"));
        modelCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, String>("model"));
        billingDateCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, String>("billingDate"));
        discountAmountCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, String>("discountAmount"));
        receivableAmountCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, String>("receivableAmount"));
        actuallyPayCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, String>("actuallyPay"));
        amountReceivedCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, String>("amountReceived"));
        owingAmountCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, String>("owingAmount"));
        costPriceCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, String>("costPrice"));
        itemAmountCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, String>("itemAmount"));
        projectAmountCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, String>("projectAmount"));
        profitCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, String>("profit"));
        receptionistCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, String>("receptionist"));
        businessTypeCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, String>("businessType"));
        notesCol.setCellValueFactory(new PropertyValueFactory<SettlementModel, String>("notes"));
        
        table.setItems(data);
        table.getColumns().addAll(NoCol, SNCol, nameCol, licensePlateNumberCol, modelCol, billingDateCol, discountAmountCol, receivableAmountCol, actuallyPayCol, amountReceivedCol, owingAmountCol, costPriceCol, itemAmountCol, projectAmountCol, profitCol, receptionistCol, businessTypeCol, notesCol); 
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
            
            c = Restrictions.between("billingDate", cal.getTime(), new Date());
        }
        
        List<SettlementDetails> results = (List<SettlementDetails>) controller.listSettlementDetails.call(c);
        
        // 优惠合计
        BigDecimal totalDiscount = new BigDecimal(0);
        // 应收金额合计
        BigDecimal totalReceivableAmount = new BigDecimal(0);
        // 实际支付合计
        BigDecimal totalActuallyPay = new BigDecimal(0);
        // 已收合计
        BigDecimal totalAmountReceived = new BigDecimal(0);
        // 欠合计
        BigDecimal totalOwingReceived = new BigDecimal(0);
        // 商品成本合计
        BigDecimal totalCostPrice = new BigDecimal(0);
        // 商品金额合计
        BigDecimal totalItemAmount = new BigDecimal(0);
        // 项目金额合计
        BigDecimal totalProjectAmount = new BigDecimal(0);
        // 利润合计
        BigDecimal totalprofit = new BigDecimal(0);
        
        int rowNum = 1;
        for (SettlementDetails sd : results) {
            SettlementModel m = new SettlementModel();
            BeanUtils.copyProperties(sd, m);
            
            m.setNo((rowNum ++ ) + "");
            m.setBillingDate(DateUtils.Date2LocalDate(sd.getBillingDate()));
            
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
            
            totalDiscount = totalDiscount.add(m.getDiscountAmount());
            totalReceivableAmount = totalReceivableAmount.add(m.getReceivableAmount());
            totalActuallyPay = totalActuallyPay.add(m.getActuallyPay());
            totalAmountReceived = totalAmountReceived.add(m.getAmountReceived());
            totalOwingReceived = totalOwingReceived.add(m.getOwingAmount());
            totalCostPrice = totalCostPrice.add(m.getCostPrice());
            totalItemAmount = totalItemAmount.add(m.getItemAmount());
            totalProjectAmount = totalProjectAmount.add(m.getProjectAmount());
            totalprofit = totalprofit.add(m.getProfit());
        }
        
        totalModel.setNo("合计");
        totalModel.setDiscountAmount(totalDiscount);
        totalModel.setReceivableAmount(totalReceivableAmount);
        totalModel.setActuallyPay(totalActuallyPay);
        totalModel.setAmountReceived(totalAmountReceived);
        totalModel.setOwingAmount(totalOwingReceived);
        totalModel.setCostPrice(totalCostPrice);
        totalModel.setItemAmount(totalItemAmount);
        totalModel.setProjectAmount(totalProjectAmount);
        totalModel.setProfit(totalprofit);
        data.add(totalModel);
    }
    
    private void export() {
        FileChooser fc = new FileChooser();
        fc.setInitialFileName(DateUtils.Date2String(new Date())+".xls");
        File selectedFile = fc.showSaveDialog(MainApp.getInstance().getPrimaryStage());
        if (selectedFile != null) {
            try {
                Excel.getInstance().exportSalesSummaryReport(table, selectedFile.getAbsolutePath());
            } catch (IOException ex) {
                org.apache.logging.log4j.Logger logger = LogManager.getLogger(SalesSummaryTabContent.class.getName());
                logger.error(ex);
                MessageBox.showMessage("由于文件被占用无法导出。");
            }
        }
    }
    
    private TextField SNField = new TextField();
    private TextField customerNameField = new TextField();
    private TextField licensePlateNumberField = new TextField();
    private TextField receptionistField = new TextField();
    private TextField notesField = new TextField();
    private TextField modelField = new TextField();
    private ComboBox<String> businessTypeField = new ComboBox<String>(); 
    private DatePicker billFromDateField = new DatePicker();
    private DatePicker billToDateField = new DatePicker();
    private Button btnExport = new Button("导出", ImageUtils.createImageView("future_projects_16px_505023_easyicon.net.png"));
    private MenuItem itemExport = new MenuItem("导出", ImageUtils.createImageView("future_projects_16px_505023_easyicon.net.png"));
    private Button btnQuery = new Button("查询");
    private Button btnBackAll = new Button("返回");
    private TableView table = new TableView();
    private final ObservableList<SettlementModel> data = FXCollections.observableArrayList();
    private SalesSummaryTabContentController controller = new SalesSummaryTabContentController();
    private SettlementModel totalModel = new SettlementModel();
}