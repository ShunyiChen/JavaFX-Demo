/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dockingsoftware.autorepairsystem.ui;

import com.dockingsoftware.autorepairsystem.MainApp;
import com.dockingsoftware.autorepairsystem.common.Excel;
import com.dockingsoftware.autorepairsystem.component.BigDecimalCell;
import com.dockingsoftware.autorepairsystem.component.EditingCell;
import com.dockingsoftware.autorepairsystem.component.MessageBox;
import com.dockingsoftware.autorepairsystem.component.TreeItemExt;
import com.dockingsoftware.autorepairsystem.component.model.ItemModel;
import com.dockingsoftware.autorepairsystem.persistence.entity.Item;
import com.dockingsoftware.autorepairsystem.persistence.entity.ItemTag;
import com.dockingsoftware.autorepairsystem.ui.controller.ItemManagementTabContentController;
import com.dockingsoftware.autorepairsystem.util.DateUtils;
import com.dockingsoftware.autorepairsystem.util.ImageUtils;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ContextMenuEvent;
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

/**
 * 商品管理
 *
 * @author Shunyi Chen
 */
public class ItemManagementTabContent extends VBox {

    /**
     * Constructor.
     */
    public ItemManagementTabContent() {
        initComponents();
    }

    private void initComponents() {
        SplitPane sp = createSplitPane();
        this.getChildren().addAll(sp);
        VBox.setVgrow(sp, Priority.ALWAYS);
    }
    
    private SplitPane createSplitPane() {
        VBox left = new VBox();
        BorderPane leftPane = createLeftPane();
        left.getChildren().addAll(leftPane);
        VBox.setVgrow(leftPane, Priority.ALWAYS);
        
        VBox right = new VBox();
        BorderPane rightPane = createRightPane();
        right.getChildren().addAll(createToolBar(), createSearchPane(), rightPane);
        VBox.setVgrow(rightPane, Priority.ALWAYS);
        
        SplitPane sp = new SplitPane();
        sp.getItems().addAll(left, right);
        sp.setDividerPositions(0.2f, 0.8f);
        
        return sp;
    }

