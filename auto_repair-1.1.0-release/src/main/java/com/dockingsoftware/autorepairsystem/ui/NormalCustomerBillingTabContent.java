/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.ui;

import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;

public class NormalCustomerBillingTabContent extends VBox {

    public NormalCustomerBillingTabContent(Tab tab) {
        this.tab = tab;
        initComponents();
    }
    
    private void initComponents() {
        pane = new SettlementDetailsPane(false);
        pane.setTab(tab);
        this.getChildren().addAll(pane);
    }
    
    public SettlementDetailsPane getPane() {
        return pane;
    }
    
    private SettlementDetailsPane pane;
    private Tab tab;
}
