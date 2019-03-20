/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.ui;

import com.dockingsoftware.autorepairsystem.MainApp;
import com.dockingsoftware.autorepairsystem.component.EditingCell;
import com.dockingsoftware.autorepairsystem.component.MessageBox;
import com.dockingsoftware.autorepairsystem.component.model.FactoryModel;
import com.dockingsoftware.autorepairsystem.persistence.entity.Factory;
import com.dockingsoftware.autorepairsystem.ui.controller.ChooseFactoryDialogController;
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

public class ChooseFactoryDialog extends Dialog {

    public ChooseFactoryDialog() {
        initComponents();
    }
    
    private void initComponents() {
        initOwner(MainApp.getInstance().getPrimaryStage());
        
        setTitle("选择送修厂");
        setHeaderText("选择送修厂。");
 
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
        searchField.setPromptText("请输入送修厂名/联系人/电话。");
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
        TableColumn name = new TableColumn("送修厂名");
        TableColumn contacts = new TableColumn("联系人");
        TableColumn phoneNo = new TableColumn("电话");
        TableColumn notes = new TableColumn("备注");
        
        // 设置列宽
        No.setPrefWidth(50);
        name.setPrefWidth(120);
        contacts.setPrefWidth(120);
        phoneNo.setPrefWidth(120);
        notes.setPrefWidth(120);
        
        // 设置取消编辑列
        No.setEditable(false);
        name.setEditable(false);
        contacts.setEditable(false);
        phoneNo.setEditable(false);
        notes.setEditable(false);
        
        // 设置可编辑列
        No.setCellFactory(cellFactory);
        name.setCellFactory(cellFactory);
        contacts.setCellFactory(cellFactory);
        phoneNo.setCellFactory(cellFactory);
        notes.setCellFactory(cellFactory);
        
        No.setCellValueFactory(new PropertyValueFactory<FactoryModel, String>("No"));
        name.setCellValueFactory(new PropertyValueFactory<FactoryModel, String>("name"));
        contacts.setCellValueFactory(new PropertyValueFactory<FactoryModel, String>("contacts"));
        phoneNo.setCellValueFactory(new PropertyValueFactory<FactoryModel, String>("phoneNo"));
        notes.setCellValueFactory(new PropertyValueFactory<FactoryModel, String>("notes"));
        
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
                        
                        FactoryModel selectedModel = (FactoryModel) table.getSelectionModel().getSelectedItem();
                        if (selectedModel != null) {
                            Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
                            okButton.fire();
                        }
                    }
                }
            }
        });
        
        ContextMenu menu = new ContextMenu();
        MenuItem itemAdd = new MenuItem("新建送修厂");
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
        table.getColumns().addAll(No, name, contacts, phoneNo, notes);
        table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY );
        
        disable(true);
        
        VBox vbox = new VBox();
        vbox.getChildren().addAll(table);
        VBox.setVgrow(table, Priority.ALWAYS);
        return vbox;
    }
    
    public void returnFactory(Consumer<Factory> consumer) {
        FactoryModel selectedModel = (FactoryModel) table.getSelectionModel().getSelectedItem();
        Factory factory = (Factory) controller.getFactoryById.call(selectedModel.getId());
        consumer.accept(factory);
    }
 
    private void loadData(String keyword) {
        data.clear();
        
        Criterion c = Restrictions.or(Restrictions.ilike("name",  keyword, MatchMode.ANYWHERE), 
                        Restrictions.ilike("contacts", keyword, MatchMode.ANYWHERE),
                        Restrictions.ilike("phoneNo", keyword, MatchMode.ANYWHERE),
                        Restrictions.ilike("notes", keyword, MatchMode.ANYWHERE));
        
        List<Factory> lstFactory = (List<Factory>) controller.listFactory.call(c);
        int rowNum = 1;
        for (Factory factory : lstFactory) {
            FactoryModel factoryModel = new FactoryModel();
            BeanUtils.copyProperties(factory, factoryModel);
            factoryModel.setNo((rowNum++) + "");
            
            data.add(factoryModel);
        }
    }
    
    private void disable(boolean b) {
        Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(b);
    }
    
    private void add() {
        AddOrUpdateFactoryDialog dia = new AddOrUpdateFactoryDialog();
        dia.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> dia.saveOrUpdateFactory(factory -> insertTableRow(factory)));
    }

    private void update() {
        FactoryModel selectedModel = (FactoryModel) table.getSelectionModel().getSelectedItem();
        if (selectedModel != null) {
            Factory factory = (Factory) controller.getFactoryById.call(selectedModel.getId());
            AddOrUpdateFactoryDialog dia = new AddOrUpdateFactoryDialog(factory);
            dia.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> dia.saveOrUpdateFactory(c -> updateTableRow(c)));
        }
    }

    private void remove() {
        
        Callback callback = new Callback() {
            @Override
            public Object call(Object param) {
                FactoryModel selectedModel = (FactoryModel) table.getSelectionModel().getSelectedItem();
                if (selectedModel != null) {
                    Factory factory = (Factory) controller.getFactoryById.call(selectedModel.getId());
                    controller.deleteFactory.call(factory);
                    data.remove(selectedModel);
                    resortTableRows();
                    if (data.size() == 0) {
                        disable(true);
                    }
                }
                return "";
            }
        };
        MessageBox.showMessage("请确认是否删除该送修厂。", Alert.AlertType.CONFIRMATION, callback);
    }
    
    private void insertTableRow(Factory factory) {
        FactoryModel factoryModel = new FactoryModel();
        BeanUtils.copyProperties(factory, factoryModel);
        factoryModel.setNo((data.size() + 1) + "");
        data.add(factoryModel);
    }

    private void updateTableRow(Factory factory) {
        FactoryModel factoryModel = (FactoryModel) table.getSelectionModel().getSelectedItem();
        BeanUtils.copyProperties(factory, factoryModel);
        table.refresh();
    }
    
    private void resortTableRows() {
        int rowNum = 1;
        for (FactoryModel fm : data) {
            fm.setNo((rowNum ++) + "");
        }
    }
    
    private TableView table = new TableView();
    private final ObservableList<FactoryModel> data = FXCollections.observableArrayList();
    private TextField searchField = new TextField();
    private Button btnSearch = new Button("搜索", ImageUtils.createImageView("search_16px_505060_easyicon.net.png"));
    private Button btnGoback = new Button("返回");
    private ChooseFactoryDialogController controller = new ChooseFactoryDialogController();
}
