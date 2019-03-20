package com.dockingsoftware.autorepairsystem.component;

import com.dockingsoftware.autorepairsystem.MainApp;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.util.Callback;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

public class MessageBox {

    public static void showMessage(String message, Alert.AlertType type, Callback callback) {
        Alert alert = new Alert(type, "");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(MainApp.getInstance().getPrimaryStage());
        alert.getDialogPane().setContentText(message);
        alert.getDialogPane().setHeaderText("提示");
        alert.setTitle("消息");
        Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setText("确定");
        if (type == Alert.AlertType.CONFIRMATION) {
            Button cancelButton = (Button) alert.getDialogPane().lookupButton(ButtonType.CANCEL);
            cancelButton.setText("取消");
        }
        alert.showAndWait()
            .filter(response -> response == ButtonType.OK)
            .ifPresent(response -> callback.call(""));
    }
    
    public static void showMessage(String message) {
        showMessage(message, Alert.AlertType.INFORMATION, callback);
    }
    
    public static void showMessage(String message, Alert.AlertType type) {
        showMessage(message, type, callback);
    }
    
    private static Callback callback = new Callback() {
        @Override
        public Object call(Object param) {
            // Do nothing.
            return "";
        }
    };
    
}
