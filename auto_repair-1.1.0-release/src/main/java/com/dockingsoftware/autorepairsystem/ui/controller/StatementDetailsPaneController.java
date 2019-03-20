/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.ui.controller;

import com.dockingsoftware.autorepairsystem.persistence.BusinessTypeDAO;
import com.dockingsoftware.autorepairsystem.persistence.CustomerDAO;
import com.dockingsoftware.autorepairsystem.persistence.ItemTagDAO;
import com.dockingsoftware.autorepairsystem.util.DAOUtils;
import javafx.util.Callback;
import com.dockingsoftware.autorepairsystem.persistence.SettlementDAO;
import com.dockingsoftware.autorepairsystem.persistence.SettlementDetailsDAO;
import com.dockingsoftware.autorepairsystem.persistence.entity.Customer;
import com.dockingsoftware.autorepairsystem.persistence.entity.Settlement;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

public class StatementDetailsPaneController {

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
    
    public Callback listBusinessType = new Callback() {
        @Override
        public Object call(Object param) {
            return businessTypeDAO.list();
        }
    };
    
    public Callback listSettlementByFactoryId = new Callback() {
        @Override
        public Object call(Object param) {
            Criterion c = Restrictions.eq("clientId", param.toString());
            return settlementDAO.list(c);
        }
    };
    
    public Callback listSettlementDetailsByCustomerId = new Callback() {
        @Override
        public Object call(Object param) {
            Criterion c = Restrictions.eq("customerId", param.toString());
            return settlementDetailsDAO.list(c);
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
    
    public Callback getRootItemTag = new Callback() {
        @Override
        public Object call(Object param) {
            return itemTagDAO.getByPID("0");
        }
    };
    
    public Callback getSettlementById = new Callback() {
        @Override
        public Object call(Object param) {
            return settlementDAO.getById((String) param);
        }
    };
    
    private CustomerDAO customerDAO = DAOUtils.getInstance().getCustomerDAO();
    private BusinessTypeDAO businessTypeDAO = DAOUtils.getInstance().getBusinessTypeDAO();
    private ItemTagDAO itemTagDAO = DAOUtils.getInstance().getItemTagDAO();
    private SettlementDAO settlementDAO = DAOUtils.getInstance().getSettlementDAO();
    private SettlementDetailsDAO settlementDetailsDAO = DAOUtils.getInstance().getSettlementDetailsDAO();
}
