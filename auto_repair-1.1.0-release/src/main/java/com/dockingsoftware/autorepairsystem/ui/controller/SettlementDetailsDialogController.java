/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.ui.controller;

import com.dockingsoftware.autorepairsystem.persistence.CustomerDAO;
import com.dockingsoftware.autorepairsystem.persistence.SettlementDAO;
import com.dockingsoftware.autorepairsystem.persistence.SettlementDetailsDAO;
import com.dockingsoftware.autorepairsystem.persistence.entity.Customer;
import com.dockingsoftware.autorepairsystem.persistence.entity.Settlement;
import com.dockingsoftware.autorepairsystem.persistence.entity.SettlementDetails;
import com.dockingsoftware.autorepairsystem.util.DAOUtils;
import javafx.util.Callback;

public class SettlementDetailsDialogController {

    public Callback getCustomerById = new Callback() {
        @Override
        public Object call(Object param) {
            return customerDAO.getById((String) param);
        }
    };
    
    public Callback saveOrUpdateCustomer = new Callback() {
        @Override
        public Object call(Object param) {
            return customerDAO.saveOrUpdate((Customer) param);
        }
    };
    
    public Callback saveOrUpdateSettlement = new Callback() {
        @Override
        public Object call(Object param) {
            return settlementDAO.saveOrUpdate((Settlement) param);
        }
    };
    
    public Callback saveOrUpdateSettlementDetails = new Callback() {
        @Override
        public Object call(Object param) {
            return settlementDetailsDAO.saveOrUpdate((SettlementDetails) param);
        }
    };
    
    private CustomerDAO customerDAO = DAOUtils.getInstance().getCustomerDAO();
    private SettlementDAO settlementDAO = DAOUtils.getInstance().getSettlementDAO();
    private SettlementDetailsDAO settlementDetailsDAO = DAOUtils.getInstance().getSettlementDetailsDAO();
}
