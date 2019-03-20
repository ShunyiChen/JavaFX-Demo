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
import com.dockingsoftware.autorepairsystem.component.model.ProjectModel;
import com.dockingsoftware.autorepairsystem.persistence.entity.Payment;
import com.dockingsoftware.autorepairsystem.ui.controller.ProjectSalesReportTabContentController;
import com.dockingsoftware.autorepairsystem.util.DateUtils;
import com.dockingsoftware.autorepairsystem.util.ImageUtils;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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

public class ProjectSalesReportTabContent extends VBox {

    public ProjectSalesReportTabContent() {
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
        
        grid.add(new Label("项目名:"), 4, 2);
        projectNameField.setPromptText("项目名");
        grid.add(projectNameField, 5, 2);
        
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
                String projectNameVal = projectNameField.getText();
                
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
                param.put("projectName", projectNameVal);
                
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
                if ("priceCol".equals(p.getId())
                        || "discountAmountCol".equals(p.getId())
                        || "amountCol".equals(p.getId())) {
                    return new BigDecimalCell();
                }
                else if ("settlementDateCol".equals(p.getId())
                        || "billingDateCol".equals(p.getId())
                        || "endTimeCol".equals(p.getId())
                        || "startTimeCol".equals(p.getId())) {
                    return new DatePickerCell();
                } 
                else if ("laborHourCol".equals(p.getId())) {
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
        TableColumn projectNameCol = new TableColumn("项目名称");
        TableColumn priceCol = new TableColumn("项目单价");
        TableColumn laborHourCol = new TableColumn("工时");
        TableColumn discountAmountCol = new TableColumn("优惠金额");
        TableColumn amountCol = new TableColumn("金额");
        TableColumn startTimeCol = new TableColumn("开工时间");
        TableColumn endTimeCol = new TableColumn("完工时间");
        TableColumn mechanicCol = new TableColumn("施工人员");
        TableColumn projectCategoryCol = new TableColumn("项目分类");
        TableColumn projectNotesCol = new TableColumn("项目备注");
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
        projectNameCol.setId("projectNameCol");
        priceCol.setId("priceCol");
        laborHourCol.setId("laborHourCol");
        discountAmountCol.setId("discountAmountCol");
        amountCol.setId("amountCol");
        projectNotesCol.setId("projectNotesCol");
        startTimeCol.setId("startTimeCol");
        endTimeCol.setId("endTimeCol");
        mechanicCol.setId("mechanicCol");
        projectCategoryCol.setId("projectCategoryCol");
        nameCol.setId("nameCol");
        licensePlateNumberCol.setId("licensePlateNumberCol");
        paymentCol.setId("paymentCol");
        settlementDateCol.setId("settlementDateCol");
        settlementNotesCol.setId("settlementNotesCol");
        settlementStateCol.setId("settlementStateCol");
      
        NoCol.setCellFactory(cellFactory);
        SNCol.setCellFactory(cellFactory);
        billingDateCol.setCellFactory(cellFactory);
        projectNameCol.setCellFactory(cellFactory);
        priceCol.setCellFactory(cellFactory);
        laborHourCol.setCellFactory(cellFactory);
        discountAmountCol.setCellFactory(cellFactory);
        amountCol.setCellFactory(cellFactory);
        projectNotesCol.setCellFactory(cellFactory);
        startTimeCol.setCellFactory(cellFactory);
        endTimeCol.setCellFactory(cellFactory);
        mechanicCol.setCellFactory(cellFactory);
        projectCategoryCol.setCellFactory(cellFactory);
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
        projectNameCol.setPrefWidth(120);
        priceCol.setPrefWidth(120);
        laborHourCol.setPrefWidth(120);
        discountAmountCol.setPrefWidth(120);
        amountCol.setPrefWidth(120);
        projectNotesCol.setPrefWidth(120);
        startTimeCol.setPrefWidth(120);
        endTimeCol.setPrefWidth(120);
        mechanicCol.setPrefWidth(120);
        projectCategoryCol.setPrefWidth(120);
        nameCol.setPrefWidth(120);
        licensePlateNumberCol.setPrefWidth(120);
        paymentCol.setPrefWidth(120);
        settlementDateCol.setPrefWidth(120);
        settlementNotesCol.setPrefWidth(120);
        settlementStateCol.setPrefWidth(120);

        NoCol.setCellValueFactory(new PropertyValueFactory<ItemModel, String>("No"));
        SNCol.setCellValueFactory(new PropertyValueFactory<ItemModel, String>("SN"));
        billingDateCol.setCellValueFactory(new PropertyValueFactory<ItemModel, String>("billingDate"));
        projectNameCol.setCellValueFactory(new PropertyValueFactory<ItemModel, String>("name"));
        priceCol.setCellValueFactory(new PropertyValueFactory<ItemModel, String>("price"));
        laborHourCol.setCellValueFactory(new PropertyValueFactory<ItemModel, String>("laborHour"));
        discountAmountCol.setCellValueFactory(new PropertyValueFactory<ItemModel, String>("discount"));
        amountCol.setCellValueFactory(new PropertyValueFactory<ItemModel, String>("amount"));
        projectNotesCol.setCellValueFactory(new PropertyValueFactory<ItemModel, String>("notes"));
        startTimeCol.setCellValueFactory(new PropertyValueFactory<ItemModel, LocalDate>("startTime"));
        endTimeCol.setCellValueFactory(new PropertyValueFactory<ItemModel, LocalDate>("endTime"));
        mechanicCol.setCellValueFactory(new PropertyValueFactory<ItemModel, String>("mechanic"));
        projectCategoryCol.setCellValueFactory(new PropertyValueFactory<ItemModel, String>("projectCategory"));
        nameCol.setCellValueFactory(new PropertyValueFactory<ItemModel, String>("customerName"));
        licensePlateNumberCol.setCellValueFactory(new PropertyValueFactory<ItemModel, String>("licensePlateNumber"));
        paymentCol.setCellValueFactory(new PropertyValueFactory<ItemModel, String>("payment"));
        settlementDateCol.setCellValueFactory(new PropertyValueFactory<ItemModel, String>("settlementDate"));
        settlementNotesCol.setCellValueFactory(new PropertyValueFactory<ItemModel, String>("settlementNotes"));
        settlementStateCol.setCellValueFactory(new PropertyValueFactory<ItemModel, String>("settlementState"));

        table.setItems(data);
        table.getColumns().addAll(NoCol, SNCol, billingDateCol, projectNameCol, priceCol, laborHourCol, discountAmountCol, amountCol, projectNotesCol, startTimeCol, endTimeCol, mechanicCol, projectCategoryCol, nameCol, licensePlateNumberCol, paymentCol, settlementDateCol, settlementNotesCol, settlementStateCol); 
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
        
        List<Object[]> results = (List<Object[]>) controller.listProjectDetails.call(param);
        
        // 数量合计
        float laborHour = 0;
        // 金额合计
        BigDecimal totalAmount = new BigDecimal(0);
        // 优惠合计
        BigDecimal totalDiscount = new BigDecimal(0);
        // 单价合计
        BigDecimal totalPrice= new BigDecimal(0);
        
        int rowNum = 1;
        for (Object[] item : results) {
            ProjectModel projectModel = new ProjectModel();
            
            projectModel.setNo((rowNum ++ ) + "");
            projectModel.setSN(item[0].toString());
            projectModel.setBillingDate(DateUtils.Date2LocalDate((Date) item[1]));
            projectModel.setName(item[2].toString());
            projectModel.setPrice(new BigDecimal(item[3].toString()));
            projectModel.setLaborHour(Float.parseFloat(item[4].toString()));
            projectModel.setDiscount(new BigDecimal(item[5].toString()));
            projectModel.setAmount(new BigDecimal(item[6].toString()));
            projectModel.setNotes(item[7] == null ? "" :item[7].toString());
            projectModel.setStartTime(DateUtils.Date2LocalDate((Date) item[8]));
            projectModel.setEndTime(DateUtils.Date2LocalDate((Date) item[9]));
            projectModel.setMechanic(item[10] == null ? "" : item[10].toString());
            projectModel.setProjectCategory(item[11].toString());
            projectModel.setCustomerName(item[12].toString());
            projectModel.setLicensePlateNumber(item[13].toString());
            projectModel.setPayment(item[14] == null ? "" : item[14].toString());
            projectModel.setSettlementDate(item[15] == null ? null : DateUtils.Date2LocalDate((Date) item[15]));
            projectModel.setSettlementNotes(item[16] == null ? "" : item[16].toString());
            projectModel.setSettlementState(item[17] == null ? "" : item[17].toString());
            
            data.add(projectModel);
            
            laborHour += projectModel.getLaborHour();
            totalAmount = totalAmount.add(projectModel.getAmount());
            totalDiscount = totalDiscount.add(projectModel.getDiscount());
            totalPrice = totalPrice.add(projectModel.getPrice());
        }
        
        totalModel.setNo("合计");
        totalModel.setAmount(totalAmount);
        totalModel.setLaborHour(laborHour);
        totalModel.setDiscount(totalDiscount);
        totalModel.setPrice(totalPrice);
        data.add(totalModel);
    }
    
    private void export() {
        FileChooser fc = new FileChooser();
        fc.setInitialFileName(DateUtils.Date2String(new Date())+".xls");
        File selectedFile = fc.showSaveDialog(MainApp.getInstance().getPrimaryStage());
        if (selectedFile != null) {
            try {
                Excel.getInstance().exportProjectSalesReport(table, selectedFile.getAbsolutePath());
            } catch (IOException ex) {
                org.apache.logging.log4j.Logger logger = LogManager.getLogger(ProjectSalesReportTabContent.class.getName());
                logger.error(ex);
                MessageBox.showMessage("由于文件被占用无法导出。");
            }
        }
    }
    
    private TextField SNField = new TextField();
    private TextField customerNameField = new TextField();
    private TextField licensePlateNumberField = new TextField();
    private TextField projectNameField = new TextField();
    
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
    private final ObservableList<ProjectModel> data = FXCollections.observableArrayList();
    private ProjectSalesReportTabContentController controller = new ProjectSalesReportTabContentController();
    private ProjectModel totalModel = new ProjectModel();
}