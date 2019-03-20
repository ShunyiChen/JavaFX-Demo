/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.ui;

import com.dockingsoftware.autorepairsystem.MainApp;
import com.dockingsoftware.autorepairsystem.common.Excel;
import com.dockingsoftware.autorepairsystem.component.BigDecimalCell;
import com.dockingsoftware.autorepairsystem.component.ComboBoxCell;
import com.dockingsoftware.autorepairsystem.component.DatePickerCell;
import com.dockingsoftware.autorepairsystem.component.EditingCell;
import com.dockingsoftware.autorepairsystem.component.MessageBox;
import com.dockingsoftware.autorepairsystem.component.model.InsuranceModel;
import com.dockingsoftware.autorepairsystem.persistence.entity.Customer;
import com.dockingsoftware.autorepairsystem.persistence.entity.Insurance;
import com.dockingsoftware.autorepairsystem.ui.controller.InsuranceTabContentController;
import com.dockingsoftware.autorepairsystem.util.DateUtils;
import com.dockingsoftware.autorepairsystem.util.ImageUtils;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

/**
 * 续保台账
 * 
 * @author Shunyi Chen
 */
public class InsuranceTabContent extends VBox {

    /**
     * Constructor.
     * 
     */
    public InsuranceTabContent() {
        initComponents();
    }
    
    private void initComponents() {
        
        BorderPane pane = tablePane();
        
        this.getChildren().addAll(toolbar(), criteriaQueryPane(), pane);
        
        VBox.setVgrow(pane, Priority.ALWAYS);
        
        startup();
    }

