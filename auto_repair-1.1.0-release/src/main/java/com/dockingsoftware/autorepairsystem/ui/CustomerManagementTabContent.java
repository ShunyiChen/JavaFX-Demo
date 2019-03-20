/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dockingsoftware.autorepairsystem.ui;

import com.dockingsoftware.autorepairsystem.MainApp;
import com.dockingsoftware.autorepairsystem.component.DatePickerCell;
import com.dockingsoftware.autorepairsystem.component.EditingCell;
import com.dockingsoftware.autorepairsystem.component.MessageBox;
import com.dockingsoftware.autorepairsystem.common.Excel;
import com.dockingsoftware.autorepairsystem.component.IntegerCell;
import com.dockingsoftware.autorepairsystem.component.model.CustomerModel;
import com.dockingsoftware.autorepairsystem.persistence.entity.Customer;
import com.dockingsoftware.autorepairsystem.ui.controller.CustomerManagementTabContentController;
import com.dockingsoftware.autorepairsystem.util.DateUtils;
import com.dockingsoftware.autorepairsystem.util.ImageUtils;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
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
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.SeparatorMenuItem;
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

public class CustomerManagementTabContent extends VBox {

    /**
     * Constructor.
     */
    public CustomerManagementTabContent() {
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
        
        btnExport.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                export();
            }
        });

        ToolBar bar = new ToolBar();
        disable(true);
        bar.getItems().addAll(btnAdd, new Separator(), btnUpdate, btnRemove, new Separator(), btnExport);

        return bar;
    }
    
    private GridPane createSearchPane() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 20, 5, 10));
        
        grid.add(new Label("客户名称:"), 0, 0);
        customerNameField.setPromptText("客户名称");
        grid.add(customerNameField, 1, 0);
        
        grid.add(new Label("电话:"), 2, 0);
        phoneNoField.setPromptText("电话");
        grid.add(phoneNoField, 3, 0);
        
        grid.add(new Label("车牌号:"), 4, 0);
        licensePlateNumberField.setPromptText("车牌号");
        grid.add(licensePlateNumberField, 5, 0);

        grid.add(new Label("车型:"), 0, 1);
        modelField.setPromptText("车型");
        grid.add(modelField, 1, 1);
        
        grid.add(new Label("VIN码:"), 2, 1);
        vinCodeField.setPromptText("VIN码");
        grid.add(vinCodeField, 3, 1);
        
        grid.add(new Label("发动机号:"), 4, 1);
        engineNoField.setPromptText("发动机号");
        grid.add(engineNoField, 5, 1);
        
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

                String customerNameVal = customerNameField.getText();
                String phoneNoVal = phoneNoField.getText();
                String licensePlateNumberVal = licensePlateNumberField.getText();
                String modelVal = modelField.getText();
                String vinCodeVal = vinCodeField.getText();
                String engineNoVal = engineNoField.getText();
                
                Criterion c1 = Restrictions.ne("id", "0");
                if (!customerNameVal.isEmpty()) {
                    c1 = Restrictions.ilike("name", customerNameVal, MatchMode.ANYWHERE);
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
                if (!modelVal.isEmpty()) {
                    c4 = Restrictions.ilike("model", modelVal, MatchMode.ANYWHERE);
                }
                Criterion c5 = Restrictions.ne("id", "0");
                if (!vinCodeVal.isEmpty()) {
                    c5 = Restrictions.ilike("vinCode", vinCodeVal, MatchMode.ANYWHERE);
                }
                Criterion c6 = Restrictions.ne("id", "0");
                if (!engineNoVal.isEmpty()) {
                    c6 = Restrictions.ilike("engineNo", engineNoVal, MatchMode.ANYWHERE);
                }
                Criterion c = Restrictions.and(c1, c2, c3, c4, c5, c6);
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
        
        loadTableData(null);

        Callback<TableColumn, TableCell> cellFactory = new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn p) {
                if ("registrationDate".equals(p.getId())
                        || "nextMaintenanceDate".equals(p.getId())
                        || "nextVisitDate".equals(p.getId())
                        || "nextAnnualReviewDate".equals(p.getId())) {
                    return new DatePickerCell();
                } 
                else if ("latestMileage".equals(p.getId())
                    || "nextMaintenanceMilage".equals(p.getId())
                    || "inspectionMonth".equals(p.getId())) {
                    return new IntegerCell();
                }
                else {
                    return new EditingCell();
                }
            }
        };
        
        TableColumn No = new TableColumn("序号");
        TableColumn name = new TableColumn("客户名称");
        TableColumn phoneNo = new TableColumn("联系手机");
        TableColumn address = new TableColumn("客户地址");
        TableColumn notes = new TableColumn("客户备注");
        TableColumn latestMileage = new TableColumn("最新公里数");
        TableColumn licensePlateNumber = new TableColumn("号码号牌");
        TableColumn factoryPlateModel = new TableColumn("厂牌型号");
        TableColumn vinCode = new TableColumn("VIN码");
        TableColumn vehicleTypeName = new TableColumn("机动车种类名称");
        TableColumn engineNo = new TableColumn("发动机号");
        TableColumn registrationDate = new TableColumn("登记日期");
        TableColumn model = new TableColumn("车型");
        TableColumn color = new TableColumn("颜色");
        TableColumn nextMaintenanceMilage = new TableColumn("下次保养里程");
        TableColumn nextMaintenanceDate = new TableColumn("下次保养时间");
        TableColumn nextVisitDate = new TableColumn("下次回访时间");
        TableColumn nextAnnualReviewDate = new TableColumn("下次年审时间");
        TableColumn insuranceMonthDay = new TableColumn("保险月/日");
        TableColumn inspectionMonth = new TableColumn("检车月份");
        TableColumn insuranceCompany = new TableColumn("保险公司");
        TableColumn insuranceNotes = new TableColumn("保险备注");
        TableColumn carNotes = new TableColumn("车辆备注");

        // 渲染列ID
        latestMileage.setId("latestMileage");
        nextMaintenanceMilage.setId("nextMaintenanceMilage");
        inspectionMonth.setId("inspectionMonth");
        
        registrationDate.setId("registrationDate");
        nextMaintenanceDate.setId("nextMaintenanceDate");
        nextVisitDate.setId("nextVisitDate");
        nextAnnualReviewDate.setId("nextAnnualReviewDate");
        
      
        No.setCellFactory(cellFactory);
        name.setCellFactory(cellFactory);
        phoneNo.setCellFactory(cellFactory);
        address.setCellFactory(cellFactory);
        notes.setCellFactory(cellFactory);
        latestMileage.setCellFactory(cellFactory);
        licensePlateNumber.setCellFactory(cellFactory);
        factoryPlateModel.setCellFactory(cellFactory);
        vinCode.setCellFactory(cellFactory);
        vehicleTypeName.setCellFactory(cellFactory);
        engineNo.setCellFactory(cellFactory);
        model.setCellFactory(cellFactory);
        color.setCellFactory(cellFactory);
        registrationDate.setCellFactory(cellFactory);
        nextMaintenanceDate.setCellFactory(cellFactory);
        nextVisitDate.setCellFactory(cellFactory);
        nextAnnualReviewDate.setCellFactory(cellFactory);
        nextMaintenanceMilage.setCellFactory(cellFactory);
        insuranceMonthDay.setCellFactory(cellFactory);
        inspectionMonth.setCellFactory(cellFactory);
        insuranceCompany.setCellFactory(cellFactory);
        insuranceNotes.setCellFactory(cellFactory);
        carNotes.setCellFactory(cellFactory);

        // 设置列宽
        No.setPrefWidth(50);
        name.setPrefWidth(120);
        phoneNo.setPrefWidth(120);
        address.setPrefWidth(120);
        notes.setPrefWidth(120);
        latestMileage.setPrefWidth(120);
        licensePlateNumber.setPrefWidth(120);
        factoryPlateModel.setPrefWidth(120);
        vinCode.setPrefWidth(120);
        vehicleTypeName.setPrefWidth(120);
        engineNo.setPrefWidth(120);
        registrationDate.setPrefWidth(120);
        model.setPrefWidth(120);
        color.setPrefWidth(120);
        notes.setPrefWidth(120);
        carNotes.setPrefWidth(120);
        nextMaintenanceMilage.setPrefWidth(120);
        nextMaintenanceDate.setPrefWidth(120);
        insuranceMonthDay.setPrefWidth(120);
        inspectionMonth.setPrefWidth(120);
        nextVisitDate.setPrefWidth(120);
        nextAnnualReviewDate.setPrefWidth(120);
        insuranceCompany.setPrefWidth(120);
        insuranceNotes.setPrefWidth(120);

        No.setCellValueFactory(new PropertyValueFactory<CustomerModel, String>("No"));
        name.setCellValueFactory(new PropertyValueFactory<CustomerModel, String>("name"));
        phoneNo.setCellValueFactory(new PropertyValueFactory<CustomerModel, String>("phoneNo"));
        address.setCellValueFactory(new PropertyValueFactory<CustomerModel, String>("address"));
        notes.setCellValueFactory(new PropertyValueFactory<CustomerModel, String>("notes"));
        carNotes.setCellValueFactory(new PropertyValueFactory<CustomerModel, String>("carNotes"));
        latestMileage.setCellValueFactory(new PropertyValueFactory<CustomerModel, Integer>("latestMileage"));
        licensePlateNumber.setCellValueFactory(new PropertyValueFactory<CustomerModel, String>("licensePlateNumber"));
        factoryPlateModel.setCellValueFactory(new PropertyValueFactory<CustomerModel, String>("factoryPlateModel"));
        vinCode.setCellValueFactory(new PropertyValueFactory<CustomerModel, String>("vinCode"));
        vehicleTypeName.setCellValueFactory(new PropertyValueFactory<CustomerModel, String>("vehicleTypeName"));
        engineNo.setCellValueFactory(new PropertyValueFactory<CustomerModel, String>("engineNo"));
        registrationDate.setCellValueFactory(new PropertyValueFactory<CustomerModel, LocalDate>("registrationDate"));
        model.setCellValueFactory(new PropertyValueFactory<CustomerModel, String>("model"));
        color.setCellValueFactory(new PropertyValueFactory<CustomerModel, String>("color"));
        nextMaintenanceMilage.setCellValueFactory(new PropertyValueFactory<CustomerModel, Integer>("nextMaintenanceMilage"));
        nextMaintenanceDate.setCellValueFactory(new PropertyValueFactory<CustomerModel, LocalDate>("nextMaintenanceDate"));
        nextVisitDate.setCellValueFactory(new PropertyValueFactory<CustomerModel, LocalDate>("nextVisitDate"));
        nextAnnualReviewDate.setCellValueFactory(new PropertyValueFactory<CustomerModel, LocalDate>("nextAnnualReviewDate"));
        insuranceMonthDay.setCellValueFactory(new PropertyValueFactory<CustomerModel, String>("insuranceMonthDay"));
        inspectionMonth.setCellValueFactory(new PropertyValueFactory<CustomerModel, Integer>("inspectionMonth"));
        insuranceCompany.setCellValueFactory(new PropertyValueFactory<CustomerModel, String>("insuranceCompany"));
        insuranceNotes.setCellValueFactory(new PropertyValueFactory<CustomerModel, String>("insuranceNotes"));

        table.setItems(data);
        table.getColumns().addAll(No, name, phoneNo, address, latestMileage, licensePlateNumber, factoryPlateModel, vinCode, vehicleTypeName, engineNo, registrationDate, model, color, 
                nextMaintenanceMilage, nextMaintenanceDate, nextVisitDate, nextAnnualReviewDate, insuranceMonthDay, inspectionMonth, insuranceCompany, insuranceNotes, notes, carNotes);
        table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                disable(false);
            }
        });

        // 设置表格右键菜单
        ContextMenu menu = new ContextMenu();
        menu.getItems().addAll(itemAdd, itemUpdate, itemRemove, new SeparatorMenuItem(), itemExport);
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
        itemExport.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                export();
            }
        });
        
        table.setContextMenu(menu);

        // 表格双击
        table.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (t.getClickCount() == 2) {
                    CustomerModel selectedModel = (CustomerModel) table.getSelectionModel().getSelectedItem();
                    if (data.size() > 0 && selectedModel != null) {
                        update();
                    } else {
                        add();
                    }
                }
            }
        });
        
        pane.setCenter(table);
        return pane;
    }

    private void loadTableData(Criterion c) {
        data.clear();
        List<Customer> lstCustomer = (List<Customer>) controller.listCustomer.call(c);
        int rowNum = 1;
        for (Customer customer : lstCustomer) {
            CustomerModel customerModel = new CustomerModel();
            BeanUtils.copyProperties(customer, customerModel);
            customerModel.setRegistrationDate(DateUtils.Date2LocalDate(customer.getRegistrationDate()));
            customerModel.setNextMaintenanceDate(DateUtils.Date2LocalDate(customer.getNextMaintenanceDate()));
            customerModel.setNextVisitDate(DateUtils.Date2LocalDate(customer.getNextVisitDate()));
            customerModel.setNextAnnualReviewDate(DateUtils.Date2LocalDate(customer.getNextAnnualReviewDate()));
            
            customerModel.setNo((rowNum ++ ) + "");

            data.add(customerModel);
        }

        disable(true);
    }

    private void add() {
        AddOrUpdateCustomerDialog dia = new AddOrUpdateCustomerDialog();
        dia.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> dia.saveOrUpdateCustomer(customer -> insertTableRow(customer)));
    }

    private void update() {
        CustomerModel selectedModel = (CustomerModel) table.getSelectionModel().getSelectedItem();
        Customer customer = (Customer) controller.getCustomerByID.call(selectedModel.getId());
        AddOrUpdateCustomerDialog dia = new AddOrUpdateCustomerDialog(customer);
        dia.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> dia.saveOrUpdateCustomer(c -> updateTableRow(c)));
    }

    private void remove() {
        
        Callback callback = new Callback() {
            @Override
            public Object call(Object param) {
                CustomerModel selectedModel = (CustomerModel) table.getSelectionModel().getSelectedItem();
                Customer customer = (Customer) controller.getCustomerByID.call(selectedModel.getId());
                controller.deleteCustomer.call(customer);
                data.remove(selectedModel);
                if (data.size() == 0) {
                    disable(true);
                }
                resortTableRowNumber();
                return "";
            }
        };
        MessageBox.showMessage("请确认是否删除该用户。", Alert.AlertType.CONFIRMATION, callback);
    }

    private void export() {
        FileChooser fc = new FileChooser();
        fc.setInitialFileName(DateUtils.Date2String(new Date())+".xls");
        File selectedFile = fc.showSaveDialog(MainApp.getInstance().getPrimaryStage());
        if (selectedFile != null) {
            try {
                Excel.getInstance().exportCustomers(table, selectedFile.getAbsolutePath());
            } catch (IOException ex) {
                org.apache.logging.log4j.Logger logger = LogManager.getLogger(CustomerManagementTabContent.class.getName());
                logger.error(ex);
                MessageBox.showMessage("由于文件被占用无法导出。");
            }
        }
    }
    
    private void disable(boolean b) {
        btnUpdate.setDisable(b);
        btnRemove.setDisable(b);
        itemUpdate.setDisable(b);
        itemRemove.setDisable(b);
    }

    private void insertTableRow(Customer customer) {
        CustomerModel customerModel = new CustomerModel();
        BeanUtils.copyProperties(customer, customerModel);
        customerModel.setNo((data.size() + 1) + "");
        customerModel.setRegistrationDate(DateUtils.Date2LocalDate(customer.getRegistrationDate()));
        customerModel.setNextMaintenanceDate(DateUtils.Date2LocalDate(customer.getNextMaintenanceDate()));
        data.add(customerModel);
    }

    private void updateTableRow(Customer customer) {
        CustomerModel customerModel = (CustomerModel) table.getSelectionModel().getSelectedItem();
        BeanUtils.copyProperties(customer, customerModel);
        customerModel.setRegistrationDate(DateUtils.Date2LocalDate(customer.getRegistrationDate()));
        customerModel.setNextMaintenanceDate(DateUtils.Date2LocalDate(customer.getNextMaintenanceDate()));
        table.refresh();
    }

    private void resortTableRowNumber() {
        int i = 1;
        for (CustomerModel m : data) {
            m.setNo((i++)+"");
        }
    }
    
    private Button btnAdd = new Button("新建客户", ImageUtils.createImageView("plus_16px_505050_easyicon.net.png"));
    private Button btnUpdate = new Button("更改", ImageUtils.createImageView("edit_16px_505013_easyicon.net.png"));
    private Button btnRemove = new Button("删除", ImageUtils.createImageView("library_16px_505031_easyicon.net.png"));
    private Button btnExport = new Button("导出", ImageUtils.createImageView("future_projects_16px_505023_easyicon.net.png"));
    
    private MenuItem itemAdd = new MenuItem("新建客户", ImageUtils.createImageView("billing_16px_504987_easyicon.net.png"));
    private MenuItem itemUpdate = new MenuItem("更改", ImageUtils.createImageView("edit_16px_505013_easyicon.net.png"));
    private MenuItem itemRemove = new MenuItem("删除", ImageUtils.createImageView("library_16px_505031_easyicon.net.png"));
    private MenuItem itemExport = new MenuItem("导出", ImageUtils.createImageView("future_projects_16px_505023_easyicon.net.png"));
    
    private TextField customerNameField = new TextField();
    private TextField phoneNoField = new TextField();
    private TextField licensePlateNumberField = new TextField();
    private TextField modelField = new TextField();
    private TextField vinCodeField = new TextField();
    private TextField engineNoField = new TextField();
    
    private Button btnQuery = new Button("查询");
    private Button btnBackAll = new Button("返回");
    private TableView table = new TableView();
    private final ObservableList<CustomerModel> data = FXCollections.observableArrayList();

    private CustomerManagementTabContentController controller = new CustomerManagementTabContentController();
}
