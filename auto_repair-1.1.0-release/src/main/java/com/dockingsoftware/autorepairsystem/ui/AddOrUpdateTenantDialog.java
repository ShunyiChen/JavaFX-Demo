/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dockingsoftware.autorepairsystem.ui;

import com.dockingsoftware.autorepairsystem.MainApp;
import com.dockingsoftware.autorepairsystem.persistence.entity.Tenant;
import com.dockingsoftware.autorepairsystem.ui.controller.AddOrUpdateTenantDialogController;
import java.util.function.Consumer;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class AddOrUpdateTenantDialog extends Dialog {

    /**
     * Constructor.
     */
    public AddOrUpdateTenantDialog() {
        setTitle("添加租户");
        setHeaderText("请填写租户信息。");
        initComponents();
    }
    
    /**
     * Constructor.
     * 
     * @param editTenant 
     */
    public AddOrUpdateTenantDialog(Tenant editTenant) {
        this.editTenant = editTenant;
        setTitle("更改租户");
        setHeaderText("请填写租户信息。");
        initComponents();
        
        if (editTenant != null) {
            nameField.setText(editTenant.getName());
            contactsField.setText(editTenant.getContacts());
            phoneNoField.setText(editTenant.getPhoneNo());
            notesField.setText(editTenant.getNotes());
        }
    }

    private void initComponents() {
        initOwner(MainApp.getInstance().getPrimaryStage());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(new Label("租户名:"), 0, 0);
        nameField.setPromptText("租户名");
        grid.add(nameField, 1, 0);

        grid.add(new Label("联系人:"), 2, 0);
        contactsField.setPromptText("联系人");
        grid.add(contactsField, 3, 0);

        grid.add(new Label("联系电话:"), 0, 1);
        phoneNoField.setPromptText("联系电话");
        grid.add(phoneNoField, 1, 1);
 
        grid.add(new Label("备注:"), 2, 1);
        notesField.setPromptText("备注");
        notesField.setPrefWidth(173);
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
    }

    public void saveOrUpdateTenant(Consumer<Tenant> consumer) {
        if (editTenant == null) {
            editTenant = new Tenant();
        }
        editTenant.setName(nameField.getText());
        editTenant.setContacts(contactsField.getText());
        editTenant.setPhoneNo(phoneNoField.getText());
        editTenant.setNotes(notesField.getText());
        
        controller.saveOrUpdateTentant.call(editTenant);
        
        consumer.accept(editTenant);
    }

    private TextField nameField = new TextField();
    private TextField contactsField = new TextField();
    private TextField phoneNoField = new TextField();
    private TextField notesField = new TextField();
    private Tenant editTenant;
    private AddOrUpdateTenantDialogController controller = new AddOrUpdateTenantDialogController();
}
