/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dockingsoftware.autorepairsystem.ui;

import com.dockingsoftware.autorepairsystem.MainApp;
import com.dockingsoftware.autorepairsystem.common.Excel;
import com.dockingsoftware.autorepairsystem.component.MessageBox;
import com.dockingsoftware.autorepairsystem.component.model.Rent;
import com.dockingsoftware.autorepairsystem.persistence.entity.Rental;
import com.dockingsoftware.autorepairsystem.persistence.entity.TenantCar;
import com.dockingsoftware.autorepairsystem.ui.controller.RentalManagementTabContentController;
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

public class RentalManagementTabContent extends VBox {

    /**
     * Constructor.
     *
     */
    public RentalManagementTabContent() {
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
        bar.getItems().addAll(btnAdd, btnUpdate, btnRemove, new Separator(), btnStatement, new Separator(), btnExport);
        return bar;
    }

    private BorderPane createTablePane() {
        BorderPane pane = new BorderPane();
        pane.setPadding(Insets.EMPTY);

        final TreeTableColumn<Rent, String> snColumn = new TreeTableColumn<>("单号");
        snColumn.setEditable(false);
        snColumn.setPrefWidth(130);
        snColumn.setCellValueFactory(new TreeItemPropertyValueFactory("SN"));

        final TreeTableColumn<Rent, String> billingDateColumn = new TreeTableColumn<>("开单日期");
        billingDateColumn.setEditable(false);
        billingDateColumn.setPrefWidth(120);
        billingDateColumn.setCellValueFactory(new TreeItemPropertyValueFactory("billingDate"));
        
        final TreeTableColumn<Rent, String> customerNameColumn = new TreeTableColumn<>("租户");
        customerNameColumn.setEditable(false);
        customerNameColumn.setPrefWidth(120);
        customerNameColumn.setCellValueFactory(new TreeItemPropertyValueFactory("tenantName"));
        
        final TreeTableColumn<Rent, String> phoneNoColumn = new TreeTableColumn<>("联系电话");
        phoneNoColumn.setEditable(false);
        phoneNoColumn.setPrefWidth(120);
        phoneNoColumn.setCellValueFactory(new TreeItemPropertyValueFactory("phoneNo"));
        
        final TreeTableColumn<Rent, String> licensePlateNumberColumn = new TreeTableColumn<>("车牌号");
        licensePlateNumberColumn.setEditable(false);
        licensePlateNumberColumn.setPrefWidth(120);
        licensePlateNumberColumn.setCellValueFactory(new TreeItemPropertyValueFactory("licensePlateNumber"));
        
        final TreeTableColumn<Rent, String> paintPartsColumn = new TreeTableColumn<>("烤漆部位");
        paintPartsColumn.setEditable(false);
        paintPartsColumn.setPrefWidth(120);
        paintPartsColumn.setCellValueFactory(new TreeItemPropertyValueFactory("paintParts"));
        
        final TreeTableColumn<Rent, String> contactsColumn = new TreeTableColumn<>("联系人");
        contactsColumn.setEditable(false);
        contactsColumn.setPrefWidth(120);
        contactsColumn.setCellValueFactory(new TreeItemPropertyValueFactory("contacts"));
        
        final TreeTableColumn<Rent, String> notesColumn = new TreeTableColumn<>("备注");
        notesColumn.setEditable(false);
        notesColumn.setPrefWidth(120);
        notesColumn.setCellValueFactory(new TreeItemPropertyValueFactory("notes"));
        
        final TreeTableColumn<Rent, String> discountAmountColumn = new TreeTableColumn<>("优惠金额");
        discountAmountColumn.setEditable(false);
        discountAmountColumn.setPrefWidth(120);
        discountAmountColumn.setCellValueFactory(new TreeItemPropertyValueFactory("discountAmount"));
        
        final TreeTableColumn<Rent, String> discountReasonColumn = new TreeTableColumn<>("优惠原因");
        discountReasonColumn.setEditable(false);
        discountReasonColumn.setPrefWidth(120);
        discountReasonColumn.setCellValueFactory(new TreeItemPropertyValueFactory("discountReason"));
        
        final TreeTableColumn<Rent, String> receivableAmountColumn = new TreeTableColumn<>("应收金额");
        receivableAmountColumn.setEditable(false);
        receivableAmountColumn.setPrefWidth(120);
        receivableAmountColumn.setCellValueFactory(new TreeItemPropertyValueFactory("receivableAmount"));
        
        final TreeTableColumn<Rent, String> actuallyPayColumn = new TreeTableColumn<>("实际支付");
        actuallyPayColumn.setEditable(false);
        actuallyPayColumn.setPrefWidth(120);
        actuallyPayColumn.setCellValueFactory(new TreeItemPropertyValueFactory("actuallyPay"));
        
        final TreeTableColumn<Rent, String> amountReceivedColumn = new TreeTableColumn<>("已收金额");
        amountReceivedColumn.setEditable(false);
        amountReceivedColumn.setPrefWidth(120);
        amountReceivedColumn.setCellValueFactory(new TreeItemPropertyValueFactory("amountReceived"));
        
        final TreeTableColumn<Rent, String> owingAmountColumn = new TreeTableColumn<>("尚欠金额");
        owingAmountColumn.setEditable(false);
        owingAmountColumn.setPrefWidth(120);
        owingAmountColumn.setCellValueFactory(new TreeItemPropertyValueFactory("owingAmount"));
        
        final TreeTableColumn<Rent, String> settlementDateColumn = new TreeTableColumn<>("结算日期");
        settlementDateColumn.setEditable(false);
        settlementDateColumn.setPrefWidth(120);
        settlementDateColumn.setCellValueFactory(new TreeItemPropertyValueFactory("settlementDate"));
        
        final TreeTableColumn<Rent, String> paymentColumn = new TreeTableColumn<>("支付方式");
        paymentColumn.setEditable(false);
        paymentColumn.setPrefWidth(120);
        paymentColumn.setCellValueFactory(new TreeItemPropertyValueFactory("payment"));
        
        final TreeTableColumn<Rent, String> settlementNotesColumn = new TreeTableColumn<>("结算备注");
        settlementNotesColumn.setEditable(false);
        settlementNotesColumn.setPrefWidth(120);
        settlementNotesColumn.setCellValueFactory(new TreeItemPropertyValueFactory("settlementNotes"));
       
        final TreeTableColumn<Rent, String> settlementStateColumn = new TreeTableColumn<>("结算状态");
        settlementStateColumn.setEditable(false);
        settlementStateColumn.setPrefWidth(120);
        settlementStateColumn.setCellValueFactory(new TreeItemPropertyValueFactory("settlementState"));
        
        treeTableView = new TreeTableView(rootItem);
        
        treeTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                TreeItem<Rent> r = (TreeItem<Rent>) newValue;
                if (r != null) {
                    if (r.getValue().SNProperty().get() == null) {
                        disable(true);
                    } else {
                        disable(false);
                    }
                }
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
        treeTableView.getColumns().setAll(
                snColumn,
                billingDateColumn,
                customerNameColumn,
                phoneNoColumn,
                licensePlateNumberColumn,
                paintPartsColumn,
                notesColumn,
                discountAmountColumn,
                discountReasonColumn,
                receivableAmountColumn,
                actuallyPayColumn,
                amountReceivedColumn,
                owingAmountColumn,
                settlementDateColumn,
                paymentColumn,
                settlementNotesColumn,
                settlementStateColumn,
                contactsColumn);
        
//        int rowsPerPage = 30;
//        Pagination pagination = new Pagination((rootItem.getChildren().size() / rowsPerPage + 1), 0); 
        
        pane.setCenter(treeTableView);
//        pane.setBottom(pagination);
        
        loadTableData(null);
        
        return pane;
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
        
        grid.add(new Label("租户名:"), 0, 1);
        name.setPromptText("租户名");
        grid.add(name, 1, 1);

        grid.add(new Label("联系人:"), 2, 1);
        contacts.setPromptText("联系人");
        grid.add(contacts, 3, 1);

        grid.add(new Label("联系电话:"), 4, 1);
        grid.add(phoneNoField, 5, 1);

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

                String SNVal = SNField.getText();
                String nameVal = name.getText();
                String contactsVal = contacts.getText();
                String phoneNoVal = phoneNoField.getText();
                Criterion c1 = Restrictions.ne("id", "0");
                if (!SNVal.isEmpty()) {
                    c1 = Restrictions.ilike("SN", SNVal, MatchMode.ANYWHERE);
                }
                Criterion c2 = Restrictions.ne("id", "0");
                if (!nameVal.isEmpty()) {
                    c2 = Restrictions.ilike("tenantName", nameVal, MatchMode.ANYWHERE);
                }
                Criterion c3 = Restrictions.ne("id", "0");
                if (!contactsVal.isEmpty()) {
                    c3 = Restrictions.ilike("contacts", contactsVal, MatchMode.ANYWHERE);
                }
                Criterion c4 = Restrictions.ne("id", "0");
                if (!phoneNoVal.isEmpty()) {
                    c4 = Restrictions.ilike("phoneNo", phoneNoVal, MatchMode.ANYWHERE);
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
        
        List<Rental> lstRental = (List<Rental>) controller.listRental.call(c);
        for (Rental r : lstRental) {
            Rent m = new Rent(
                    r.getId(),
                    r.getSN(),
                    DateUtils.Date2String(r.getBillingDate()),
                    r.getTenantId(),
                    r.getTenantName(),
                    r.getContacts(),
                    r.getPhoneNo(),
                    r.getDiscountAmount().toString(),
                    r.getDiscountReason(),
                    r.getReceivableAmount().toString(),
                    r.getActuallyPay().toString(),
                    r.getAmountReceived().toString(),
                    r.getOwingAmount().toString(),
                    DateUtils.Date2String(r.getSettlementDate()),
                    r.getPayment(),
                    r.getSettlementNotes(),
                    r.getSettlementState(),
                    "", "", "");
            TreeItem<Rent> item = new TreeItem<>(m);
            
            for (TenantCar car : r.getTenantCars()) {
                Rent rm = new Rent(
                    car.getId(),
                    car.getSN(),
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    car.getLicensePlateNumber(),
                    car.getPaintParts(),
                    car.getNotes());
                TreeItem<Rent> subitem = new TreeItem<>(rm);
                item.getChildren().add(subitem);
            }
            
            rootItem.getChildren().add(item);
        }
        

        disable(true);
    }
    
    private void export() {
        FileChooser fc = new FileChooser();
        fc.setInitialFileName(DateUtils.Date2String(new Date())+".xls");
        File selectedFile = fc.showSaveDialog(MainApp.getInstance().getPrimaryStage());
        if (selectedFile != null) {
            try {
                Excel.getInstance().exportRentals(treeTableView, selectedFile.getAbsolutePath());
            } catch (IOException ex) {
                org.apache.logging.log4j.Logger logger = LogManager.getLogger(RentalManagementTabContent.class.getName());
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

    private void add() {
        RentalDetailsDialog dia = new RentalDetailsDialog();
        dia.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> dia.saveOrUpdateRental(r -> loadTableData(null)));
    }
    
    private void update() {
        TreeItem<Rent> selectedItem = (TreeItem<Rent>) treeTableView.getSelectionModel().getSelectedItem();
        Rent m = selectedItem.getValue();
        if (m.SNProperty().get() != null) {
            Rental editRental = (Rental) controller.getRentalById.call(m.idProperty().get());
            RentalDetailsDialog dia = new RentalDetailsDialog(editRental);
            dia.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> dia.saveOrUpdateRental(r -> loadTableData(criterion)));
        }
    }
    
    private void remove() {
        Callback callback = new Callback() {
            @Override
            public Object call(Object param) {
                TreeItem<Rent> selectedItem = (TreeItem<Rent>) treeTableView.getSelectionModel().getSelectedItem();
                Rent m = selectedItem.getValue();
                if (m.SNProperty().get().equals("")) {
                    Rent pm = selectedItem.getParent().getValue();
                    Rental deleteRental = (Rental) controller.getRentalById.call(pm.idProperty().get());
                    Iterator<TenantCar> iter = deleteRental.getTenantCars().iterator();
                    while (iter.hasNext()) {
                        TenantCar car = iter.next();
                        if (car.getId().equals(m.idProperty().get())) {
                            iter.remove();
                        }
                    }
                    controller.saveOrUpdateRental.call(deleteRental);
                    selectedItem.getParent().getChildren().remove(selectedItem);
                    
                } else {
                    Rental deleteRental = (Rental) controller.getRentalById.call(m.idProperty().get());
                    controller.deleteRental.call(deleteRental);
                    rootItem.getChildren().remove(selectedItem);
                    if (rootItem.getChildren().size() == 0) {
                        disable(true);
                    }
                }
                return "";
            }
        };
        
        MessageBox.showMessage("请确认是否删除该行记录？", Alert.AlertType.CONFIRMATION, callback);
    }
    
    private void settle() {
        TreeItem<Rent> selectedItem = (TreeItem<Rent>) treeTableView.getSelectionModel().getSelectedItem();
        Rent rent = selectedItem.getValue();
        List<Rental> lstRental = new ArrayList<Rental>();
        Rental r = (Rental) controller.getRentalById.call(rent.idProperty().get());
        lstRental.add(r);
        RentalSettlementDialog dia = new RentalSettlementDialog(lstRental);
        dia.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> dia.pay(null));
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
    
    private TextField SNField = new TextField();
    private TextField name = new TextField();
    private TextField contacts = new TextField();
    private TextField phoneNoField = new TextField();
    private DatePicker fromDate = new DatePicker();
    private DatePicker toDate = new DatePicker();
    private Button btnQuery = new Button("查询");
    private Button btnBackAll = new Button("返回");
    
    private TreeItem<Rent> rootItem = new TreeItem<>(new Rent("Root","", "", "", "", "", "","", "", "", "", "", "", "", "", "", "", "", "", ""));
    private TreeTableView treeTableView;
    private Criterion criterion;
    private boolean expandFlag = false;
    
    private RentalManagementTabContentController controller = new RentalManagementTabContentController();
}