    private void startup() {
        Service<Void> service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {           
                    @Override
                    protected Void call() throws Exception {
                        //Background work                       
                        final CountDownLatch latch = new CountDownLatch(1);
                        Platform.runLater(new Runnable() {                          
                            @Override
                            public void run() {
//                                try{
//                                    //FX Stuff done here
//                                    checkRateSettings();
//                                }finally{
//                                    latch.countDown();
//                                }
                            }
                        });
                        latch.await();                      
                        //Keep with the background work
                        return null;
                    }
                };
            }
        };
        service.start();
    }
    
    private ToolBar toolbar() {
        
        btnChoose.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                ChooseCustomerDialog dia = new ChooseCustomerDialog();
                dia.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> dia.returnCustomer(cmodel -> insertInsuranceRow(cmodel)));
            }
        });
        
        btnAdd.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                AddOrUpdateCustomerDialog dia = new AddOrUpdateCustomerDialog();
                dia.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> dia.saveOrUpdateCustomer(insurance -> insertInsuranceRow(insurance)));
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
                 
                InsuranceModel selectedModel = (InsuranceModel) insuranceTable.getSelectionModel().getSelectedItem();
                if (selectedModel != totalModel) {
                     Callback callback = new Callback() {
                        @Override
                        public Object call(Object param) {
                            Insurance i = new Insurance();
                            BeanUtils.copyProperties(selectedModel, i);
                             
                            controller.deleteInsurance.call(i);
                                            
                            data.remove(selectedModel);
                
                            updateTotalAmount();
                            
                            if (data.size() == 0) {
                                disable(true);
                            }
                            
                            return "";
                        }
                    };
                     
                    MessageBox.showMessage("请确定是否删除该条记录。", Alert.AlertType.CONFIRMATION, callback);
                }
            }
        });
        
        btnSetting.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                showInsuranceSettingsDialog();
            }
        });
        
        btnExport.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                export();
            }
        });
        
        ToolBar bar = new ToolBar();
        
        bar.getItems().addAll(btnChoose, new Separator(), btnAdd, btnUpdate, btnRemove, btnSetting, new Separator(), btnExport);
        
        return bar;
    }
    
    private GridPane criteriaQueryPane() {
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 10, 10));
        
        grid.add(new Label("被保险人:"), 0, 0);
        name.setPromptText("被保险人");
        grid.add(name, 1, 0);
        
        grid.add(new Label("车牌号:"), 2, 0);
        licensePlateNumberField.setPromptText("车牌号");
        grid.add(licensePlateNumberField, 3, 0);
        
        grid.add(new Label("状态:"), 4, 0);
        statusField.setPrefWidth(173);
        statusField.getItems().addAll("", "已出单", "未出单");
        statusField.getSelectionModel().select(0);
        grid.add(statusField, 5, 0);
        
        grid.add(new Label("出单日期:"), 0, 1);
        searchFrom.setEditable(true);
        grid.add(searchFrom, 1, 1);
        
        grid.add(new Label("到:"), 2, 1);
        searchTo.setEditable(true);
        grid.add(searchTo, 3, 1);
        
        grid.add(new Label("备注:"), 4, 1);
        grid.add(notesField, 5, 1);
        
        grid.add(new Label("生效日期:"), 0, 2);
        fromField.setEditable(true);
        grid.add(fromField, 1, 2);
        
        grid.add(new Label("到:"), 2, 2);
        toField.setEditable(true);
        grid.add(toField, 3, 2);
        
        grid.add(new Label("销售员:"), 4, 2);
        grid.add(sellerField, 5, 2);
        
        HBox h = new HBox();
        h.setSpacing(5);
        h.setAlignment(Pos.CENTER);
        grid.add(h, 6, 2);
 
        btnBackAll.setPrefWidth(60);
        h.getChildren().addAll(btnSearch, btnBackAll);
        
        btnSearch.setGraphic(ImageUtils.createImageView("job_search_16px_1192818_easyicon.net.png"));
        btnSearch.setStyle("-fx-font: 13 arial; -fx-base: rgb(0, 150, 201);");
        btnBackAll.setStyle("-fx-font: 13 arial;");
        
        btnSearch.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                
                String nameVal = name.getText();
                String licensePlateNumberVal = licensePlateNumberField.getText();
                String statusVal = statusField.getValue();
                String notesVal = notesField.getText();
                String sellerVal = sellerField.getText();
               
                Criterion c1 = Restrictions.ne("id", "0");
                if (!nameVal.isEmpty()) {
                    c1 = Restrictions.ilike("name", nameVal, MatchMode.ANYWHERE);
                }
                Criterion c2 = Restrictions.ne("id", "0");
                if (!licensePlateNumberVal.isEmpty()) {
                    c2 = Restrictions.ilike("licensePlateNumber", licensePlateNumberVal, MatchMode.ANYWHERE);
                }
                Criterion c3 = Restrictions.ne("id", "0");
                if (!statusVal.isEmpty()) {
                    c3 = Restrictions.eq("status", statusVal);
                }
                Criterion c4 = Restrictions.ne("id", "0");
                if (!notesVal.isEmpty()) {
                    c4 = Restrictions.ilike("insuranceNotes", notesVal);
                }
                Criterion c5 = Restrictions.ne("id", "0");
                if (!sellerVal.isEmpty()) {
                    c5 = Restrictions.ilike("seller", sellerVal);
                }
                
                Criterion c6 = Restrictions.ne("id", "0");
                if (!searchFrom.getEditor().getText().isEmpty() && !searchTo.getEditor().getText().isEmpty()) {
                    c6 = Restrictions.between("outDate", DateUtils.LocalDate2Date(searchFrom.getValue()), DateUtils.LocalDate2Date(searchTo.getValue()));
                }
                
                Criterion c7 = Restrictions.ne("id", "0");
                if (!fromField.getEditor().getText().isEmpty() && !toField.getEditor().getText().isEmpty()) {
                    
                    // 创建0时0分0秒一个date对象
                    Date from = DateUtils.LocalDate2Date(fromField.getValue());
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(from);
                    cal.set(Calendar.HOUR_OF_DAY, 0);
                    cal.set(Calendar.MINUTE,0);
                    cal.set(Calendar.SECOND,0);
                    cal.set(Calendar.MILLISECOND,0);
                    // 创建23时59分59秒一个date对象
                    Date to = DateUtils.LocalDate2Date(toField.getValue());
                    Calendar cal2 = Calendar.getInstance();
                    cal2.setTime(to);
                    cal2.set(Calendar.HOUR_OF_DAY, 23);
                    cal2.set(Calendar.MINUTE,59);
                    cal2.set(Calendar.SECOND,59);
                    cal2.set(Calendar.MILLISECOND,999);
                    
                    c7 = Restrictions.between("fromDate", cal.getTime(), cal2.getTime());
                }
                Criterion c = Restrictions.and( c1, c2, c3, c4, c5, c6, c7);
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
    
    private BorderPane tablePane() {
        BorderPane pane = new BorderPane();
        pane.setPadding(Insets.EMPTY);
        
        insuranceTable.setEditable(true);
        
        Callback<TableColumn, TableCell> cellFactory = new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn p) {
                if ("outDate".equals(p.getId())
                        || "fromDate".equals(p.getId())) {
                    return new DatePickerCell();
                } else if ("commerceInsuranceAmount".equals(p.getId())
                        || "compulsoryInsuranceAmount".equals(p.getId())
                        || "usageTax".equals(p.getId())
                        || "total".equals(p.getId())
                        || "fee".equals(p.getId())
                        || "payback".equals(p.getId())
                        || "customerRebate".equals(p.getId())
                        || "income".equals(p.getId())
                        || "compulsoryInsuranceRate".equals(p.getId())
                        || "commercialInsuranceRate".equals(p.getId())) {
                    return new BigDecimalCell();
                } else if ("status".equals(p.getId())) {
                    
                    return new ComboBoxCell(new String[]{"已出单", "未出单"});
                }
                else {
                    return new EditingCell();
                }
            }
        };
        
        loadTableData(null);
        
        TableColumn No = new TableColumn("序号");
        TableColumn outDate = new TableColumn("出单日期*");
        TableColumn fromDate = new TableColumn("生效日期*");
        TableColumn customerName = new TableColumn("被保险人");
        TableColumn licensePlateNumber = new TableColumn("车牌号");
        TableColumn amount = new TableColumn("保险金额");
        TableColumn commerceInsuranceAmount = new TableColumn("商业险金额*");
        TableColumn compulsoryInsuranceAmount = new TableColumn("交强险金额*");
        TableColumn usageTax = new TableColumn("车船使用税*");
        TableColumn total = new TableColumn("合计");
        TableColumn insuranceNotes = new TableColumn("备注*");
        TableColumn seller = new TableColumn("销售员*");
        TableColumn fee = new TableColumn("手续费*");
        TableColumn payback = new TableColumn("客户保费回款*");
        TableColumn customerRebate = new TableColumn("客户返利");
        TableColumn income = new TableColumn("收入");
        TableColumn status = new TableColumn("状态*");
        TableColumn commercialInsuranceRate = new TableColumn("商业险费率*");
        TableColumn compulsoryInsuranceRate = new TableColumn("交强险费率*");
        
        amount.getColumns().addAll(commerceInsuranceAmount, compulsoryInsuranceAmount, usageTax, total);
        
        No.setId("No");
        outDate.setId("outDate");
        fromDate.setId("fromDate");
        commerceInsuranceAmount.setId("commerceInsuranceAmount");
        compulsoryInsuranceAmount.setId("compulsoryInsuranceAmount");
        usageTax.setId("usageTax");
        total.setId("total");
        fee.setId("fee");
        payback.setId("payback");
        customerRebate.setId("customerRebate");
        income.setId("income");
        status.setId("status");
        commercialInsuranceRate.setId("commercialInsuranceRate");
        compulsoryInsuranceRate.setId("compulsoryInsuranceRate");
        
        // 设置不可编辑列
        No.setEditable(false);
        customerName.setEditable(false);
        licensePlateNumber.setEditable(false);
        total.setEditable(false);
        customerRebate.setEditable(false);
        income.setEditable(false);
        
        // 设置可编辑列
        No.setCellFactory(cellFactory);
        outDate.setCellFactory(cellFactory);
        fromDate.setCellFactory(cellFactory);
        customerName.setCellFactory(cellFactory);
        licensePlateNumber.setCellFactory(cellFactory);
        commerceInsuranceAmount.setCellFactory(cellFactory);
        compulsoryInsuranceAmount.setCellFactory(cellFactory);
        usageTax.setCellFactory(cellFactory);
        total.setCellFactory(cellFactory);
        payback.setCellFactory(cellFactory);
        customerRebate.setCellFactory(cellFactory);
        income.setCellFactory(cellFactory);
        insuranceNotes.setCellFactory(cellFactory);
        seller.setCellFactory(cellFactory);
        status.setCellFactory(cellFactory);
        fee.setCellFactory(cellFactory);
        commercialInsuranceRate.setCellFactory(cellFactory);
        compulsoryInsuranceRate.setCellFactory(cellFactory);
        
        // 设置列宽
        No.setPrefWidth(50);
        outDate.setPrefWidth(120);
        fromDate.setPrefWidth(120);
        customerName.setPrefWidth(120);
        licensePlateNumber.setPrefWidth(120);
        commerceInsuranceAmount.setPrefWidth(120);
        compulsoryInsuranceAmount.setPrefWidth(120);
        usageTax.setPrefWidth(120);
        total.setPrefWidth(120);
        insuranceNotes.setPrefWidth(120);
        seller.setPrefWidth(120);
        fee.setPrefWidth(120);
        payback.setPrefWidth(120);
        customerRebate.setPrefWidth(120);
        income.setPrefWidth(120);
        status.setPrefWidth(120);
        commercialInsuranceRate.setPrefWidth(120);
        compulsoryInsuranceRate.setPrefWidth(120);
        
        No.setCellValueFactory(new PropertyValueFactory<InsuranceModel, String>("No"));
        outDate.setCellValueFactory(new PropertyValueFactory<InsuranceModel, String>("outDate"));
        fromDate.setCellValueFactory(new PropertyValueFactory<InsuranceModel, String>("fromDate")); 
        customerName.setCellValueFactory(new PropertyValueFactory<InsuranceModel, String>("name"));
        licensePlateNumber.setCellValueFactory(new PropertyValueFactory<InsuranceModel, String>("licensePlateNumber"));
        commerceInsuranceAmount.setCellValueFactory(new PropertyValueFactory<InsuranceModel, String>("commerceInsuranceAmount"));
        compulsoryInsuranceAmount.setCellValueFactory(new PropertyValueFactory<InsuranceModel, String>("compulsoryInsuranceAmount"));
        usageTax.setCellValueFactory(new PropertyValueFactory<InsuranceModel, String>("usageTax"));
        total.setCellValueFactory(new PropertyValueFactory<InsuranceModel, String>("total"));
        insuranceNotes.setCellValueFactory(new PropertyValueFactory<InsuranceModel, String>("insuranceNotes"));
        seller.setCellValueFactory(new PropertyValueFactory<InsuranceModel, String>("seller"));
        fee.setCellValueFactory(new PropertyValueFactory<InsuranceModel, String>("fee"));
        payback.setCellValueFactory(new PropertyValueFactory<InsuranceModel, String>("payback"));
        customerRebate.setCellValueFactory(new PropertyValueFactory<InsuranceModel, String>("customerRebate"));
        income.setCellValueFactory(new PropertyValueFactory<InsuranceModel, String>("income"));
        status.setCellValueFactory(new PropertyValueFactory<InsuranceModel, String>("status"));
        commercialInsuranceRate.setCellValueFactory(new PropertyValueFactory<InsuranceModel, BigDecimal>("commercialInsuranceRate"));
        compulsoryInsuranceRate.setCellValueFactory(new PropertyValueFactory<InsuranceModel, BigDecimal>("compulsoryInsuranceRate"));
        
        // 商业险费率
        commercialInsuranceRate.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<InsuranceModel, BigDecimal>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<InsuranceModel, BigDecimal> t) {
                InsuranceModel m = (InsuranceModel) t.getTableView().getItems().get(t.getTablePosition().getRow());
                if (m != totalModel) {
                    m.setCommercialInsuranceRate(t.getNewValue());
                    updateAmount(m);
                }
            }
        });
        // 交强险费率
        compulsoryInsuranceRate.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<InsuranceModel, BigDecimal>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<InsuranceModel, BigDecimal> t) {
                InsuranceModel m = (InsuranceModel) t.getTableView().getItems().get(t.getTablePosition().getRow());
                if (m != totalModel) {
                    m.setCompulsoryInsuranceRate(t.getNewValue());
                    updateAmount(m);
                }
            }
        });
        
        // 出单日期
        outDate.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<InsuranceModel, LocalDate>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<InsuranceModel, LocalDate> t) {
                InsuranceModel m = (InsuranceModel) t.getTableView().getItems().get(t.getTablePosition().getRow());
                if (m != totalModel) {
                    m.setOutDate(t.getNewValue());
                    
                    Insurance insurance = (Insurance) controller.getInsuranceByID.call(m.getId());
                    insurance.setOutDate(DateUtils.LocalDate2Date(m.getOutDate()));
                    controller.saveOrUpdateInsurance.call(insurance);
                }
            }
        });
        
        // 生效日期
        fromDate.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<InsuranceModel, LocalDate>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<InsuranceModel, LocalDate> t) {
                InsuranceModel m = (InsuranceModel) t.getTableView().getItems().get(t.getTablePosition().getRow());
                if (m != totalModel) {
                    m.setFromDate(t.getNewValue());
                    
                    Insurance insurance = (Insurance) controller.getInsuranceByID.call(m.getId());
                    insurance.setFromDate(DateUtils.LocalDate2Date(m.getFromDate()));
                    controller.saveOrUpdateInsurance.call(insurance);
                }
            }
        });
        
        // 商业险金额
        commerceInsuranceAmount.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<InsuranceModel, BigDecimal>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<InsuranceModel, BigDecimal> t) {
                InsuranceModel m = (InsuranceModel) t.getTableView().getItems().get(t.getTablePosition().getRow());
                if (m != totalModel) {
                    m.setCommerceInsuranceAmount(t.getNewValue());
                    updateAmount(m);
                }
            }
        });
        // 交强险金额
        compulsoryInsuranceAmount.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<InsuranceModel, BigDecimal>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<InsuranceModel, BigDecimal> t) {
                InsuranceModel m = (InsuranceModel) t.getTableView().getItems().get(t.getTablePosition().getRow());
                if (m != totalModel) {
                    m.setCompulsoryInsuranceAmount(t.getNewValue());
                    
                    updateAmount(m);
                }
            }
        });
        // 车船税
        usageTax.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<InsuranceModel, BigDecimal>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<InsuranceModel, BigDecimal> t) {
                InsuranceModel m = (InsuranceModel) t.getTableView().getItems().get(t.getTablePosition().getRow());
                if (m != totalModel) {
                    m.setUsageTax(t.getNewValue());
                    
                    updateAmount(m);
                }
            }
        });
        // 客户保费回款
        payback.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<InsuranceModel, BigDecimal>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<InsuranceModel, BigDecimal> t) {
                InsuranceModel m = (InsuranceModel) t.getTableView().getItems().get(t.getTablePosition().getRow());
                if (m != totalModel) {
                    m.setPayback(t.getNewValue());
                    
                    updateAmount(m);
                }
            }
        });
        
        insuranceNotes.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<InsuranceModel, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<InsuranceModel, String> t) {
                InsuranceModel m = (InsuranceModel) t.getTableView().getItems().get(t.getTablePosition().getRow());
                if (m != totalModel) {
                    m.setInsuranceNotes(t.getNewValue());
                    
                    Insurance insurance = (Insurance) controller.getInsuranceByID.call(m.getId());
                    insurance.setInsuranceNotes(m.getInsuranceNotes());
                    controller.saveOrUpdateInsurance.call(insurance);
                }
            }
        });
        
        seller.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<InsuranceModel, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<InsuranceModel, String> t) {
                InsuranceModel m = (InsuranceModel) t.getTableView().getItems().get(t.getTablePosition().getRow());
                if (m != totalModel) {
                    m.setSeller(t.getNewValue());
                    
                    Insurance insurance = (Insurance) controller.getInsuranceByID.call(m.getId());
                    insurance.setSeller(m.getSeller());
                    controller.saveOrUpdateInsurance.call(insurance);
                }
            }
        });
        
        status.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<InsuranceModel, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<InsuranceModel, String> t) {
                InsuranceModel m = (InsuranceModel) t.getTableView().getItems().get(t.getTablePosition().getRow());
                if (m != totalModel) {
                    m.setStatus(t.getNewValue());
                    Insurance insurance = (Insurance) controller.getInsuranceByID.call(m.getId());
                    insurance.setStatus(m.getStatus());
                    controller.saveOrUpdateInsurance.call(insurance);
                }
            }
        });
        
        fee.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<InsuranceModel, BigDecimal>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<InsuranceModel, BigDecimal> t) {
                InsuranceModel m = (InsuranceModel) t.getTableView().getItems().get(t.getTablePosition().getRow());
                if (m != totalModel) {
                    m.setFee(t.getNewValue());
                    m.setIncome(m.getFee().subtract(m.getCustomerRebate()));
                    
                    Insurance insurance = (Insurance) controller.getInsuranceByID.call(m.getId());
                    insurance.setFee(m.getFee());
                    insurance.setIncome(m.getIncome());
                    
                    controller.saveOrUpdateInsurance.call(insurance);
                    
                    insuranceTable.refresh();
                }
            }
        });
        
        insuranceTable.setItems(data);
        insuranceTable.getColumns().addAll(No, commercialInsuranceRate, compulsoryInsuranceRate, outDate, fromDate, customerName, licensePlateNumber, amount, insuranceNotes, seller, fee, payback, customerRebate, income, status);
        insuranceTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY );
        pane.setCenter(insuranceTable);
        
        insuranceTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                disable(false);
            }
        });
        
