/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.ui.controller;

import com.dockingsoftware.autorepairsystem.persistence.SettlementDAO;
import com.dockingsoftware.autorepairsystem.persistence.SettlementDetailsDAO;
import com.dockingsoftware.autorepairsystem.util.DAOUtils;
import javafx.util.Callback;
import org.hibernate.criterion.Criterion;

public class ViewHistoryTabContentController {

    public Callback listSettlement = new Callback() {
        @Override
        public Object call(Object param) {
            return settlementDAO.list((Criterion) param);
        }
    };
    
    private SettlementDAO settlementDAO = DAOUtils.getInstance().getSettlementDAO();
}
