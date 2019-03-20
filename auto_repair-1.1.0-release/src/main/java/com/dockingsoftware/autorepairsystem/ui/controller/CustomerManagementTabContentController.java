/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.ui.controller;

import com.dockingsoftware.autorepairsystem.persistence.CustomerDAO;
import com.dockingsoftware.autorepairsystem.persistence.entity.Customer;
import com.dockingsoftware.autorepairsystem.util.DAOUtils;
import javafx.util.Callback;
import org.hibernate.criterion.Criterion;

public class CustomerManagementTabContentController {

    public Callback getCustomerByID = new Callback() {
        @Override
        public Object call(Object param) {
            return customerDAO.getById((String) param);
        }
    };
    
    public Callback listCustomer = new Callback() {
        @Override
        public Object call(Object param) {
            return customerDAO.list((Criterion) param);
        }
    };
    
    public Callback saveOrUpdateCustomer = new Callback() {
        @Override
        public Object call(Object param) {
            return customerDAO.saveOrUpdate((Customer) param);
        }
    };
    
    public Callback deleteCustomer = new Callback() {
        @Override
        public Object call(Object param) {
            customerDAO.delete((Customer) param);
            return "";
        }
    };
    
    private CustomerDAO customerDAO = DAOUtils.getInstance().getCustomerDAO();
}