//        // 表格双击
//        insuranceTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent t) {
//                
//                if (t.getClickCount() == 2) {
//                    if (data.size() > 0) {
//                        TableColumn col = getTableColumnByName(insuranceTable, "商业险金额*");
////                        if (col != null) {
////                            InsuranceModel m = (InsuranceModel) insuranceTable.getSelectionModel().getSelectedItem();
////                            InsuranceSettingsDialog dia = new InsuranceSettingsDialog(m.getId());
////                            dia.selectTab(1);
////                            dia.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> dia.saveSettings(m2 -> updateAmount(m2)));
////                        }
//                    }
//                }
//            }
//        });
        
        ContextMenu menu = new ContextMenu();
        
        MenuItem items = new MenuItem("设置商业险项目");
        menu.getItems().addAll(items);
        
        items.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                showInsuranceSettingsDialog();
            }
        });
        
        insuranceTable.setContextMenu(menu);
        
        return pane;
    }
    
    private void showInsuranceSettingsDialog() {
        InsuranceModel m = (InsuranceModel) insuranceTable.getSelectionModel().getSelectedItem();
        if (m != totalModel) {
            Insurance editInsurance = (Insurance) controller.getInsuranceByID.call(m.getId());
            InsuranceSettingsDialog dia = new InsuranceSettingsDialog(editInsurance);
            dia.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> dia.saveSettings());
        }
    }
    
    private <T> TableColumn<T, ?> getTableColumnByName(TableView<T> tableView, String name) {
        for (TableColumn<T, ?> col : tableView.getColumns())
            if (col.getText().equals(name)) return col ;
        return null ;
    }
    
    private void update() {
        InsuranceModel selectedModel = (InsuranceModel) insuranceTable.getSelectionModel().getSelectedItem();
        if (selectedModel != totalModel) {
            Customer customer = (Customer) controller.getCustomerByID.call(selectedModel.getCustomer().getId());
            
            AddOrUpdateCustomerDialog dia = new AddOrUpdateCustomerDialog(customer);
            dia.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> dia.saveOrUpdateCustomer(c -> updateInsuranceRow(c)));
        }
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
            
            c = Restrictions.between("outDate", cal.getTime(), new Date());
        }
        
        List<Insurance> lstInsurance = (List<Insurance>) controller.lstInsurance.call(c);
        BigDecimal totalFee = BigDecimal.ZERO;
        BigDecimal totalCustomerRebate = BigDecimal.ZERO;
        BigDecimal totalIncomeAmount = BigDecimal.ZERO;
        int num = 1;
        for (Insurance i : lstInsurance) {
            InsuranceModel model = new InsuranceModel();
            BeanUtils.copyProperties(i, model);
            
            model.setOutDate(DateUtils.Date2LocalDate(i.getOutDate()));
            model.setFromDate(DateUtils.Date2LocalDate(i.getFromDate()));
            model.setCommercialInsuranceRate(new BigDecimal(i.getCommercialInsuranceRate()));
            model.setCompulsoryInsuranceRate(new BigDecimal(i.getCompulsoryInsuranceRate()));
            
            totalFee = totalFee.add(i.getFee());
            totalCustomerRebate = totalCustomerRebate.add(i.getCustomerRebate());
            totalIncomeAmount = totalIncomeAmount.add(i.getIncome().setScale(2, RoundingMode.HALF_UP));
            model.setNo(""+(num++));
            data.add(model);
        }
        
        totalModel.setNo("合计:");
        totalModel.setFee(totalFee);
        totalModel.setCustomerRebate(totalCustomerRebate);
        totalModel.setIncome(totalIncomeAmount);
        data.add(totalModel);
        
        disable(true);
    }
    
    public void updateAmount(InsuranceModel m) {
        Insurance insurance = (Insurance) controller.getInsuranceByID.call(m.getId());
        BigDecimal v1 = m.getCommerceInsuranceAmount();
        BigDecimal v2 = m.getCompulsoryInsuranceAmount();
        BigDecimal v3 = m.getUsageTax();
        BigDecimal p = m.getPayback();
        // 商业险费率
        BigDecimal rate1 = m.getCommercialInsuranceRate();
        // 交强险费率
        BigDecimal rate2 = m.getCompulsoryInsuranceRate();
        // 合计
        m.setTotal((v1.add(v2).add(v3)));
        // 手续费
        BigDecimal v4 = v1.multiply(rate1);
        BigDecimal v5 = v2.multiply(rate2);
        m.setFee((v4.add(v5)).setScale(2, RoundingMode.HALF_UP));
        // 客户返利
        m.setCustomerRebate((m.getTotal().subtract(p)).setScale(2, RoundingMode.HALF_UP));
        // 收入
        m.setIncome((m.getFee().subtract(m.getCustomerRebate())).setScale(2, RoundingMode.HALF_UP));
        
        BeanUtils.copyProperties(m, insurance);
        
        insurance.setCommercialInsuranceRate(rate1.toString());
        insurance.setCompulsoryInsuranceRate(rate2.toString());
        
        controller.saveOrUpdateInsurance.call(insurance);
        
        updateTotalAmount();
        
        insuranceTable.refresh();
    }
    
    private void updateTotalAmount() {
        BigDecimal totalFee = BigDecimal.ZERO;
        BigDecimal totalCustomerRebate = BigDecimal.ZERO;
        BigDecimal totalIncomeAmount = BigDecimal.ZERO;
        int num = 1;
        for (InsuranceModel i : data) {
            if (i != totalModel) {
                i.setNo(""+(num++));
                totalFee = totalFee.add(i.getFee());
                totalCustomerRebate = totalCustomerRebate.add(i.getCustomerRebate());
                totalIncomeAmount = totalIncomeAmount.add(i.getIncome().setScale(2, RoundingMode.HALF_UP));
            }
        }
        totalModel.setNo("合计：");
        totalModel.setFee(totalFee);
        totalModel.setCustomerRebate(totalCustomerRebate);
        totalModel.setIncome(totalIncomeAmount);
    }
    
    private void disable(boolean b) {
        btnUpdate.setDisable(b);
        btnRemove.setDisable(b);
        btnSetting.setDisable(b);
    }
    
    private void insertInsuranceRow(Customer customer) {
        Map<String, String> parameters = (Map<String, String>) controller.listAllParameters.call("");
        data.remove(totalModel);
        controller.saveOrUpdateCustomer.call(customer);
        
        Insurance insurance = new Insurance();
        BeanUtils.copyProperties(customer, insurance);
        insurance.setId(null);
        insurance.setCustomer(customer);
        String rate1 = StringUtils.isEmpty(parameters.get("COMMERCIALINSURANCERATE")) ? "0" : parameters.get("COMMERCIALINSURANCERATE");
        String rate2 = StringUtils.isEmpty(parameters.get("COMPULSORYINSURANCERATE")) ? "0" : parameters.get("COMPULSORYINSURANCERATE");
        insurance.setCommercialInsuranceRate(rate1);
        insurance.setCompulsoryInsuranceRate(rate2);
        insurance.setCommerceInsuranceAmount(BigDecimal.ZERO);
        insurance.setCompulsoryInsuranceAmount(BigDecimal.ZERO);
        insurance.setUsageTax(BigDecimal.ZERO);
        insurance.setTotal(BigDecimal.ZERO);
        insurance.setFee(BigDecimal.ZERO);
        insurance.setPayback(BigDecimal.ZERO);
        insurance.setCustomerRebate(BigDecimal.ZERO);
        insurance.setIncome(BigDecimal.ZERO);
        insurance.setStatus("未出单");
        insurance.setOutDate(new Date());
        insurance.setFromDate(new Date());
        
        controller.saveOrUpdateInsurance.call(insurance);
        
        InsuranceModel im = new InsuranceModel();
        BeanUtils.copyProperties(insurance, im);
        im.setCommercialInsuranceRate(new BigDecimal(rate1));
        im.setCompulsoryInsuranceRate(new BigDecimal(rate2));
        
        data.add(im);
        data.add(totalModel);
        updateTotalAmount();
    }
    
    private void updateInsuranceRow(Customer customer) {
        InsuranceModel insuranceModel = (InsuranceModel) insuranceTable.getSelectionModel().getSelectedItem();
        
        Insurance insurance = (Insurance) controller.getInsuranceByID.call(insuranceModel.getId());
        insurance.setName(customer.getName());
        insurance.setLicensePlateNumber(customer.getLicensePlateNumber());
        
        controller.saveOrUpdateInsurance.call(insurance);
    
        loadTableData(null);
    }

    private void export() {
        FileChooser fc = new FileChooser();
        fc.setInitialFileName(DateUtils.Date2String(new Date())+".xls");
        File selectedFile = fc.showSaveDialog(MainApp.getInstance().getPrimaryStage());
        if (selectedFile != null) {
            try {
                Excel.getInstance().exportInsurance(insuranceTable, selectedFile.getAbsolutePath());
            } catch (IOException ex) {
                org.apache.logging.log4j.Logger logger = LogManager.getLogger(InsuranceTabContent.class.getName());
                logger.error(ex);
                MessageBox.showMessage("由于文件被占用无法导出。");
            }
        }
    }

    private Button btnChoose = new Button("选择客户");
    private Button btnAdd = new Button("添加");
    private Button btnUpdate = new Button("更改");
    private Button btnRemove = new Button("删除");
    private Button btnSetting = new Button("设置商业险项目");
    private Button btnExport = new Button("导出", ImageUtils.createImageView("future_projects_16px_505023_easyicon.net.png"));
    private TableView insuranceTable = new TableView();
    private final ObservableList<InsuranceModel> data = FXCollections.observableArrayList();
    
    private TextField name = new TextField();
    private TextField licensePlateNumberField = new TextField();
    private ComboBox<String> statusField = new ComboBox<String>();
    private DatePicker searchFrom = new DatePicker();
    private DatePicker searchTo = new DatePicker();
    private DatePicker fromField = new DatePicker();
    private DatePicker toField = new DatePicker();
    private TextField notesField = new TextField();
    private TextField sellerField = new TextField();
    private Button btnSearch = new Button("查询");
    private Button btnBackAll = new Button("返回");
    private InsuranceTabContentController controller = new InsuranceTabContentController();
    private InsuranceModel totalModel = new InsuranceModel();
}