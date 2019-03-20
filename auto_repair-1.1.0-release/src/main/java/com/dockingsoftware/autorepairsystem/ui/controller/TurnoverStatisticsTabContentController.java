/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.ui.controller;

import com.dockingsoftware.autorepairsystem.persistence.SettlementDetailsDAO;
import com.dockingsoftware.autorepairsystem.util.DAOUtils;
import javafx.util.Callback;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

public class TurnoverStatisticsTabContentController {
    
    public Callback listSettlementDetails = new Callback() {
        @Override
        public Object call(Object param) {
            return settlementDetailsDAO.list((Criterion) param, Order.asc("billingDate"));
        }
    };
    
    private SettlementDetailsDAO settlementDetailsDAO = DAOUtils.getInstance().getSettlementDetailsDAO();
}
