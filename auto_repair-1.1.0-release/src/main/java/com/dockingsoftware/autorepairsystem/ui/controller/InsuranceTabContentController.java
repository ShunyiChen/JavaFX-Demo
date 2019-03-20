/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.ui.controller;

import com.dockingsoftware.autorepairsystem.persistence.CustomerDAO;
import com.dockingsoftware.autorepairsystem.persistence.InsuranceDAO;
import com.dockingsoftware.autorepairsystem.persistence.ParameterDAO;
import com.dockingsoftware.autorepairsystem.persistence.entity.Customer;
import com.dockingsoftware.autorepairsystem.persistence.entity.Insurance;
import com.dockingsoftware.autorepairsystem.persistence.entity.Parameter;
import com.dockingsoftware.autorepairsystem.util.DAOUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.util.Callback;
import org.hibernate.criterion.Criterion;

public class InsuranceTabContentController {

    public Callback listAllParameters = new Callback() {
        @Override
        public Object call(Object param) {
            List<Parameter> lstAllParameters = parameterDAO.list(null);
            Map<String, String> keyValues = new HashMap<String, String>();
            for (Parameter p : lstAllParameters) {
                keyValues.put(p.getId(), p.getValue());
            }
            return keyValues;
        }
    };
    
    public Callback lstInsurance = new Callback() {
        @Override
        public Object call(Object param) {
            return insuranceDAO.list((Criterion) param);
        }
    };
    
    public Callback saveOrUpdateInsurance = new Callback() {
        @Override
        public Object call(Object param) {
            return insuranceDAO.saveOrUpdate((Insurance) param);
        }
    };
    
    public Callback getInsuranceByID = new Callback() {
        @Override
        public Object call(Object param) {
            return insuranceDAO.getById((String) param);
        }
    };
    
    public Callback deleteInsurance = new Callback() {
        @Override
        public Object call(Object param) {
            insuranceDAO.delete((Insurance) param);
            return "";
        }
    };
    
    public Callback saveOrUpdateCustomer = new Callback() {
        @Override
        public Object call(Object param) {
            return customerDAO.saveOrUpdate((Customer) param);
        }
    };
    
    public Callback getCustomerByID = new Callback() {
        @Override
        public Object call(Object param) {
            return customerDAO.getById((String) param);
        }
    };
    
    private CustomerDAO customerDAO = DAOUtils.getInstance().getCustomerDAO();
    private InsuranceDAO insuranceDAO = DAOUtils.getInstance().getInsuranceDAO();
    private ParameterDAO parameterDAO = DAOUtils.getInstance().getParameterDAO();
}
