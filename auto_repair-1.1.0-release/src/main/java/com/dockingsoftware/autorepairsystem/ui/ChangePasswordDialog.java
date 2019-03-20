/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.ui;

import com.dockingsoftware.autorepairsystem.MainApp;
import com.dockingsoftware.autorepairsystem.persistence.entity.User;
import com.dockingsoftware.autorepairsystem.ui.controller.ChangePasswordDialogController;
import com.dockingsoftware.autorepairsystem.util.MD5Utils;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class ChangePasswordDialog extends Dialog {

    public ChangePasswordDialog() {
        initComponents();
    }

    private void initComponents() {
        initOwner(MainApp.getInstance().getPrimaryStage());

        setTitle("修改密码");
        setHeaderText("修改用户密码");

        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
        okButton.setText("确定");
        Button cancelButton = (Button) getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelButton.setText("取消");

        // Do some validation (using the Java 8 lambda syntax).
        password.textProperty().addListener((observable, oldValue, newValue) -> {
            okButton.setDisable(newValue.trim().isEmpty());
        });
        VBox vbox = new VBox();
        vbox.getChildren().addAll(createGridPane());
        getDialogPane().setContent(vbox);
    }

    public void change() {
        User user = (User) controller.getUserById.call(MainApp.getInstance().getLoggedUser().getId());
        user.setPassword(MD5Utils.md5(password.getText()));
        controller.saveOrUpdatePassword.call(user);
        MainApp.getInstance().getLoggedUser().setPassword(password.getText());
    }

    private GridPane createGridPane() {

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 10, 10));

        grid.add(new Label("输入新密码:"), 0, 0);
        password.setText(MainApp.getInstance().getLoggedUser().getPassword());
        password.setPromptText("密码");
        grid.add(password, 1, 0);

        return grid;
    }

    private TextField password = new TextField();
    private ChangePasswordDialogController controller = new ChangePasswordDialogController();
}
