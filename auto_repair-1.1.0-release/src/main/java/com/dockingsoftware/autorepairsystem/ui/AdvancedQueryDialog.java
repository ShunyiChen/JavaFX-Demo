/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.ui;

import com.dockingsoftware.autorepairsystem.MainApp;
import javafx.scene.control.Dialog;

public class AdvancedQueryDialog extends Dialog {

    public AdvancedQueryDialog() {
        initComponents();
    }
    
    private void initComponents() {
        initOwner(MainApp.getInstance().getPrimaryStage());
        
        setTitle("高级查询");
        setHeaderText("请组合多条件查询。");
    }
    
    
    
}
