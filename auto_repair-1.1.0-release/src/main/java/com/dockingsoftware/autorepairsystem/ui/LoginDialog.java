/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dockingsoftware.autorepairsystem.ui;

import com.dockingsoftware.autorepairsystem.MainApp;
import com.dockingsoftware.autorepairsystem.persistence.entity.User;
import com.dockingsoftware.autorepairsystem.ui.controller.LoginDialogController;
import java.util.function.Consumer;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class LoginDialog extends Dialog {

    public LoginDialog() {
        initComponents();
    }

    private void initComponents() {
        initOwner(MainApp.getInstance().getPrimaryStage());

        setTitle("登录");
        setHeaderText("用户登录");

        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
        okButton.setText("登录");
        Button cancelButton = (Button) getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelButton.setText("取消");
        // Do some validation (using the Java 8 lambda syntax).
        userName.textProperty().addListener((observable, oldValue, newValue) -> {
            okButton.setDisable(newValue.trim().isEmpty());
        });
        VBox vbox = new VBox();
        vbox.getChildren().addAll(createGridPane());
        getDialogPane().setContent(vbox);
    }

    public void login(Consumer<User> consumer) {
        User user = (User) controller.getUserByNameAndPassword.call(new String[]{userName.getText(), password.getText()});
        // 返回未加密的密码
        user.setPassword(password.getText());
        consumer.accept(user);
    }

    private GridPane createGridPane() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 10, 10));

        grid.add(new Label("用户名:"), 0, 0);
        userName.setText("admin");
        userName.setPromptText("用户名");
        grid.add(userName, 1, 0);

        grid.add(new Label("密码:"), 0, 1);
        password.setPromptText("密码");
        grid.add(password, 1, 1);

        return grid;
    }
    
    private TextField userName = new TextField();
    private PasswordField password = new PasswordField();
    private LoginDialogController controller = new LoginDialogController();
}
