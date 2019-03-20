/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.ui;

import com.dockingsoftware.autorepairsystem.MainApp;
import java.util.function.Consumer;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class ChooseOrderTypeDialog extends Dialog {

    public ChooseOrderTypeDialog() {
        initComponents();
    }

    private void initComponents() {
        initOwner(MainApp.getInstance().getPrimaryStage());

        setTitle("选择开单对象");
        setHeaderText("选择开单对象。");

        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
        okButton.setText("确定");
        Button cancelButton = (Button) getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelButton.setText("取消");

        // Do some validation (using the Java 8 lambda syntax).
//        password.textProperty().addListener((observable, oldValue, newValue) -> {
//            okButton.setDisable(newValue.trim().isEmpty());
//        });
        VBox vbox = new VBox();
        vbox.getChildren().addAll(createGridPane());
        getDialogPane().setContent(vbox);
    }

    private GridPane createGridPane() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 10, 10));
        ToggleGroup tg = new ToggleGroup();
        customer.setSelected(true);
        customer.setToggleGroup(tg);
        factory.setToggleGroup(tg);
        
        customer.setPrefWidth(103);
        grid.add(customer, 0, 0);
        grid.add(factory, 1, 0);
        return grid;
    }
    
    public void isNormalCustomer(Consumer<Boolean> consumer) {
        consumer.accept(customer.isSelected());
    }
    
    private RadioButton customer = new RadioButton("普通客户");
    private RadioButton factory = new RadioButton("送修厂客户");
 
}
