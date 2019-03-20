/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.ui;

import com.dockingsoftware.autorepairsystem.component.model.Inventory;
import com.dockingsoftware.autorepairsystem.Constants;
import com.dockingsoftware.autorepairsystem.MainApp;
import com.dockingsoftware.autorepairsystem.component.MessageBox;
import com.dockingsoftware.autorepairsystem.component.TreeItemExt;
import com.dockingsoftware.autorepairsystem.common.Excel;
import com.dockingsoftware.autorepairsystem.persistence.entity.Settlement;
import com.dockingsoftware.autorepairsystem.persistence.entity.SettlementDetails;
import com.dockingsoftware.autorepairsystem.ui.controller.SettlementManagementTabContentController;
import com.dockingsoftware.autorepairsystem.util.DateUtils;
import com.dockingsoftware.autorepairsystem.util.ImageUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
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

/**
 * 结算单管理
 * 
 * @author Shunyi Chen
 */
public class SettlementManagementTabContent extends VBox {

    public SettlementManagementTabContent() {
        initComponents();
    }
    
    private void initComponents() {
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
        
        btnStatement.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                settle();
            }
        });
        
        btnExport.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                export();
            }
        });

        ToolBar bar = new ToolBar();
        disable(true);
        bar.getItems().addAll(btnAdd, btnUpdate, btnRemove, new Separator(), btnStatement, new Separator(), btnExport);

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
        grid.add(fromDate, 3, 0);
        
        grid.add(new Label("到:"), 4, 0);
        grid.add(toDate, 5, 0);
        
        grid.add(new Label("客户名称:"), 0, 1);
        clientNameField.setPromptText("客户名称");
        grid.add(clientNameField, 1, 1);
        
        grid.add(new Label("客户电话:"), 2, 1);
        phoneNoField.setPromptText("客户电话");
        grid.add(phoneNoField, 3, 1);
        
        grid.add(new Label("车牌号:"), 4, 1);
        licensePlateNumberField.setPromptText("车牌号");
        grid.add(licensePlateNumberField, 5, 1);
        
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
                String snVal = SNField.getText();
                String clientNameVal = clientNameField.getText();
                String phoneNoVal = phoneNoField.getText();
                String licensePlateNumberVal = licensePlateNumberField.getText();
                
                Criterion c1 = Restrictions.ne("id", "0");
                if (!clientNameVal.isEmpty()) {
                    c1 = Restrictions.ilike("clientName", clientNameVal, MatchMode.ANYWHERE);
                }
                Criterion c2 = Restrictions.ne("id", "0");
                if (!phoneNoVal.isEmpty()) {
                    c2 = Restrictions.ilike("phoneNo", phoneNoVal, MatchMode.ANYWHERE);
                }
                Criterion c3 = Restrictions.ne("id", "0");
                if (!licensePlateNumberVal.isEmpty()) {
                    c3 = Restrictions.ilike("licensePlateNumber", licensePlateNumberVal, MatchMode.ANYWHERE);
                }
                Criterion c4 = Restrictions.ne("id", "0");
                if (!snVal.isEmpty()) {
                    c4 = Restrictions.ilike("SN", snVal, MatchMode.ANYWHERE);
                }
 
                Criterion c5 = Restrictions.ne("id", "0");
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

                    c5 = Restrictions.between("billingDate", cal.getTime(), cal2.getTime());
                }
                Criterion c = Restrictions.and(c1, c2, c3, c4, c5);
                
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
        
        final TreeTableColumn<Inventory, String> snColumn = new TreeTableColumn<>("单号");
        snColumn.setEditable(false);
        snColumn.setPrefWidth(130);
        snColumn.setCellValueFactory(new TreeItemPropertyValueFactory("SN"));
        
        final TreeTableColumn<Inventory, String> billingObjectColumn = new TreeTableColumn<>("开单对象");
        billingObjectColumn.setEditable(false);
        billingObjectColumn.setPrefWidth(120);
        billingObjectColumn.setCellValueFactory(new TreeItemPropertyValueFactory("billingObject"));
        
        final TreeTableColumn<Inventory, String> billingDateColumn = new TreeTableColumn<>("开单日期");
        billingDateColumn.setEditable(false);
        billingDateColumn.setPrefWidth(120);
        billingDateColumn.setCellValueFactory(new TreeItemPropertyValueFactory("billingDate"));
        
        final TreeTableColumn<Inventory, String> customerNameColumn = new TreeTableColumn<>("客户名");
        customerNameColumn.setEditable(false);
        customerNameColumn.setPrefWidth(120);
        customerNameColumn.setCellValueFactory(new TreeItemPropertyValueFactory("clientName"));
        
        final TreeTableColumn<Inventory, String> phoneNoColumn = new TreeTableColumn<>("客户电话");
        phoneNoColumn.setEditable(false);
        phoneNoColumn.setPrefWidth(120);
        phoneNoColumn.setCellValueFactory(new TreeItemPropertyValueFactory("phoneNo"));
        
        final TreeTableColumn<Inventory, String> licensePlateNumberColumn = new TreeTableColumn<>("车牌号");
        licensePlateNumberColumn.setEditable(false);
        licensePlateNumberColumn.setPrefWidth(120);
        licensePlateNumberColumn.setCellValueFactory(new TreeItemPropertyValueFactory("licensePlateNumber"));
        
        final TreeTableColumn<Inventory, String> contactsColumn = new TreeTableColumn<>("联系人");
        contactsColumn.setEditable(false);
        contactsColumn.setPrefWidth(120);
        contactsColumn.setCellValueFactory(new TreeItemPropertyValueFactory("contacts"));
        
        final TreeTableColumn<Inventory, String> notesColumn = new TreeTableColumn<>("备注");
        notesColumn.setEditable(false);
        notesColumn.setPrefWidth(120);
        notesColumn.setCellValueFactory(new TreeItemPropertyValueFactory("notes"));
        
        final TreeTableColumn<Inventory, String> discountAmountColumn = new TreeTableColumn<>("优惠金额");
        discountAmountColumn.setEditable(false);
        discountAmountColumn.setPrefWidth(120);
        discountAmountColumn.setCellValueFactory(new TreeItemPropertyValueFactory("discountAmount"));
        
        final TreeTableColumn<Inventory, String> discountReasonColumn = new TreeTableColumn<>("优惠原因");
        discountReasonColumn.setEditable(false);
        discountReasonColumn.setPrefWidth(120);
        discountReasonColumn.setCellValueFactory(new TreeItemPropertyValueFactory("discountReason"));
        
        final TreeTableColumn<Inventory, String> receivableAmountColumn = new TreeTableColumn<>("应收金额");
        receivableAmountColumn.setEditable(false);
        receivableAmountColumn.setPrefWidth(120);
        receivableAmountColumn.setCellValueFactory(new TreeItemPropertyValueFactory("receivableAmount"));
        
        final TreeTableColumn<Inventory, String> actuallyPayColumn = new TreeTableColumn<>("实际支付");
        actuallyPayColumn.setEditable(false);
        actuallyPayColumn.setPrefWidth(120);
        actuallyPayColumn.setCellValueFactory(new TreeItemPropertyValueFactory("actuallyPay"));
        
        final TreeTableColumn<Inventory, String> amountReceivedColumn = new TreeTableColumn<>("已收金额");
        amountReceivedColumn.setEditable(false);
        amountReceivedColumn.setPrefWidth(120);
        amountReceivedColumn.setCellValueFactory(new TreeItemPropertyValueFactory("amountReceived"));
        
        final TreeTableColumn<Inventory, String> owingAmountColumn = new TreeTableColumn<>("尚欠金额");
        owingAmountColumn.setEditable(false);
        owingAmountColumn.setPrefWidth(120);
        owingAmountColumn.setCellValueFactory(new TreeItemPropertyValueFactory("owingAmount"));
        
        final TreeTableColumn<Inventory, String> settlementDateColumn = new TreeTableColumn<>("结算日期");
        settlementDateColumn.setEditable(false);
        settlementDateColumn.setPrefWidth(120);
        settlementDateColumn.setCellValueFactory(new TreeItemPropertyValueFactory("settlementDate"));
        
        final TreeTableColumn<Inventory, String> paymentColumn = new TreeTableColumn<>("支付方式");
        paymentColumn.setEditable(false);
        paymentColumn.setPrefWidth(120);
        paymentColumn.setCellValueFactory(new TreeItemPropertyValueFactory("payment"));
        
        final TreeTableColumn<Inventory, String> settlementNotesColumn = new TreeTableColumn<>("结算备注");
        settlementNotesColumn.setEditable(false);
        settlementNotesColumn.setPrefWidth(120);
        settlementNotesColumn.setCellValueFactory(new TreeItemPropertyValueFactory("settlementNotes"));
        
        final TreeTableColumn<Inventory, String> businessStateColumn = new TreeTableColumn<>("业务状态");
        businessStateColumn.setEditable(false);
        businessStateColumn.setPrefWidth(120);
        businessStateColumn.setCellValueFactory(new TreeItemPropertyValueFactory("businessState"));
        
        final TreeTableColumn<Inventory, String> settlementStateColumn = new TreeTableColumn<>("结算状态");
        settlementStateColumn.setEditable(false);
        settlementStateColumn.setPrefWidth(120);
        settlementStateColumn.setCellValueFactory(new TreeItemPropertyValueFactory("settlementState"));
        
        loadTableData(null);
        
        treeTableView = new TreeTableView(rootItem);
        
        treeTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                disable(false);
            }
        });
        
        // 表格双击
        treeTableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (t.getClickCount() == 2) {
                    update();
                }
            }
        });
        
        ContextMenu menu = new ContextMenu();
        menu.getItems().addAll(expandOrContractAll, new SeparatorMenuItem(), itemAdd, itemUpdate, itemRemove, new SeparatorMenuItem(), itemStatement, new SeparatorMenuItem(), itemExport);
        itemAdd.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                add();
            }
        });
        itemUpdate.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                update();
            }
        });
        itemRemove.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                remove();
            }
        });
        itemStatement.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                settle();
            }
        });
        itemExport.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                export();
            }
        });
        expandOrContractAll.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                if (!expandFlag) {
                    expandAll();
                } else {
                    contractAll();
                }
            }
        });
        treeTableView.setContextMenu(menu);
        treeTableView.showRootProperty().setValue(false);
        treeTableView.setEditable(true);
        treeTableView.setPrefSize(430, 200);
        treeTableView.getColumns().setAll(snColumn, billingDateColumn, customerNameColumn, phoneNoColumn, licensePlateNumberColumn, 
                discountAmountColumn, discountReasonColumn, receivableAmountColumn, actuallyPayColumn, amountReceivedColumn, owingAmountColumn,
                settlementDateColumn, paymentColumn, settlementNotesColumn, businessStateColumn, settlementStateColumn, billingObjectColumn, contactsColumn,  notesColumn);
        