    private BorderPane createLeftPane() {
        BorderPane pane = new BorderPane();
        pane.setPadding(Insets.EMPTY);
        
        ItemTag rootTag = (ItemTag) controller.getRootTag.call("0");
        root.getProperties().put(TreeItemExt.ATTR_ITEM_TAG_KEY, rootTag);
        tree.setRoot(root);
        tree.setShowRoot(true);
        
        lstAllTags = (List<ItemTag>) controller.getAllTags.call(null);
        
        loadTree(rootTag.getId(), root);
        
        ContextMenu menu = new ContextMenu();
        MenuItem addTagItem = new MenuItem("添加新标签");
        MenuItem renameTagItem = new MenuItem("重命名");
        MenuItem removeTagItem = new MenuItem("删除");
        menu.getItems().addAll(addTagItem, new SeparatorMenuItem(), renameTagItem, removeTagItem);
        tree.setContextMenu(menu);
        tree.getSelectionModel().select(root);
        tree.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent event) {
                TreeItemExt treeItem = (TreeItemExt) tree.getSelectionModel().getSelectedItem();
                ItemTag tag = (ItemTag) treeItem.getProperties().get(TreeItemExt.ATTR_ITEM_TAG_KEY);
                // If it's root
                if (tag.getPid().equals("0")) {
                    renameTagItem.setDisable(true);
                    removeTagItem.setDisable(true);
                } else {
                    renameTagItem.setDisable(false);
                    removeTagItem.setDisable(false);
                }
                menu.show(tree, event.getScreenX(), event.getScreenY());
            }
        });

        addTagItem.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                TreeItemExt selectTreeItem = (TreeItemExt) tree.getSelectionModel().getSelectedItem();
                ItemTag parentTag = (ItemTag) selectTreeItem.getProperties().get(TreeItemExt.ATTR_ITEM_TAG_KEY);
                
                AddOrUpdateItemTagDialog dia = new AddOrUpdateItemTagDialog(parentTag.getId(), null);
                dia.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> dia.saveOrUpdateItemTag(tag -> insertTreeItem(tag)));
            }
        });

        renameTagItem.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                TreeItemExt selectTreeItem = (TreeItemExt) tree.getSelectionModel().getSelectedItem();
                ItemTag tag = (ItemTag) selectTreeItem.getProperties().get(TreeItemExt.ATTR_ITEM_TAG_KEY);
                AddOrUpdateItemTagDialog dia = new AddOrUpdateItemTagDialog(tag);
                dia.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> dia.saveOrUpdateItemTag(t -> updateTreeItem(t)));
            }
        });

        removeTagItem.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                
                Callback callback = new Callback() {
                    @Override
                    public Object call(Object param) {
                        TreeItemExt treeItem = (TreeItemExt) tree.getSelectionModel().getSelectedItem();
                        
                        // 判断是否删除标签下的全部商品
                        if (deleteTagItems.isSelected()) {
                            
                            ItemTag tag = (ItemTag) treeItem.getProperties().get(TreeItemExt.ATTR_ITEM_TAG_KEY);
                            Criterion c =  Restrictions.eq("tagId", tag.getId());
                            List<Item> lstItem = (List<Item>) controller.getItemsByCriterion.call(c);
                            for (Item item : lstItem) {
                                controller.deleteItem.call(item);
                            }
                            
                            removeTagItems(treeItem);
                        }
                        
                        // 删除标签
                        ItemTag tag = (ItemTag) treeItem.getProperties().get(TreeItemExt.ATTR_ITEM_TAG_KEY);
                        controller.deleteTag.call(tag);
                        treeItem.getParent().getChildren().remove(treeItem);
                        
                        return "";
                    }
                };
                
                showMessage("请确认是否删除该分类。", Alert.AlertType.CONFIRMATION, callback);
            }
        });

        tree.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> { 
            TreeItemExt selectedTreeItem = (TreeItemExt) tree.getSelectionModel().getSelectedItem();
            ItemTag tag = (ItemTag) selectedTreeItem.getProperties().get(TreeItemExt.ATTR_ITEM_TAG_KEY);
            
            loadTable(tag);
        });
        
        pane.setCenter(tree);
        return pane;
    }

    private BorderPane createRightPane() {
        BorderPane pane = new BorderPane();
        pane.setPadding(Insets.EMPTY);
        
        Callback<TableColumn, TableCell> cellFactory = new Callback<TableColumn, TableCell>() {
            @Override
            public TableCell call(TableColumn p) {
                if ("costPrice".equals(p.getId())
                        || "salesPrice".equals(p.getId())) {
                    return new BigDecimalCell();
                } 
                else {
                    return new EditingCell();
                }
            }
        };
        
        ItemTag rootTag = (ItemTag) root.getProperties().get(TreeItemExt.ATTR_ITEM_TAG_KEY);
        
        // 加载表格数据
        loadTable(rootTag);
        
        // 设置字段
        TableColumn No = new TableColumn("序号");
        TableColumn name = new TableColumn("商品名称");
        TableColumn costPrice = new TableColumn("成本价");
        TableColumn salesPrice = new TableColumn("销售单价");
        TableColumn notes = new TableColumn("备注");
        TableColumn tagName = new TableColumn("标签名");

        // 设置列宽
        No.setPrefWidth(50);
        name.setPrefWidth(120);
        costPrice.setPrefWidth(120);
        salesPrice.setPrefWidth(120);
        notes.setPrefWidth(420);
        tagName.setPrefWidth(120);
        
        costPrice.setId("costPrice");
        salesPrice.setId("salesPrice");
        
        No.setCellFactory(cellFactory);
        name.setCellFactory(cellFactory);
        costPrice.setCellFactory(cellFactory);
        salesPrice.setCellFactory(cellFactory);
        notes.setCellFactory(cellFactory);
        tagName.setCellFactory(cellFactory);

        No.setCellValueFactory(new PropertyValueFactory<ItemModel, String>("No"));
        name.setCellValueFactory(new PropertyValueFactory<ItemModel, String>("name"));
        costPrice.setCellValueFactory(new PropertyValueFactory<ItemModel, BigDecimal>("costPrice"));
        salesPrice.setCellValueFactory(new PropertyValueFactory<ItemModel, BigDecimal>("salesPrice"));
        notes.setCellValueFactory(new PropertyValueFactory<ItemModel, String>("notes"));
        tagName.setCellValueFactory(new PropertyValueFactory<ItemModel, String>("tagName"));
        
        table.setItems(data);
        table.getColumns().addAll(No, name, costPrice, salesPrice, notes, tagName);
        table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                disable(false);
            }
        });
        // 设置表格右键菜单
        ContextMenu menu = new ContextMenu();
        menu.getItems().addAll(itemAdd, new SeparatorMenuItem(), itemUpdate, itemRemove, new SeparatorMenuItem(), itemExport);
        itemAdd.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                addItem();
            }
        });
        itemUpdate.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                updateItem();
            }
        });
        itemRemove.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                removeItem();
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
                    if (data.size() > 0) {
                        updateItem();
                    } else {
                        addItem();
                    }
                }
            }
        });

        pane.setCenter(table);
        return pane;
    }

    private ToolBar createToolBar() {
         btnAdd.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                addItem();
            }
        });
        
        btnUpdate.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                updateItem();
            }
        });
        
        btnRemove.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                removeItem();
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

    private HBox createSearchPane() {
        HBox searchBox = new HBox();
        searchField.setPromptText("请输入商品名称或备注。");
        btnGoback.setPrefWidth(60);
        searchBox.getChildren().addAll(searchField, btnSearch, btnGoback);
        HBox.setHgrow(searchField, Priority.ALWAYS);
        
        btnSearch.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                String keyword = searchField.getText();
                Criterion c1 = Restrictions.ne("id", "0");
                if (!keyword.isEmpty()) {
                    c1 = Restrictions.ilike("name", keyword, MatchMode.ANYWHERE);
                }
                
                Criterion c2 = Restrictions.ne("id", "0");
                if (!keyword.isEmpty()) {
                    c2= Restrictions.ilike("notes", keyword, MatchMode.ANYWHERE);
                }
                
                Criterion c = Restrictions.or(c1, c2);
                
                loadTableDataByCriterion(c);
            }
        });
        btnGoback.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                loadTableDataByCriterion(null);
            }
        });
        
        return searchBox;
    }

    private void disable(boolean b) {
        btnUpdate.setDisable(b);
        btnRemove.setDisable(b);
        itemUpdate.setDisable(b);
        itemRemove.setDisable(b);
    }
    
    private void addItem() {
        TreeItemExt treeItem = (TreeItemExt) tree.getSelectionModel().getSelectedItem();
        ItemTag tag = (ItemTag) treeItem.getProperties().get(TreeItemExt.ATTR_ITEM_TAG_KEY);
        
        AddOrUpdateItemDialog dia = new AddOrUpdateItemDialog(tag, null);
        dia.setQuantityDisable();
        dia.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> dia.saveOrUpdateItem(item -> insertTableRow(item)));
    }
    
    private void updateItem() {
        ItemModel selectItemModel = (ItemModel) table.getSelectionModel().getSelectedItem();
        Item item = (Item) controller.getItemById.call(selectItemModel.getId());
        AddOrUpdateItemDialog dia = new AddOrUpdateItemDialog(null, item);
        dia.setQuantityDisable();
        dia.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> dia.saveOrUpdateItem(i -> updateTableRow(i)));
    }
    
    private void removeItem() {
        
        Callback callback = new Callback() {
            @Override
            public Object call(Object param) {
                ItemModel selectItemModel = (ItemModel) table.getSelectionModel().getSelectedItem();
                Item item = new Item();
                item.setId(selectItemModel.getId());
                
                controller.deleteItem.call(item);
                
                data.remove(selectItemModel);
                if (data.size() == 0) {
                    disable(true);
                }
                return "";
            }
        };
        
        MessageBox.showMessage("请确认是否要删除该商品。", Alert.AlertType.CONFIRMATION, callback);
    }

    private void export() {
        FileChooser fc = new FileChooser();
        fc.setInitialFileName(DateUtils.Date2String(new Date())+".xls");
        File selectedFile = fc.showSaveDialog(MainApp.getInstance().getPrimaryStage());
        if (selectedFile != null) {
            try {
                Excel.getInstance().exportItems(table, selectedFile.getAbsolutePath());
            } catch (IOException ex) {
                org.apache.logging.log4j.Logger logger = LogManager.getLogger(ItemManagementTabContent.class.getName());
                logger.error(ex);
                MessageBox.showMessage("由于文件被占用无法导出。");
            }
        }
    }
    
    private void insertTreeItem(ItemTag newTag) {
        TreeItemExt parentTreeItem = (TreeItemExt) tree.getSelectionModel().getSelectedItem();
        
        TreeItemExt newTreeItem = new TreeItemExt(newTag.getName(), "tag_16px_505072_easyicon.net.png");
        newTreeItem.getProperties().put(TreeItemExt.ATTR_ITEM_TAG_KEY, newTag);
        parentTreeItem.getChildren().addAll(newTreeItem);
        parentTreeItem.setExpanded(true);
        tree.getSelectionModel().select(newTreeItem);
    }

    private void updateTreeItem(ItemTag newTag) {
        TreeItemExt treeItem = (TreeItemExt) tree.getSelectionModel().getSelectedItem();
        treeItem.setValue(newTag.getName());
        
        Criterion c =  Restrictions.eq("tagId", newTag.getId());
        List<Item> lstItem = (List<Item>) controller.getItemsByCriterion.call(c);
        for (Item item : lstItem) {
            item.setTagName(newTag.getName());
            controller.saveOrUpdateItem.call(item);
        }
        
        loadTable(newTag);
    }
    
    private void loadTree(String tagId, TreeItemExt parentItem) {
        
        for (ItemTag c : lstAllTags) {
            if (c.getPid().equals(tagId)) {
                TreeItemExt childItem = new TreeItemExt(c.getName(), "tag_16px_505072_easyicon.net.png");
                childItem.getProperties().put(TreeItemExt.ATTR_ITEM_TAG_KEY, c);
                parentItem.getChildren().addAll(childItem);
                
                loadTree(c.getId(), childItem);
            }
        }
        parentItem.setExpanded(true);
    }
    
    private void insertTableRow(Item item) {
        ItemModel model = new ItemModel();
        BeanUtils.copyProperties(item, model);
        model.setNo((data.size() + 1) + "");
        data.add(model);
    }
    
    private void updateTableRow(Item item) {
        ItemModel model = (ItemModel) table.getSelectionModel().getSelectedItem();
        BeanUtils.copyProperties(item, model);
        model.setNo(model.getNo());
        table.refresh();
    }
    
    private void loadTable(ItemTag tag) {
        data.clear();
        List<Item> lstItem = null;
        // If it's root.
        if (tag.getPid().equals("0")) {
            lstItem = (List<Item>) controller.getItemsByCriterion.call(null);
        } else {
            Criterion c =  Restrictions.eq("tagId", tag.getId());
            lstItem = (List<Item>) controller.getItemsByCriterion.call(c);
        }
        
        int rowNum = 1;
        for (Item item : lstItem) {
            ItemModel itemModel = new ItemModel();
            BeanUtils.copyProperties(item, itemModel);
            itemModel.setNo((rowNum++) + "");
            data.add(itemModel);
        }
        
        disable(true);
    }
    
    private void loadTableDataByCriterion(Criterion c) {
        data.clear();
        List<Item> lstItem = (List<Item>) controller.getItemsByCriterion.call(c);
        int rowNum = 1;
        for (Item item : lstItem) {
            ItemModel itemModel = new ItemModel();
            BeanUtils.copyProperties(item, itemModel);
            itemModel.setNo((rowNum++) +"");
            
            data.add(itemModel);
        }
        
        disable(true);
    }
    
    private void removeTagItems(TreeItemExt parentItem) {
        Iterator<TreeItemExt> iter = parentItem.getChildren().iterator();
        while (iter.hasNext()) {
            TreeItemExt treeItem = iter.next();
            ItemTag tag = (ItemTag) treeItem.getProperties().get(TreeItemExt.ATTR_ITEM_TAG_KEY);
            
            Criterion c =  Restrictions.eq("tagId", tag.getId());
            List<Item> lstItem = (List<Item>) controller.getItemsByCriterion.call(c);
            for (Item item : lstItem) {
                controller.deleteItem.call(item);
            }
            removeTagItems(treeItem);
        }
    }
    
    private void showMessage(String message, Alert.AlertType type, Callback callback) {
        Dialog dia = new Dialog();
        DialogPane dp = dia.getDialogPane();
        dia.initOwner(MainApp.getInstance().getPrimaryStage());

        dia.setHeaderText("删除确认");

        dia.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Button okButton = (Button) dp.lookupButton(ButtonType.OK);
        okButton.setText("确定");
        Button cancelButton = (Button) dp.lookupButton(ButtonType.CANCEL);
        cancelButton.setText("取消");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 10, 10));

        grid.add(new Label(message), 0, 0);
        grid.add(deleteTagItems, 1, 0);
        
        VBox vbox = new VBox();
        vbox.getChildren().addAll(grid);
        dia.getDialogPane().setContent(vbox);
        
        dia.showAndWait()
            .filter(response -> response == ButtonType.OK)
            .ifPresent(response -> callback.call(""));
    }
    
    private CheckBox deleteTagItems = new CheckBox("同时删除标签下的商品");
    private Button btnAdd = new Button("添加新商品", ImageUtils.createImageView("plus_16px_505050_easyicon.net.png"));
    private Button btnUpdate = new Button("变更", ImageUtils.createImageView("edit_16px_505013_easyicon.net.png"));
    private Button btnRemove = new Button("删除", ImageUtils.createImageView("library_16px_505031_easyicon.net.png"));
    private Button btnExport = new Button("导出", ImageUtils.createImageView("future_projects_16px_505023_easyicon.net.png"));
    private TextField searchField = new TextField();
    private Button btnSearch = new Button("搜索", ImageUtils.createImageView("search_16px_505060_easyicon.net.png"));
    private Button btnGoback = new Button("返回");
    private MenuItem itemAdd = new MenuItem("添加新商品", ImageUtils.createImageView("plus_16px_505050_easyicon.net.png"));
    private MenuItem itemUpdate = new MenuItem("变更", ImageUtils.createImageView("edit_16px_505013_easyicon.net.png"));
    private MenuItem itemRemove = new MenuItem("删除", ImageUtils.createImageView("library_16px_505031_easyicon.net.png"));
    private MenuItem itemExport = new MenuItem("导出", ImageUtils.createImageView("future_projects_16px_505023_easyicon.net.png"));
    
    private List<ItemTag> lstAllTags;
    private TreeItemExt root = new TreeItemExt("全部商品", "tag_16px_505072_easyicon.net.png");
    private TreeView tree = new TreeView();
    private TableView table = new TableView();
    private final ObservableList<ItemModel> data = FXCollections.observableArrayList();
    private ItemManagementTabContentController controller = new ItemManagementTabContentController();
}
