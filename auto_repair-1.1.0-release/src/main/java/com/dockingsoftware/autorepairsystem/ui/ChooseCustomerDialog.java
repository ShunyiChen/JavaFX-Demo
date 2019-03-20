/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.ui;

import com.dockingsoftware.autorepairsystem.MainApp;
import com.dockingsoftware.autorepairsystem.component.EditingCell;
import com.dockingsoftware.autorepairsystem.component.MessageBox;
import com.dockingsoftware.autorepairsystem.component.model.CustomerModel;
import com.dockingsoftware.autorepairsystem.persistence.entity.Customer;
import com.dockingsoftware.autorepairsystem.ui.controller.ChooseCustomerDialogController;
import com.dockingsoftware.autorepairsystem.util.ImageUtils;
import java.util.List;
import java.util.function.Consumer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;

public class ChooseCustomerDialog extends Dialog {

    public ChooseCustomerDialog() {
        initComponents();
    }
    
    private void initComponents() {
        initOwner(MainApp.getInstance().getPrimaryStage());
        
        setTitle("选择客户");
        setHeaderText("请选择客户。");
 
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
        okButton.setText("选择");
        Button cancelButton = (Button) getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelButton.setText("取消");
        VBox vbox = new VBox();
        vbox.getChildren().addAll(createSearchPane(), createTablePane());
        getDialogPane().setContent(vbox);
    }
    
