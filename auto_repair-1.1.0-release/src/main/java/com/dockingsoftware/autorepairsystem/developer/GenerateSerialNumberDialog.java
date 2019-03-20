/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dockingsoftware.autorepairsystem.developer;

import com.dockingsoftware.autorepairsystem.Constants;
import com.dockingsoftware.autorepairsystem.MainApp;
import com.dockingsoftware.autorepairsystem.component.MessageBox;
import com.dockingsoftware.autorepairsystem.util.DateUtils;
import com.dockingsoftware.autorepairsystem.util.Encryptor;
import static com.dockingsoftware.autorepairsystem.util.PrintUtils.p;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author Chen
 */
public class GenerateSerialNumberDialog extends Dialog {
    
    public GenerateSerialNumberDialog() {
        initComponents();
    }
    
    private void initComponents() {
        initOwner(MainApp.getInstance().getPrimaryStage());
        
        setTitle("生成序列号");
        setHeaderText("生成序列号");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        
        grid.add(new Label("截止日期:"), 0, 0);
        grid.add(dateField, 1, 0);
        
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);
        okButton.setText("生成");
        Button cancelButton = (Button) getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelButton.setText("取消");

        getDialogPane().setContent(grid);
    }
    
    public void generate() {
        Date expirationDate = DateUtils.LocalDate2Date(dateField.getValue());
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
        String SN = "";
        try {
            SN = Encryptor.getInstance().encrypt(f.format(expirationDate));
        } catch (Exception ex) {
            org.apache.logging.log4j.Logger logger = LogManager.getLogger(GenerateSerialNumberDialog.class.getName());
            logger.error(ex);
            MessageBox.showMessage("序列号生成出错。");
        }
        p("新序列号："+SN);
    }
    
    private DatePicker dateField = new DatePicker();
}
