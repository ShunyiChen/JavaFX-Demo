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
import com.dockingsoftware.autorepairsystem.component.model.IAEModel;
import com.dockingsoftware.autorepairsystem.persistence.entity.IAE;
import com.dockingsoftware.autorepairsystem.ui.controller.IncomeAndExpenseTabContentController;
import com.dockingsoftware.autorepairsystem.util.DateUtils;
import com.dockingsoftware.autorepairsystem.util.ImageUtils;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
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

public class IncomeAndExpenseTabContent extends VBox {

    public IncomeAndExpenseTabContent() {
        initComponent();
    }
    
    private void initComponent() {
        BorderPane tablePane = createTablePane();
        this.getChildren().addAll(createToolBar(), createSearchPane(), tablePane);
        VBox.setVgrow(tablePane, Priority.ALWAYS);
    }
    

    private ToolBar createToolBar() {
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
        
        btnExport.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                export();
            }
        });

        ToolBar bar = new ToolBar();
        bar.getItems().addAll(btnAdd, btnUpdate, btnRemove, new Separator(), btnExport);

        return bar;
    }
    
    private GridPane createSearchPane() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 20, 5, 10));
        
        grid.add(new Label("科目:"), 0, 0);
        subjectField.setPromptText("科目");
        grid.add(subjectField, 1, 0);
        
        grid.add(new Label("支付日期:"), 2, 0);
        fromDate.setEditable(true);
        grid.add(fromDate, 3, 0);
        
        grid.add(new Label("到:"), 4, 0);
        toDate.setEditable(true);
        grid.add(toDate, 5, 0);

        grid.add(new Label("支付方式:"), 0, 1);
        paymentField.setPromptText("支付方式");
        grid.add(paymentField, 1, 1);
        
        grid.add(new Label("经手人:"), 2, 1);
        handlerNameField.setPromptText("经手人");
        grid.add(handlerNameField, 3, 1);
        
        grid.add(new Label("摘要:"), 4, 1);
        summaryField.setPromptText("摘要");
        grid.add(summaryField, 5, 1);
        
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
                String subjectVal = subjectField.getText();
                String paymentVal = paymentField.getText();
                String handlerNameVal = handlerNameField.getText();
                String summaryVal = summaryField.getText();

                Criterion c1 = Restrictions.ne("id", "0");
                if (!subjectVal.isEmpty()) {
                    c1 = Restrictions.ilike("subject", subjectVal, MatchMode.ANYWHERE);
                }
                Criterion c2 = Restrictions.ne("id", "0");
                if (!paymentVal.isEmpty()) {
                    c2 = Restrictions.ilike("payment", paymentVal, MatchMode.ANYWHERE);
                }
                Criterion c4 = Restrictions.ne("id", "0");
                if (!handlerNameVal.isEmpty()) {
                    c4 = Restrictions.ilike("handlerName", handlerNameVal, MatchMode.ANYWHERE);
                }
                Criterion c5 = Restrictions.ne("id", "0");
                if (!summaryVal.isEmpty()) {
                    c5 = Restrictions.ilike("summary", summaryVal, MatchMode.ANYWHERE);
                }

                Criterion c6 = Restrictions.ne("id", "0");
                if (!fromDate.getEditor().getText().isEmpty() && !toDate.getEditor().getText().isEmpty()) {
                    // 创建0时0分0秒一个date对象
                    Date from = DateUtils.LocalDate2Date(fromDate.getValue());
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(from);
                    cal.set(Calendar.HOUR_OF_DAY, 0);
                    cal.set(Calendar.MINUTE,0);
                    cal.set(Calendar.SECOND,0);
                    cal.set(Calendar.MILLISECOND,0);
                    // 创建23时59分59秒一个date对象
                    Date to = DateUtils.LocalDate2Date(toDate.getValue());
                    Calendar cal2 = Calendar.getInstance();
                    cal2.setTime(to);
                    cal2.set(Calendar.HOUR_OF_DAY, 23);
                    cal2.set(Calendar.MINUTE,59);
                    cal2.set(Calendar.SECOND,59);
                    cal2.set(Calendar.MILLISECOND,999);

                    c5 = Restrictions.between("paymentDate", cal.getTime(), cal2.getTime());
                }

                Criterion c = Restrictions.and(c1, c2, c4, c5, c6);

                loadTableData(c);
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
                if ("incomeCol".equals(p.getId())
                        || "expenseCol".equals(p.getId())) {
                    return new BigDecimalCell();
                }
                else if ("paymentDateCol".equals(p.getId())) {
                    return new DatePickerCell();
                } 
                else {
                    return new EditingCell();
                }
            }
        };
        
        TableColumn NoCol = new TableColumn("序号");
        TableColumn subjectCol = new TableColumn("科目");
        TableColumn summaryCol = new TableColumn("摘要");
        TableColumn incomeCol = new TableColumn("收入");
        TableColumn expenseCol = new TableColumn("支出");
        TableColumn handlerNameCol = new TableColumn("经手人");
        TableColumn paymentCol = new TableColumn("支付方式");
        TableColumn paymentDateCol = new TableColumn("支付日期");

        // 渲染列ID
        NoCol.setId("NoCol");
        subjectCol.setId("subjectCol");
        summaryCol.setId("summaryCol");
        incomeCol.setId("incomeCol");
        expenseCol.setId("expenseCol");
        handlerNameCol.setId("handlerNameCol");
        paymentCol.setId("paymentCol");
        paymentDateCol.setId("paymentDateCol");
      
        NoCol.setCellFactory(cellFactory);
        subjectCol.setCellFactory(cellFactory);
        summaryCol.setCellFactory(cellFactory);
        incomeCol.setCellFactory(cellFactory);
        expenseCol.setCellFactory(cellFactory);
        handlerNameCol.setCellFactory(cellFactory);
        paymentCol.setCellFactory(cellFactory);
        paymentDateCol.setCellFactory(cellFactory);

        // 设置列宽
        NoCol.setPrefWidth(50);
        subjectCol.setPrefWidth(120);
        summaryCol.setPrefWidth(120);
        incomeCol.setPrefWidth(120);
        expenseCol.setPrefWidth(120);
        handlerNameCol.setPrefWidth(120);
        paymentCol.setPrefWidth(120);
        paymentDateCol.setPrefWidth(120);

        NoCol.setCellValueFactory(new PropertyValueFactory<IAEModel, String>("No"));
        subjectCol.setCellValueFactory(new PropertyValueFactory<IAEModel, String>("subject"));
        summaryCol.setCellValueFactory(new PropertyValueFactory<IAEModel, String>("summary"));
        incomeCol.setCellValueFactory(new PropertyValueFactory<IAEModel, String>("incomeAmount"));
        expenseCol.setCellValueFactory(new PropertyValueFactory<IAEModel, String>("expenseAmount"));
        handlerNameCol.setCellValueFactory(new PropertyValueFactory<IAEModel, String>("handlerName"));
        paymentCol.setCellValueFactory(new PropertyValueFactory<IAEModel, String>("payment"));
        paymentDateCol.setCellValueFactory(new PropertyValueFactory<IAEModel, String>("paymentDate"));
        
        table.setItems(data);
        table.getColumns().addAll(NoCol, subjectCol, summaryCol, incomeCol, expenseCol, handlerNameCol, paymentCol, paymentDateCol); 
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
                    if (data.size() > 0) {
                        update();
                    }
                }
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
            
            c = Restrictions.between("paymentDate", cal.getTime(), new Date());
        }
        
        List<IAE> results = (List<IAE>) controller.listIAE.call(c);
   
        // 收入合计
        BigDecimal totalIncomeAmount = new BigDecimal(0);
        // 支出合计
        BigDecimal totalExpenseAmount = new BigDecimal(0);        
        int rowNum = 1;
        for (IAE sd : results) {
            
            IAEModel m = new IAEModel();
            BeanUtils.copyProperties(sd, m);
            
            m.setNo((rowNum ++ ) + "");
            m.setPaymentDate(DateUtils.Date2LocalDate(sd.getPaymentDate()));
            totalIncomeAmount = totalIncomeAmount.add(m.getIncomeAmount());
            totalExpenseAmount = totalExpenseAmount.add(m.getExpenseAmount());
            data.add(m);
        }
        
        totalModel.setNo("合计");
        totalModel.setIncomeAmount(totalIncomeAmount);
        totalModel.setExpenseAmount(totalExpenseAmount);
        data.add(totalModel);
        
        disable(true);
    }
    
    private void add() {
        AddOrUpdateIAEDialog dia = new AddOrUpdateIAEDialog();
        dia.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> dia.saveOrUpdateIAE(iae -> loadTableData(null)));
    }
    
    private void update() {
        IAEModel selectedModel = (IAEModel) table.getSelectionModel().getSelectedItem();
        if (selectedModel != totalModel) {
            IAE editIAE = (IAE) controller.getIAEById.call(selectedModel.getId());
            AddOrUpdateIAEDialog dia = new AddOrUpdateIAEDialog(editIAE);
            dia.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> dia.saveOrUpdateIAE(iae -> loadTableData(null)));
        }
    }
    
    private void remove() {
        final IAEModel selectedModel = (IAEModel) table.getSelectionModel().getSelectedItem();
        if (selectedModel != totalModel) {
            Callback callback = new Callback() {
                @Override
                public Object call(Object param) {
                    IAE removeIAE = (IAE) controller.getIAEById.call(selectedModel.getId());
                    controller.deleteIAE.call(removeIAE);

                    loadTableData(null);

                    if (data.isEmpty()) {
                        disable(true);
                    }
                    return "";
                } 
            };
            MessageBox.showMessage("请确认是否删除该行记录。", Alert.AlertType.CONFIRMATION, callback);
        }
    }
    
    private void export() {
        FileChooser fc = new FileChooser();
        fc.setInitialFileName(DateUtils.Date2String(new Date())+".xls");
        File selectedFile = fc.showSaveDialog(MainApp.getInstance().getPrimaryStage());
        if (selectedFile != null) {
            try {
                Excel.getInstance().exportIncomeAndExpenseReport(table, selectedFile.getAbsolutePath());
            } catch (IOException ex) {
                org.apache.logging.log4j.Logger logger = LogManager.getLogger(IncomeAndExpenseTabContent.class.getName());
                logger.error(ex);
                MessageBox.showMessage("由于文件被占用无法导出。");
            }
        }
    }
    
    private void disable(boolean bool) {
        btnUpdate.setDisable(bool);
        btnRemove.setDisable(bool);
    }
    
    private TextField subjectField = new TextField();
    private TextField handlerNameField = new TextField();
    private TextField summaryField = new TextField();
    private TextField paymentField = new TextField();
    private DatePicker fromDate = new DatePicker();
    private DatePicker toDate = new DatePicker();
    private Button btnAdd = new Button("记一笔", ImageUtils.createImageView("pencil_16px_505047_easyicon.net.png"));
    private Button btnUpdate = new Button("更改");
    private Button btnRemove = new Button("删除");
    private Button btnExport = new Button("导出", ImageUtils.createImageView("future_projects_16px_505023_easyicon.net.png"));
    private MenuItem itemExport = new MenuItem("导出", ImageUtils.createImageView("future_projects_16px_505023_easyicon.net.png"));
    private Button btnQuery = new Button("查询");
    private Button btnBackAll = new Button("返回");
    private TableView table = new TableView();
    private final ObservableList<IAEModel> data = FXCollections.observableArrayList();
    private IncomeAndExpenseTabContentController controller = new IncomeAndExpenseTabContentController();
    private IAEModel totalModel = new IAEModel();
}

