/*
 * To change this license header, choose License Headers in Item Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.ui;

import com.dockingsoftware.autorepairsystem.MainApp;
import com.dockingsoftware.autorepairsystem.component.BigDecimalCell;
import com.dockingsoftware.autorepairsystem.component.EditingCell;
import com.dockingsoftware.autorepairsystem.component.FloatCell;
import com.dockingsoftware.autorepairsystem.component.MessageBox;
import com.dockingsoftware.autorepairsystem.component.model.ItemModel;
import com.dockingsoftware.autorepairsystem.persistence.entity.Item;
import com.dockingsoftware.autorepairsystem.persistence.entity.ItemTag;
import com.dockingsoftware.autorepairsystem.ui.controller.ChooseItemDialogController;
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

public class ChooseItemDialog extends Dialog {

    public ChooseItemDialog(String tag) {
        this.tag = tag;
        initComponents();
    }
    
    private void initComponents() {
        initOwner(MainApp.getInstance().getPrimaryStage());
        
        setTitle("选择商品");
        setHeaderText("请选择商品条目。");
 
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
        searchField.setPromptText("请输入商品名称搜索。");
        searchField.setPrefWidth(400);
        btnGoback.setPrefWidth(60);
        btnSearch.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                String nameVal = searchField.getText();
                Criterion c = Restrictions.ne("id", "0");
                if (!nameVal.isEmpty()) {
                    c = Restrictions.ilike("name", nameVal, MatchMode.ANYWHERE);
                }
                loadData(c);
            }
        });
        btnGoback.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                loadData(null);
            }
        });
        hbox.getChildren().addAll(searchField, btnSearch, btnGoback);
        HBox.setHgrow(searchField, Priority.ALWAYS);
        
        return hbox;
    }
    
    private VBox createTablePane() {
        loadData(null);
        
        Callback<TableColumn, TableCell> cellFactory = new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn p) {
                if ("salesPrice".equals(p.getId())) {
                    return new BigDecimalCell();
                } else if ("quantity".equals(p.getId())) {
                    return new FloatCell();
                } else {
                    return new EditingCell();
                }
            }
        };
        
        TableColumn No = new TableColumn("序号");
        TableColumn name = new TableColumn("商品名称");
        TableColumn salesPrice = new TableColumn("销售单价");
        TableColumn quantity = new TableColumn("数量");
        
        salesPrice.setId("salesPrice");
        quantity.setId("quantity");
        
        // 设置列宽
        No.setPrefWidth(50);
        name.setPrefWidth(120);
        salesPrice.setPrefWidth(120);
        quantity.setPrefWidth(120);
        
        // 设置取消编辑列
        No.setEditable(false);
        name.setEditable(false);
        salesPrice.setEditable(false);
        quantity.setEditable(false);
        
        // 设置可编辑列
        No.setCellFactory(cellFactory);
        name.setCellFactory(cellFactory);
        salesPrice.setCellFactory(cellFactory);
        quantity.setCellFactory(cellFactory);
        
        No.setCellValueFactory(new PropertyValueFactory<ItemModel, String>("No"));
        name.setCellValueFactory(new PropertyValueFactory<ItemModel, String>("name"));
        salesPrice.setCellValueFactory(new PropertyValueFactory<ItemModel, BigDecimal>("salesPrice"));
        quantity.setCellValueFactory(new PropertyValueFactory<ItemModel, Float>("quantity"));
        table.setItems(data);
        table.getColumns().addAll(No, name, salesPrice, quantity);
        
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
                        
                        ItemModel selectedModel = (ItemModel) table.getSelectionModel().getSelectedItem();
                        if (selectedModel != null) {
                            Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
                            okButton.fire();
                        }
                    }
                }
            }
        });
        
        ContextMenu menu = new ContextMenu();
        MenuItem itemAdd = new MenuItem("添加新商品");
        menu.getItems().addAll(itemAdd, new SeparatorMenuItem(), itemUpdate, itemRemove);
        table.setContextMenu(menu);
        
        itemAdd.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                ItemTag tag = (ItemTag) controller.getRootItemTag.call("");
                AddOrUpdateItemDialog dia = new AddOrUpdateItemDialog(tag, null);
                dia.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> dia.saveOrUpdateItem(i -> insertItemRow(i)));
            }
        });
        itemUpdate.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                ItemModel selectItemModel = (ItemModel) table.getSelectionModel().getSelectedItem();
                Item item = (Item) controller.getItemById.call(selectItemModel.getId());
                AddOrUpdateItemDialog dia = new AddOrUpdateItemDialog(null, item);
                dia.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> dia.saveOrUpdateItem(m -> updateItemRow(m)) );
            }
        });
        itemRemove.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                Callback callback = new Callback() {
                    @Override
                    public Object call(Object param) {
                        ItemModel selectItemModel = (ItemModel) table.getSelectionModel().getSelectedItem();
                        Item item = new Item();
                        item.setId(selectItemModel.getId());

                        controller.deleteItem.call(item);

                        data.remove(selectItemModel);

                        resortTableRows();
                        
                        if (data.size() == 0) {
                            disable(true);
                        }
                        return "";
                    }
                };
                MessageBox.showMessage("请确认是否要删除该商品。", Alert.AlertType.CONFIRMATION, callback);
            }
        });
        disable(true);
        VBox vbox = new VBox();
        vbox.getChildren().addAll(table);
        VBox.setVgrow(table, Priority.ALWAYS);
        return vbox;
    }
     
    private void insertItemRow(Item item) {
        ItemModel m = new ItemModel();
        BeanUtils.copyProperties(item, m);
        m.setNo((data.size() + 1) +"");
        data.add(m);
    }
    
    private void updateItemRow(Item item) {
        ItemModel selectedModel = (ItemModel) table.getSelectionModel().getSelectedItem();
        BeanUtils.copyProperties(item, selectedModel);
        selectedModel.setNo(selectedModel.getNo());
        table.refresh();
    }
    
    public void returnItem(Consumer<Item> consumer) {
        ItemModel selectedModel = (ItemModel) table.getSelectionModel().getSelectedItem();
        Item item = (Item) controller.getItemById.call(selectedModel.getId());
        consumer.accept(item);
    }
 
    private void loadData(Criterion c) {
        data.clear();
        List<Item> lstItem = (List<Item>) controller.listItemByCriterion.call(c);
        int rowNum = 1;
        for (Item p : lstItem) {
            ItemModel m = new ItemModel();
            BeanUtils.copyProperties(p, m);
            m.setNo((rowNum++) + "");
            data.add(m);
        }
        disable(true);
    }
    
    private void resortTableRows() {
        int rowNum = 1;
        for (ItemModel im : data) {
            im.setNo((rowNum ++) + "");
        }
    }
    
    private void disable(boolean b) {
        Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(b);
        itemUpdate.setDisable(b);
        itemRemove.setDisable(b);
    }
    
    private TableView table = new TableView();
    private final ObservableList<ItemModel> data = FXCollections.observableArrayList();
    private TextField searchField = new TextField();
    private Button btnSearch = new Button("搜索", ImageUtils.createImageView("search_16px_505060_easyicon.net.png"));
    private Button btnGoback = new Button("返回");
    private MenuItem itemUpdate = new MenuItem("变更");
    private MenuItem itemRemove = new MenuItem("删除");
    private ChooseItemDialogController controller = new ChooseItemDialogController();
    private String tag;
}