/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.ui.controller;

import com.dockingsoftware.autorepairsystem.persistence.SettlementDAO;
import com.dockingsoftware.autorepairsystem.persistence.SettlementDetailsDAO;
import com.dockingsoftware.autorepairsystem.persistence.entity.Settlement;
import com.dockingsoftware.autorepairsystem.util.DAOUtils;
import javafx.util.Callback;
import org.hibernate.criterion.Criterion;

public class SettlementManagementTabContentController {

    public Callback getSettlementDetailsById = new Callback() {
        @Override
        public Object call(Object param) {
            return settlementDetailsDAO.getById((String) param);
        }
    };
    
    public Callback getSettlementById = new Callback() {
        @Override
        public Object call(Object param) {
            return settlementDAO.getById((String) param);
        }
    };
    
    public Callback listSettlement = new Callback() {
        @Override
        public Object call(Object param) {
            return settlementDAO.list((Criterion) param);
        }
    };
    
    public Callback saveOrUpdateSettlement = new Callback() {
        @Override
        public Object call(Object param) {
            settlementDAO.saveOrUpdate((Settlement) param);
            return "";
        }
    };
    
    public Callback deleteSettlement = new Callback() {
        @Override
        public Object call(Object param) {
            settlementDAO.delete((Settlement) param);
            return "";
        }
    };
    
    private SettlementDetailsDAO settlementDetailsDAO = DAOUtils.getInstance().getSettlementDetailsDAO();
    private SettlementDAO settlementDAO = DAOUtils.getInstance().getSettlementDAO();
}
