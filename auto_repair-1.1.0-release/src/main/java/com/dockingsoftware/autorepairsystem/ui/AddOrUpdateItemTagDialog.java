/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.ui;

import com.dockingsoftware.autorepairsystem.MainApp;
import com.dockingsoftware.autorepairsystem.persistence.entity.ItemTag;
import com.dockingsoftware.autorepairsystem.ui.controller.AddOrUpdateItemTagDialogController;
import java.util.function.Consumer;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class AddOrUpdateItemTagDialog extends Dialog {
    
    /**
     * 新增
     * 
     * @param pid
     * @param itemTag 
     */
    public AddOrUpdateItemTagDialog(String pid, ItemTag itemTag) {
        this.pid = pid;
        this.itemTag = itemTag;
        initComponents();
    }
    
     /**
     * 更改
     * 
     * @param itemTag 
     */
    public AddOrUpdateItemTagDialog(ItemTag itemTag) {
        this.itemTag = itemTag;
        initComponents();
    }
    
    private void initComponents() {
        initOwner(MainApp.getInstance().getPrimaryStage());
        if (itemTag == null) {
            setTitle("添加标签");
            setHeaderText("请添加新标签。");
        } else {
            setTitle("更改标签");
            setHeaderText("请输入标签名。");
            
            nameField.setText(itemTag.getName());
        }
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("商品标签:"), 0, 0);
        nameField.setPromptText("商品标签");
        grid.add(nameField, 1, 0);
        
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(true);
        okButton.setText("确定");
        Button cancelButton = (Button) getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelButton.setText("取消");
        // Do some validation (using the Java 8 lambda syntax).
        nameField.textProperty().addListener((observable, oldValue, newValue) -> {
            okButton.setDisable(newValue.trim().isEmpty());
        });
        
        getDialogPane().setContent(grid);
    }
    
    public void saveOrUpdateItemTag(Consumer<ItemTag> consumer) {
        if (itemTag == null) {
            itemTag = new ItemTag();
            itemTag.setPid(pid);
        }
        itemTag.setName(nameField.getText());
        
        controller.saveOrUpdateItemTag.call(itemTag);
        
        consumer.accept(itemTag);
    }
    
    private String pid;
    private ItemTag itemTag;
    private TextField nameField = new TextField();
    private AddOrUpdateItemTagDialogController controller = new AddOrUpdateItemTagDialogController();
}
