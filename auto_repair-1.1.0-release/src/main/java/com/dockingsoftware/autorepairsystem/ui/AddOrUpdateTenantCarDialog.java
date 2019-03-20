/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.ui;

import com.dockingsoftware.autorepairsystem.MainApp;
import com.dockingsoftware.autorepairsystem.component.model.TenantCarModel;
import com.dockingsoftware.autorepairsystem.persistence.entity.TenantCar;
import com.dockingsoftware.autorepairsystem.util.ValidateUtils;
import java.util.function.Consumer;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class AddOrUpdateTenantCarDialog extends Dialog {

    public AddOrUpdateTenantCarDialog() {
        initComponents();
    }
    
    public AddOrUpdateTenantCarDialog(TenantCarModel model) {
        initComponents();
        
        licensePlateNumber.setText(model.getLicensePlateNumber());
        paintParts.setText(model.getPaintParts());
        notes.setText(model.getNotes());
    }
    
    private void initComponents() {
        initOwner(MainApp.getInstance().getPrimaryStage());
        
        setTitle("添加租户车辆");
        setHeaderText("请填写车辆信息。");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        
        grid.add(new Label("车牌号:"), 0, 0);
        licensePlateNumber.setPromptText("车牌号");
        grid.add(licensePlateNumber, 1, 0);
       
        grid.add(new Label("烤漆部位:"), 0, 1);
        paintParts.setPromptText("烤漆部位");
        grid.add(paintParts, 1, 1);
        
        grid.add(new Label("备注:"), 0, 2);
        notes.setPromptText("备注");
        grid.add(notes, 1, 2);
        
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(true);
        okButton.setText("确定");
        Button cancelButton = (Button) getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelButton.setText("取消");
        // Do some validation (using the Java 8 lambda syntax).
        licensePlateNumber.textProperty().addListener((observable, oldValue, newValue) -> {
            boolean bool = ValidateUtils.isCarnumberNO(licensePlateNumber.getText());
            okButton.setDisable(!bool);
        });
        
        getDialogPane().setContent(grid);
    
        okButton.setDisable(true);
    }
    
    public void returnTenantCar(Consumer<TenantCar> consumer) {
        if (car == null) {
            car = new TenantCar();
        }
        car.setLicensePlateNumber(licensePlateNumber.getText());
        car.setPaintParts(paintParts.getText());
        car.setNotes(notes.getText());

        consumer.accept(car);
    }
    
    private TextField licensePlateNumber = new TextField();
    private TextField paintParts = new TextField();
    private TextField notes = new TextField();
    
    // 构造参数
    private TenantCar car;
}
