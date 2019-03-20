/*
 * To change this license header, choose License Headers in Factory Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.ui;

import com.dockingsoftware.autorepairsystem.MainApp;
import com.dockingsoftware.autorepairsystem.persistence.entity.Factory;
import com.dockingsoftware.autorepairsystem.ui.controller.AddOrUpdateFactoryDialogController;
import java.util.function.Consumer;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class AddOrUpdateFactoryDialog extends Dialog {

    public AddOrUpdateFactoryDialog(Factory factory) {
        this.factory = factory;
        initComponents();
    }
    
    public AddOrUpdateFactoryDialog() {
        initComponents();
    }
    
    private void initComponents() {
        initOwner(MainApp.getInstance().getPrimaryStage());
        
        setTitle("添加送修厂");
        setHeaderText("请填写送修厂信息。");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        
        grid.add(new Label("送修厂名:"), 0, 0);
        nameField.setPromptText("送修厂名");
        grid.add(nameField, 1, 0);
       
        grid.add(new Label("联系人:"), 2, 0);
        contactsField.setPromptText("联系人");
        grid.add(contactsField, 3, 0);
        
        grid.add(new Label("联系电话:"), 0, 1);
        phoneNoField.setPromptText("联系电话");
        grid.add(phoneNoField, 1, 1);
        
        grid.add(new Label("备注:"), 2, 1);
        notesField.setPromptText("备注");
        grid.add(notesField, 3, 1);
        
        
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
        
        if (factory != null) {
            nameField.setText(factory.getName());
            contactsField.setText(factory.getContacts());
            phoneNoField.setText(factory.getPhoneNo() + "");
            notesField.setText(factory.getNotes());
            okButton.setDisable(false);
        } else {
            okButton.setDisable(true);
        }
    }
    
    public void saveOrUpdateFactory(Consumer<Factory> consumer) {
        if (factory == null) {
            factory = new Factory();
        }
        factory.setName(nameField.getText());
        factory.setContacts(contactsField.getText());
        factory.setPhoneNo(phoneNoField.getText());
        factory.setNotes(notesField.getText());
        
        controller.saveOrUpdateFactory.call(factory);
        consumer.accept(factory);
    }
    
    private TextField nameField = new TextField();
    private TextField contactsField = new TextField();
    private TextField phoneNoField = new TextField();
    private TextField notesField = new TextField();
   
    private AddOrUpdateFactoryDialogController controller = new AddOrUpdateFactoryDialogController();
    // 构造参数
    private Factory factory;
}
