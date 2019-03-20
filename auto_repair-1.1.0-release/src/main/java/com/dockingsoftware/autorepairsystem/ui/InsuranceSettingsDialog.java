/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.ui;

import com.dockingsoftware.autorepairsystem.MainApp;
import com.dockingsoftware.autorepairsystem.persistence.entity.Insurance;
import com.dockingsoftware.autorepairsystem.persistence.entity.InsuranceItem;
import com.dockingsoftware.autorepairsystem.ui.controller.InsuranceSettingsDialogController;
import java.util.List;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.springframework.util.StringUtils;

/**
 * 设置商业险项目
 * 
 * @author Shunyi Chen
 */
public class InsuranceSettingsDialog extends Dialog {
    
    /**
     * Constructor.
     * 
     * @param editInsurance 
     */
    public InsuranceSettingsDialog(Insurance editInsurance) {
        this.editInsurance = editInsurance;
        initComponents();
    }

    private void initComponents() {
        initOwner(MainApp.getInstance().getPrimaryStage());

        setTitle("设置");
        setHeaderText("设置商业险项目。");

        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
        okButton.setText("确定");
        Button cancelButton = (Button) getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelButton.setText("取消");

        // Do some validation (using the Java 8 lambda syntax).
//        password.textProperty().addListener((observable, oldValue, newValue) -> {
//            okButton.setDisable(newValue.trim().isEmpty());
//        });
        getDialogPane().setContent(createItemsPane());
    }
    
    private VBox createItemsPane() {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(20, 20, 10, 10));
        HBox h1 = new HBox();
        Button btnAdd = new Button("添加");
        Button btnRemove = new Button("删除");
        h1.setAlignment(Pos.CENTER);
        h1.getChildren().addAll(new Label("商业险项目:"), insuranceItemField, btnAdd, btnRemove);
        HBox.setHgrow(insuranceItemField, Priority.ALWAYS);
        insuranceItemField.setText("");
        insuranceItemField.setPromptText("商业险项目名");
        
        for (InsuranceItem bt : editInsurance.getInsuranceItems()) {
            insuranceItemListView.getItems().add(bt);
        }
        insuranceItemListView.setPrefHeight(200);
        vbox.getChildren().addAll(h1, insuranceItemListView);
        
        btnAdd.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                if (!StringUtils.isEmpty(insuranceItemField.getText())) {
                    insuranceItemListView.getItems().add(InsuranceItem.create(insuranceItemField.getText()));
                }
            }
        });
        
        btnRemove.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                InsuranceItem bt = insuranceItemListView.getSelectionModel().getSelectedItem();
                insuranceItemListView.getItems().remove(bt);
            }
        });
        
        return vbox;
    }
    
    public void saveSettings() {
        editInsurance.getInsuranceItems().clear();
        
        for (InsuranceItem ii : insuranceItemListView.getItems()) {
            editInsurance.getInsuranceItems().add(ii);
        }
        controller.saveOrUpdateInsurance.call(editInsurance);
    }
    
    private List<InsuranceItem> listInsuranceItem;
    private TextField insuranceItemField = new TextField();
    private ListView<InsuranceItem> insuranceItemListView = new ListView<InsuranceItem>();
    private Insurance editInsurance = null;
    private InsuranceSettingsDialogController controller = new InsuranceSettingsDialogController();
}