    private HBox createSearchPane() {
        HBox hbox = new HBox();
        searchField.setPromptText("请输入客户名/电话/车牌号或车型。");
        searchField.setPrefWidth(400);
        btnGoback.setPrefWidth(60);
        btnSearch.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                String keyword = searchField.getText();
                loadData(keyword);
            }
        });
        btnGoback.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                loadData("");
            }
        });
        hbox.getChildren().addAll(searchField, btnSearch, btnGoback);
        HBox.setHgrow(searchField, Priority.ALWAYS);
        
        return hbox;
    }
    
    private VBox createTablePane() {
        
        loadData("");
        
        Callback<TableColumn, TableCell> cellFactory = new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn p) {
                return new EditingCell();
            }
        };
        
        TableColumn No = new TableColumn("序号");
        TableColumn name = new TableColumn("客户名称");
        TableColumn phoneNo = new TableColumn("手机");
        TableColumn licensePlateNumber = new TableColumn("车牌号");
        TableColumn model = new TableColumn("车型");
        
        // 设置列宽
        No.setPrefWidth(50);
        name.setPrefWidth(120);
        phoneNo.setPrefWidth(120);
        licensePlateNumber.setPrefWidth(120);
        model.setPrefWidth(120);
        
        // 设置取消编辑列
        No.setEditable(false);
        name.setEditable(false);
        phoneNo.setEditable(false);
        licensePlateNumber.setEditable(false);
        model.setEditable(false);
        
        // 设置可编辑列
        No.setCellFactory(cellFactory);
        name.setCellFactory(cellFactory);
        phoneNo.setCellFactory(cellFactory);
        licensePlateNumber.setCellFactory(cellFactory);
        model.setCellFactory(cellFactory);
        
        No.setCellValueFactory(new PropertyValueFactory<CustomerModel, String>("No"));
        name.setCellValueFactory(new PropertyValueFactory<CustomerModel, String>("name"));
        phoneNo.setCellValueFactory(new PropertyValueFactory<CustomerModel, String>("phoneNo"));
        licensePlateNumber.setCellValueFactory(new PropertyValueFactory<CustomerModel, String>("licensePlateNumber"));
        model.setCellValueFactory(new PropertyValueFactory<CustomerModel, String>("model"));
        
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
                        
                        CustomerModel selectedModel = (CustomerModel) table.getSelectionModel().getSelectedItem();
                        if (selectedModel != null) {
                            Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
                            okButton.fire();
                        }
                    }
                }
            }
        });
        
        ContextMenu menu = new ContextMenu();
        MenuItem itemAdd = new MenuItem("新建客户");
        MenuItem itemRemove = new MenuItem("删除");
        MenuItem itemUpdate = new MenuItem("更改");
        
        itemAdd.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                add();
            }
        });
        itemRemove.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                remove();
            }
        });
        itemUpdate.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                update();
            }
        });
        menu.getItems().addAll(itemAdd, new SeparatorMenuItem(), itemRemove, itemUpdate);
        table.setContextMenu(menu);
        
        table.setItems(data);
        table.getColumns().addAll(No, name, phoneNo, licensePlateNumber, model);
        table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY );
        
        disable(true);
        
        VBox vbox = new VBox();
        vbox.getChildren().addAll(table);
        VBox.setVgrow(table, Priority.ALWAYS);
        return vbox;
    }
    
    public void returnCustomer(Consumer<Customer> consumer) {
        CustomerModel selectedModel = (CustomerModel) table.getSelectionModel().getSelectedItem();
        Customer customer = (Customer) controller.getCustomerById.call(selectedModel.getId());
        consumer.accept(customer);
    }
 
    private void loadData(String keyword) {
        data.clear();
        
        Criterion c = Restrictions.or(Restrictions.ilike("name",  keyword, MatchMode.ANYWHERE), 
                        Restrictions.ilike("phoneNo", keyword, MatchMode.ANYWHERE),
                        Restrictions.ilike("licensePlateNumber", keyword, MatchMode.ANYWHERE),
                        Restrictions.ilike("model", keyword, MatchMode.ANYWHERE));
        
        List<Customer> lstCustomer = (List<Customer>) controller.listCustomer.call(c);
        int rowNum = 1;
        for (Customer customer : lstCustomer) {
            CustomerModel customerModel = new CustomerModel();
            BeanUtils.copyProperties(customer, customerModel);
            customerModel.setNo((rowNum++) + "");
            
            data.add(customerModel);
        }
    }
    
    private void disable(boolean b) {
        Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(b);
    }
    
    private void add() {
        AddOrUpdateCustomerDialog dia = new AddOrUpdateCustomerDialog();
        dia.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> dia.saveOrUpdateCustomer(customer -> insertTableRow(customer)));
    }

    private void update() {
        CustomerModel selectedModel = (CustomerModel) table.getSelectionModel().getSelectedItem();
        if (selectedModel != null) {
            Customer customer = (Customer) controller.getCustomerById.call(selectedModel.getId());
            AddOrUpdateCustomerDialog dia = new AddOrUpdateCustomerDialog(customer);
            dia.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> dia.saveOrUpdateCustomer(c -> updateTableRow(c)));
        }
    }

    private void remove() {
        
        Callback callback = new Callback() {
            @Override
            public Object call(Object param) {
                CustomerModel selectedModel = (CustomerModel) table.getSelectionModel().getSelectedItem();
                if (selectedModel != null) {
                    Customer customer = (Customer) controller.getCustomerById.call(selectedModel.getId());
                    controller.deleteCustomer.call(customer);
                    data.remove(selectedModel);
                    resortTableRows();
                    if (data.size() == 0) {
                        disable(true);
                    }
                }
                return "";
            }
        };
        MessageBox.showMessage("请确认是否删除该用户。", Alert.AlertType.CONFIRMATION, callback);
    }
    
    private void insertTableRow(Customer customer) {
        CustomerModel customerModel = new CustomerModel();
        BeanUtils.copyProperties(customer, customerModel);
        customerModel.setNo((data.size() + 1) + "");
        data.add(customerModel);
    }

    private void updateTableRow(Customer customer) {
        CustomerModel customerModel = (CustomerModel) table.getSelectionModel().getSelectedItem();
        BeanUtils.copyProperties(customer, customerModel);
        table.refresh();
    }
    
    private void resortTableRows() {
        int rowNum = 1;
        for (CustomerModel cm : data) {
            cm.setNo((rowNum ++) + "");
        }
    }
    
    private TableView table = new TableView();
    private final ObservableList<CustomerModel> data = FXCollections.observableArrayList();
    private TextField searchField = new TextField();
    private Button btnSearch = new Button("搜索", ImageUtils.createImageView("search_16px_505060_easyicon.net.png"));
    private Button btnGoback = new Button("返回");
    private ChooseCustomerDialogController controller = new ChooseCustomerDialogController();
}
