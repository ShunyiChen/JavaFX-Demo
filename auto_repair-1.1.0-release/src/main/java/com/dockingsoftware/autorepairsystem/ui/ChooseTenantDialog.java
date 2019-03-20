/*
 * To change this license header, choose License Headers in Tenant Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.ui;

import com.dockingsoftware.autorepairsystem.MainApp;
import com.dockingsoftware.autorepairsystem.component.EditingCell;
import com.dockingsoftware.autorepairsystem.component.MessageBox;
import com.dockingsoftware.autorepairsystem.component.model.TenantModel;
import com.dockingsoftware.autorepairsystem.persistence.entity.Tenant;
import com.dockingsoftware.autorepairsystem.ui.controller.ChooseTenantDialogController;
import com.dockingsoftware.autorepairsystem.util.ImageUtils;
import java.math.BigDecimal;
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

public class ChooseTenantDialog extends Dialog {

    public ChooseTenantDialog() {
        initComponents();
    }
    
    private void initComponents() {
        initOwner(MainApp.getInstance().getPrimaryStage());
        
        setTitle("选择租户");
        setHeaderText("请选择一个租户。");
 
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
        okButton.setText("选择");
        okButton.setDisable(true);
        Button cancelButton = (Button) getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelButton.setText("取消");

        VBox vbox = new VBox();
        vbox.getChildren().addAll(createSearchPane(), createTablePane());
        getDialogPane().setContent(vbox);
    }
    
    private HBox createSearchPane() {
        HBox hbox = new HBox();
        searchField.setPromptText("请输入租户名、联系人、电话或备注。");
        searchField.setPrefWidth(400);
        btnGoback.setPrefWidth(60);
        btnSearch.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                String keyword = searchField.getText();
                loadTableData(keyword);
            }
        });
        btnGoback.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                loadTableData("");
            }
        });
        hbox.getChildren().addAll(searchField, btnSearch, btnGoback);
        HBox.setHgrow(searchField, Priority.ALWAYS);
        
        return hbox;
    }
    
    private VBox createTablePane() {
        
        loadTableData("");
    
        Callback<TableColumn, TableCell> cellFactory = new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn p) {
                return new EditingCell();
            }
        };
        
        TableColumn NoCol = new TableColumn("序号");
        TableColumn nameCol = new TableColumn("租户名");
        TableColumn contactsCol = new TableColumn("联系人");
        TableColumn phoneNoCol = new TableColumn("联系电话");
        TableColumn notesCol = new TableColumn("备注");
        
        // 设置列宽
        NoCol.setPrefWidth(50);
        nameCol.setPrefWidth(120);
        contactsCol.setPrefWidth(120);
        phoneNoCol.setPrefWidth(120);
        notesCol.setPrefWidth(120);
        
        // 设置可编辑列
        NoCol.setCellFactory(cellFactory);
        nameCol.setCellFactory(cellFactory);
        contactsCol.setCellFactory(cellFactory);
        phoneNoCol.setCellFactory(cellFactory);
        notesCol.setCellFactory(cellFactory);
        
        NoCol.setCellValueFactory(new PropertyValueFactory<TenantModel, String>("No"));
        nameCol.setCellValueFactory(new PropertyValueFactory<TenantModel, String>("name"));
        contactsCol.setCellValueFactory(new PropertyValueFactory<TenantModel, BigDecimal>("contacts"));
        phoneNoCol.setCellValueFactory(new PropertyValueFactory<TenantModel, Float>("phoneNo"));
        notesCol.setCellValueFactory(new PropertyValueFactory<TenantModel, String>("notes"));
        
        table.setItems(data);
        table.getColumns().addAll(NoCol, nameCol, contactsCol, phoneNoCol, notesCol);
        table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY );
        
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
                        
                        TenantModel selectedModel = (TenantModel) table.getSelectionModel().getSelectedItem();
                        if (selectedModel != null) {
                            Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
                            okButton.fire();
                        }
                    }
                }
            }
        });
        
        ContextMenu menu = new ContextMenu();
        
        menu.getItems().addAll(itemAdd, new SeparatorMenuItem(), itemUpdate, itemRemove);
        table.setContextMenu(menu);
        
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
        disable(true);
        VBox vbox = new VBox();
        vbox.getChildren().addAll(table);
        VBox.setVgrow(table, Priority.ALWAYS);
        return vbox;
    }
     
    public void returnTenant(Consumer<Tenant> consumer) {
        TenantModel selectedModel = (TenantModel) table.getSelectionModel().getSelectedItem();
        Tenant tenant = (Tenant) controller.getTenantByID.call(selectedModel.getId());
        consumer.accept(tenant);
    }
 
    private void loadTableData(String keyword) {
        data.clear();
        
        Criterion c = Restrictions.or(
                Restrictions.ilike("name", keyword, MatchMode.ANYWHERE),
                Restrictions.ilike("contacts", keyword, MatchMode.ANYWHERE),
                Restrictions.ilike("phoneNo", keyword, MatchMode.ANYWHERE),
                Restrictions.ilike("notes", keyword, MatchMode.ANYWHERE)
        );
        
        List<Tenant> lstTenant = (List<Tenant>) controller.listTenantByCriterion.call(c);
        int rowNum = 1;
        for (Tenant p : lstTenant) {
            TenantModel model = new TenantModel();
            BeanUtils.copyProperties(p, model);
            model.setNo((rowNum++) + "");
            data.add(model);
        }
        disable(true);
    }
    
    private void resortTableRows() {
        int rowNum = 1;
        for (TenantModel pm : data) {
            pm.setNo((rowNum ++) + "");
        }
    }
    
    private void add() {
        AddOrUpdateTenantDialog dia = new AddOrUpdateTenantDialog();
        dia.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> dia.saveOrUpdateTenant(t -> insertTenantRow(t)));
        resortTableRows();
    }
    
    private void update() {
        TenantModel selectedModel = (TenantModel) table.getSelectionModel().getSelectedItem();
        Tenant editTenant = (Tenant) controller.getTenantByID.call(selectedModel.getId());
        AddOrUpdateTenantDialog dia = new AddOrUpdateTenantDialog(editTenant);
        dia.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> dia.saveOrUpdateTenant(t -> updateTenantRow(t)));
    }
    
    private void remove() {
        Callback callback = new Callback() {
            @Override
            public Object call(Object param) {
                TenantModel selectedModel = (TenantModel) table.getSelectionModel().getSelectedItem();
                if (selectedModel != null) {
                    
                    Tenant t = (Tenant) controller.getTenantByID.call(selectedModel.getId());
                    controller.deleteTenant.call(t);
                    
                    data.remove(selectedModel);
                    if (data.size() == 0) {
                        disable(true);
                    }
                    resortTableRows();
                }
                return "";
            }
        };
        MessageBox.showMessage("请确认是否删除该租户。", Alert.AlertType.CONFIRMATION, callback);
    }
    
    private void insertTenantRow(Tenant t) {
        TenantModel tenantModel = new TenantModel();
        BeanUtils.copyProperties(t, tenantModel);
        data.add(tenantModel);
    }
    
    private void updateTenantRow(Tenant t) {
        TenantModel selectedModel = (TenantModel) table.getSelectionModel().getSelectedItem();
        BeanUtils.copyProperties(t, selectedModel);
        table.refresh();
    }
    
    private void disable(boolean b) {
        Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(b);
        itemUpdate.setDisable(b);
        itemRemove.setDisable(b);
    }
    
    private TableView table = new TableView();
    private final ObservableList<TenantModel> data = FXCollections.observableArrayList();
    private TextField searchField = new TextField();
    private Button btnSearch = new Button("搜索", ImageUtils.createImageView("search_16px_505060_easyicon.net.png"));
    private Button btnGoback = new Button("返回");
    private MenuItem itemAdd = new MenuItem("添加新租户");
    private MenuItem itemUpdate = new MenuItem("更改");
    private MenuItem itemRemove = new MenuItem("删除");
    private ChooseTenantDialogController controller = new ChooseTenantDialogController();
}