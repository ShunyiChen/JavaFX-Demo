/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.ui;

import com.dockingsoftware.autorepairsystem.MainApp;
import com.dockingsoftware.autorepairsystem.persistence.entity.Subject;
import com.dockingsoftware.autorepairsystem.ui.controller.AddOrUpdateSubjectDialogController;
import java.util.function.Consumer;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class AddOrUpdateSubjectDialog extends Dialog {

    /**
     * Constructor.
     */
    public AddOrUpdateSubjectDialog() {
        setTitle("添加科目");
        setHeaderText("请填写科目信息。");
        initComponents();
    }
    
    /**
     * Constructor.
     * 
     * @param editIAE
     */
    public AddOrUpdateSubjectDialog(Subject editSubject) {
        this.editSubject = editSubject;
        setTitle("编辑科目");
        setHeaderText("请填写科目信息。");
        initComponents();
        
        if (editSubject != null) {
            nameField.setText(editSubject.getName());
            notesField.setText(editSubject.getNotes());
        }
    }

    private void initComponents() {
        initOwner(MainApp.getInstance().getPrimaryStage());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(new Label("科目名称:"), 0, 0);
        nameField.setPromptText("科目名称");
        grid.add(nameField, 1, 0);
        
        grid.add(new Label("备注:"), 2, 0);
        notesField.setPromptText("备注");
        grid.add(notesField, 3, 0);
        
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

    public void saveOrUpdateSubject(Consumer<Subject> consumer) {
        if (editSubject == null) {
            editSubject = new Subject();
        }
        editSubject.setName(nameField.getText());
        editSubject.setNotes(notesField.getText());
        
        controller.saveOrUpdateSubject.call(editSubject);
        
        consumer.accept(editSubject);
    }

    private TextField nameField = new TextField();
    private TextField notesField = new TextField();
    
    private Subject editSubject;
    private AddOrUpdateSubjectDialogController controller = new AddOrUpdateSubjectDialogController();
}
