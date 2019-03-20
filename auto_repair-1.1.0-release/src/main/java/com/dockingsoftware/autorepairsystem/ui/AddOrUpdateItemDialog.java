/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.ui;

import com.dockingsoftware.autorepairsystem.MainApp;
import com.dockingsoftware.autorepairsystem.component.NumericTextField;
import com.dockingsoftware.autorepairsystem.persistence.entity.Item;
import com.dockingsoftware.autorepairsystem.persistence.entity.ItemTag;
import com.dockingsoftware.autorepairsystem.ui.controller.AddOrUpdateItemDialogController;
import java.math.BigDecimal;
import java.util.function.Consumer;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class AddOrUpdateItemDialog extends Dialog {
    
    public AddOrUpdateItemDialog(ItemTag tag, Item item) {
        this.tag = tag;
        this.item = item;
        initComponents();
    }
    
    private void initComponents() {
        initOwner(MainApp.getInstance().getPrimaryStage());
        
        setTitle("添加商品");
        setHeaderText("请输入商品信息。");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        
        grid.add(new Label("商品名称:"), 0, 0);
        grid.add(name, 1, 0);
        
        grid.add(new Label("数量:"), 2, 0);
        grid.add(quantity, 3, 0);
        
        grid.add(new Label("成本单价:"), 0, 1);
        grid.add(costPrice, 1, 1);
         
        grid.add(new Label("销售单价:"), 2, 1);
        grid.add(salesPrice, 3, 1);
        
        grid.add(new Label("备注:"), 0, 2);
        grid.add(notes, 1, 2, 3, 1);
        
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(true);
        okButton.setText("确定");
        Button cancelButton = (Button) getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelButton.setText("取消");
        // Do some validation (using the Java 8 lambda syntax).
        name.textProperty().addListener((observable, oldValue, newValue) -> {
            okButton.setDisable(newValue.trim().isEmpty());
        });
        
        getDialogPane().setContent(grid);
        
        if (item != null) {
            setTitle("更改商品");
            setHeaderText("请输入商品信息。");
            name.setText(item.getName());
            costPrice.setText(item.getCostPrice().toString());
            salesPrice.setText(item.getSalesPrice().toString());
            notes.setText(item.getNotes());
            okButton.setDisable(false);
        } else {
            okButton.setDisable(true);
        }
    }
    
    public void saveOrUpdateItem(Consumer<Item> consumer) {
        if (item == null) {
            item = new Item();
            item.setTagId(tag.getId());
            item.setTagName(tag.getName());
        }
        item.setName(name.getText());
        item.setCostPrice(new BigDecimal(costPrice.getText().isEmpty() ? "0" : costPrice.getText()));
        item.setSalesPrice(new BigDecimal(salesPrice.getText().isEmpty() ? "0" : salesPrice.getText()));
        item.setQuantity(Float.parseFloat(quantity.getText().isEmpty() ? "0" : quantity.getText()));
        item.setNotes(notes.getText());
        controller.saveOrUpdateItem.call(item);
        consumer.accept(item);
    }
    
    public void setQuantityDisable() {
        quantity.setDisable(true);
    }
    
    private TextField name = new TextField();
    private NumericTextField quantity = new NumericTextField();
    private NumericTextField costPrice = new NumericTextField();
    private NumericTextField salesPrice = new NumericTextField();
    private TextField notes = new TextField();
    private AddOrUpdateItemDialogController controller = new AddOrUpdateItemDialogController();
    
    // 构造参数
    private ItemTag tag;
    private Item item;
}
