/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.ui;

import com.dockingsoftware.autorepairsystem.MainApp;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class InputLicenseDialog extends Dialog {

    public InputLicenseDialog() {
        initComponents();
    }

    private void initComponents() {
        initOwner(MainApp.getInstance().getPrimaryStage());

        setTitle("许可证");
        setHeaderText("若要继续，请为此产品输入有效的序列号。");

        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
        okButton.setText("确定");
        Button cancelButton = (Button) getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelButton.setText("取消");

        // Do some validation (using the Java 8 lambda syntax).
        licenceField.textProperty().addListener((observable, oldValue, newValue) -> {
            okButton.setDisable(newValue.trim().isEmpty());
        });
        VBox vbox = new VBox();
        vbox.getChildren().addAll(createGridPane());
        getDialogPane().setContent(vbox);
    }

    private GridPane createGridPane() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 10, 10));

        grid.add(new Label("序列号:"), 0, 0);
        licenceField.setPrefWidth(400);
        licenceField.setPromptText("序列号");
        grid.add(licenceField, 1, 0);

        return grid;
    }
    
    public String returnSerialNumber() {
        return licenceField.getText();
    }

    private TextField licenceField = new TextField();
}
