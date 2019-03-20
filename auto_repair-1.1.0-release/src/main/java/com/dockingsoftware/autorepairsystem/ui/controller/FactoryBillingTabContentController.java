/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.ui.controller;

import com.dockingsoftware.autorepairsystem.persistence.FactoryDAO;
import com.dockingsoftware.autorepairsystem.persistence.SettlementDAO;
import com.dockingsoftware.autorepairsystem.persistence.SettlementDetailsDAO;
import com.dockingsoftware.autorepairsystem.persistence.entity.Settlement;
import com.dockingsoftware.autorepairsystem.util.DAOUtils;
import javafx.util.Callback;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

public class FactoryBillingTabContentController {

    public Callback getFactoryById = new Callback() {
        @Override
        public Object call(Object param) {
            return factoryDAO.getById((String) param);
        }
    };
    
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
    
    public Callback listSettlementByFactoryId = new Callback() {
        @Override
        public Object call(Object param) {
            Criterion c = Restrictions.eq("clientId", param.toString());
            return settlementDAO.list(c);
        }
    };
    
    public Callback saveOrUpdateSettlement = new Callback() {
        @Override
        public Object call(Object param) {
            return settlementDAO.saveOrUpdate((Settlement) param);
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
    private FactoryDAO factoryDAO = DAOUtils.getInstance().getFactoryDAO();
}