//        int rowsPerPage = 30;
//        Pagination pagination = new Pagination((rootItem.getChildren().size() / rowsPerPage + 1), 0); 
        
        pane.setCenter(treeTableView);
//        pane.setBottom(pagination);
        
        return pane;
    }
    
    private void loadTableData(Criterion c) {
        criterion = c;
        
        rootItem.getChildren().clear();
        rootItem.setExpanded(true);
        
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
        
        List<Settlement> lstSettlement = (List<Settlement>) controller.listSettlement.call(c);
        for (Settlement s : lstSettlement) {
            if (Constants.OBJ_2.equals(s.getBillingObject())) {
                TreeItem<Inventory> item = new TreeItem<>(new Inventory(
                    s.getId(),
                    s.getSN(),
                    s.getBillingObject(),
                    DateUtils.Date2String(s.getBillingDate()),
                    s.getClientName(),
                    s.getPhoneNo(),
                    s.getLicensePlateNumber(),
                    s.getContacts(),
                    "", "", "", "", "", "", "", "", "", "", "", ""
                ));
                
                List<SettlementDetails> subItems = s.getDetails();
                for (SettlementDetails sd : subItems) {
                    TreeItem<Inventory> subItem = new TreeItem<>(new Inventory(
                            sd.getId(),
                            sd.getSN(),
                            Constants.OBJ_3,
                            DateUtils.Date2String(sd.getBillingDate()),
                            sd.getCustomerName(),
                            sd.getPhoneNo(),
                            sd.getLicensePlateNumber(),
                            "",
                            sd.getNotes(),
                            sd.getDiscountAmount().toString(),
                            sd.getDiscountReason(),
                            sd.getReceivableAmount().toString(),
                            sd.getActuallyPay().toString(),
                            sd.getAmountReceived().toString(),
                            sd.getOwingAmount().toString(),
                            sd.getSettlementDate() == null ? "" : DateUtils.Date2String(sd.getSettlementDate()),
                            sd.getPayment(),
                            sd.getSettlementNotes(),
                            sd.getBusinessState(),
                            sd.getSettlementState()
                    ));
                    item.getChildren().add(subItem);
                }
                rootItem.getChildren().add(item);
            } else {
                SettlementDetails sd = s.getDetails().get(0);
                TreeItem<Inventory> item = new TreeItem<>(new Inventory(
                    s.getId(),
                    s.getSN(),
                    s.getBillingObject(),
                    DateUtils.Date2String(s.getBillingDate()),
                    s.getClientName(),
                    s.getPhoneNo(),
                    s.getLicensePlateNumber(),
                    s.getContacts(),
                    sd.getNotes(),
                    sd.getDiscountAmount().toString(),
                    sd.getDiscountReason(),
                    sd.getReceivableAmount().toString(),
                    sd.getActuallyPay().toString(),
                    sd.getAmountReceived().toString(), 
                    sd.getOwingAmount().toString(),
                    sd.getSettlementDate() == null ? "" : DateUtils.Date2String(sd.getSettlementDate()),
                    sd.getPayment(),
                    sd.getSettlementNotes(),
                    sd.getBusinessState(),
                    sd.getSettlementState()
                ));
                rootItem.getChildren().add(item);
            }
        }
        disable(true);
    }
    
    private void add() {
        TreeItemExt tix = MainApp.getInstance().getNavigationBar().getItemWXKD();
        ChooseOrderTypeDialog dia = new ChooseOrderTypeDialog();
        dia.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response ->  dia.isNormalCustomer(bool ->  TabContentUI.getInstance().addTab(tix, bool)));
    }
    
    private void update() {
        TreeItem<Inventory> selectedItem = (TreeItem<Inventory>) treeTableView.getSelectionModel().getSelectedItem();
        Inventory inv = selectedItem.getValue();
        if (inv.billingObjectProperty().get().equals(Constants.OBJ_1)) {
            // 刷新节点ID
            TreeItemExt tix = MainApp.getInstance().getNavigationBar().getItemWXKD();
            Tab tab = TabContentUI.getInstance().addTab(tix, true);
            tab.setText(inv.SNProperty().get());
            
            NormalCustomerBillingTabContent content = (NormalCustomerBillingTabContent) tab.getContent();
            content.getPane().edit(inv.idProperty().get());
        } else if (inv.billingObjectProperty().get().equals(Constants.OBJ_2)) {
            TreeItemExt tix = MainApp.getInstance().getNavigationBar().getItemWXKD();
            Tab tab = TabContentUI.getInstance().addTab(tix, false);
            tab.setText(inv.SNProperty().get());
            
            FactoryBillingTabContent content = (FactoryBillingTabContent) tab.getContent();
            content.edit(inv.idProperty().get());
        } else {
            SettlementDetails details = (SettlementDetails) controller.getSettlementDetailsById.call(inv.idProperty().get());
            SettlementDetailsDialog dia = new SettlementDetailsDialog(details);
            dia.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> dia.updateSettlementDetails(sd -> loadTableData(criterion)));
        }
    }
    
    private void remove() {
        
        Callback callback = new Callback() {
            @Override
            public Object call(Object param) {
                TreeItem<Inventory> selectedItem = (TreeItem<Inventory>) treeTableView.getSelectionModel().getSelectedItem();
                Inventory inv = selectedItem.getValue();
                
                if (inv.billingObjectProperty().get().equals(Constants.OBJ_3)) {
                    Settlement deleteSettlement = (Settlement) controller.getSettlementById.call(selectedItem.getParent().getValue().idProperty().get());
                    Iterator<SettlementDetails> iter = deleteSettlement.getDetails().iterator();
                    while (iter.hasNext()) {
                        SettlementDetails sd = iter.next();
                        if (sd.getId().equals(selectedItem.getValue().idProperty().get())) {
                            iter.remove();
                        }
                    }
                    controller.saveOrUpdateSettlement.call(deleteSettlement);
                    selectedItem.getParent().getChildren().remove(selectedItem);
                } else {
                    Settlement deleteSettlement = (Settlement) controller.getSettlementById.call(inv.idProperty().get());
                    controller.deleteSettlement.call(deleteSettlement);
                    rootItem.getChildren().remove(selectedItem);
                    if (rootItem.getChildren().size() == 0) {
                        disable(true);
                    }
                }
                return "";
            }
        };
        MessageBox.showMessage("请确认是否删除该行记录。", Alert.AlertType.CONFIRMATION, callback);
    }
    
    private void settle() {
        TreeItem<Inventory> selectedItem = (TreeItem<Inventory>) treeTableView.getSelectionModel().getSelectedItem();
        Inventory inv = selectedItem.getValue();
        List<SettlementDetails> lstDetails = new ArrayList<SettlementDetails>();
        if (inv.billingObjectProperty().get().equals(Constants.OBJ_1)) {
            Settlement s = (Settlement) controller.getSettlementById.call(inv.idProperty().get());
            lstDetails.addAll(s.getDetails());
        } else if (inv.billingObjectProperty().get().equals(Constants.OBJ_2)) {
            Settlement s = (Settlement) controller.getSettlementById.call(inv.idProperty().get());
            lstDetails.addAll(s.getDetails());
        } else {
            SettlementDetails sd = (SettlementDetails) controller.getSettlementDetailsById.call(inv.idProperty().get());
            lstDetails.add(sd);
        }

        SettlementDialog dia = new SettlementDialog(lstDetails);
        dia.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> dia.pay(null));
    }
    
    private void export() {
        FileChooser fc = new FileChooser();
        fc.setInitialFileName(DateUtils.Date2String(new Date())+".xls");
        File selectedFile = fc.showSaveDialog(MainApp.getInstance().getPrimaryStage());
        if (selectedFile != null) {
            try {
                Excel.getInstance().exportSettlements(treeTableView, selectedFile.getAbsolutePath());
            } catch (IOException ex) {
                org.apache.logging.log4j.Logger logger = LogManager.getLogger(SettlementManagementTabContent.class.getName());
                logger.error(ex);
                MessageBox.showMessage("由于文件被占用无法导出。");
            }
        }
    }
    
    private void expandAll() {
        expandOrContractAll.setText("收缩全部");
        for (TreeItem item : rootItem.getChildren()) {
            item.expandedProperty().setValue(Boolean.TRUE);
        }
        expandFlag = true;
    }
    
    private void contractAll() {
        expandOrContractAll.setText("展开全部");
        for (TreeItem item : rootItem.getChildren()) {
            item.expandedProperty().setValue(Boolean.FALSE);
        }
        expandFlag = false;
    }
    
    private void disable(boolean b) {
        btnUpdate.setDisable(b);
        btnRemove.setDisable(b);
        btnStatement.setDisable(b);
        itemUpdate.setDisable(b);
        itemRemove.setDisable(b);
        itemStatement.setDisable(b);
    }
    
    private Button btnAdd = new Button("开单", ImageUtils.createImageView("billing_16px_504987_easyicon.net.png"));
    private Button btnUpdate = new Button("更改", ImageUtils.createImageView("edit_16px_505013_easyicon.net.png"));
    private Button btnRemove = new Button("删除", ImageUtils.createImageView("library_16px_505031_easyicon.net.png"));
    private Button btnStatement = new Button("结算", ImageUtils.createImageView("payment_card_16px_505044_easyicon.net.png"));
    private Button btnExport = new Button("导出", ImageUtils.createImageView("future_projects_16px_505023_easyicon.net.png"));
    private MenuItem expandOrContractAll = new MenuItem("展开全部", ImageUtils.createImageView("premium_16px_505051_easyicon.net.png"));
    private MenuItem itemAdd = new MenuItem("开单", ImageUtils.createImageView("billing_16px_504987_easyicon.net.png"));
    private MenuItem itemUpdate = new MenuItem("更改", ImageUtils.createImageView("edit_16px_505013_easyicon.net.png"));
    private MenuItem itemRemove = new MenuItem("删除", ImageUtils.createImageView("library_16px_505031_easyicon.net.png"));
    private MenuItem itemStatement = new MenuItem("结算", ImageUtils.createImageView("payment_card_16px_505044_easyicon.net.png"));
    private MenuItem itemExport = new MenuItem("导出", ImageUtils.createImageView("future_projects_16px_505023_easyicon.net.png"));
    
    private TextField phoneNoField = new TextField();
    private TextField clientNameField = new TextField();
    private TextField licensePlateNumberField = new TextField();
    private TextField SNField = new TextField();
    private DatePicker fromDate = new DatePicker();
    private DatePicker toDate = new DatePicker();
    
    private Button btnQuery = new Button("查询");
    private Button btnBackAll = new Button("返回");
    private TreeItem<Inventory> rootItem = new TreeItem<>(new Inventory("Root","", "", "", "", "", "", "", "", "", "","", "", "", "", "", "", "", "", ""));
    private TreeTableView treeTableView;
    private Criterion criterion;
    private boolean expandFlag = false;
    private SettlementManagementTabContentController controller = new SettlementManagementTabContentController();
    
}
