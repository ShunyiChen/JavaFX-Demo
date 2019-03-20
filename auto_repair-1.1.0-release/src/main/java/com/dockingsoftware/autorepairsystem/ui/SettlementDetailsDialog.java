/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.ui;

import com.dockingsoftware.autorepairsystem.MainApp;
import com.dockingsoftware.autorepairsystem.persistence.entity.Customer;
import com.dockingsoftware.autorepairsystem.persistence.entity.Settlement;
import com.dockingsoftware.autorepairsystem.persistence.entity.SettlementDetails;
import com.dockingsoftware.autorepairsystem.ui.controller.SettlementDetailsDialogController;
import java.util.function.Consumer;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.VBox;

public class SettlementDetailsDialog extends Dialog {

    /**
     * Constructor.
     * 
     */
    public SettlementDetailsDialog() {
        initComponents();
    }
    
    /**
     * Constructor.
     * 
     * @param editSettlementDetails 
     */
    public SettlementDetailsDialog(SettlementDetails editSettlementDetails) {
        this.editSettlementDetails = editSettlementDetails;
        initComponents();
        pane.setInputFieldValues(editSettlementDetails);
    }

    private void initComponents() {
        initOwner(MainApp.getInstance().getPrimaryStage());
        if (editSettlementDetails != null) {
            setTitle("编辑车辆");
        } else {
            setTitle("添加车辆");
        }
        setHeaderText("请填写车辆维修明细。");
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
        if (editSettlementDetails == null) {
            okButton.setText("添加");
        } else {
            okButton.setText("保存");
        }
        Button cancelButton = (Button) getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelButton.setText("取消");
        VBox vbox = new VBox();
        
        okButton.setDisable(true);
        
        pane.getLicensePlateNumberField().textProperty().addListener((observable, oldValue, newValue) -> {
            okButton.setDisable(pane.getLicensePlateNumberField().getText().isEmpty());
        });
        
        vbox.getChildren().addAll(pane);
        getDialogPane().setContent(vbox);
        getDialogPane().setPrefWidth(1200);
        getDialogPane().setPrefHeight(700);
    }

    public void insertSettlementDetails(Consumer<SettlementDetails> consumer) {
        
        editSettlementDetails = pane.createSettlementDetails(null);
        editSettlementDetails.setSN(editSettlement.getSN());
        editSettlement.getDetails().add(editSettlementDetails);
        controller.saveOrUpdateSettlement.call(editSettlement);
        
        updateCustomerLatestMileage(editSettlementDetails.getCustomerId(), editSettlementDetails.getLatestMileage());
        
        consumer.accept(editSettlementDetails);
    }
    
    public void updateSettlementDetails(Consumer<SettlementDetails> consumer) {
        pane.createSettlementDetails(editSettlementDetails);
        
        controller.saveOrUpdateSettlementDetails.call(editSettlementDetails);
        
        updateCustomerLatestMileage(editSettlementDetails.getCustomerId(), editSettlementDetails.getLatestMileage());
        
        consumer.accept(editSettlementDetails);
    }
    
    private void updateCustomerLatestMileage(String customerId, int latestMileage) {
        Customer c = (Customer) controller.getCustomerById.call(customerId);
        c.setLatestMileage(latestMileage);
        controller.saveOrUpdateCustomer.call(c);
    }
    
    public void setEditSettlement(Settlement editSettlement) {
        this.editSettlement = editSettlement;
    }
    
    private SettlementDetailsDialogController controller = new SettlementDetailsDialogController();
    private SettlementDetailsPane pane = new SettlementDetailsPane(true);
    private Settlement editSettlement;
    private SettlementDetails editSettlementDetails;
    
}
