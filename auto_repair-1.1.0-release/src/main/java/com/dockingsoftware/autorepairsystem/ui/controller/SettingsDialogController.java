/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.ui.controller;

import com.dockingsoftware.autorepairsystem.persistence.BusinessTypeDAO;
import com.dockingsoftware.autorepairsystem.persistence.ParameterDAO;
import com.dockingsoftware.autorepairsystem.persistence.PaymentDAO;
import com.dockingsoftware.autorepairsystem.persistence.ProjectCategoryDAO;
import com.dockingsoftware.autorepairsystem.persistence.VehicleTypeDAO;
import com.dockingsoftware.autorepairsystem.persistence.entity.BusinessType;
import com.dockingsoftware.autorepairsystem.persistence.entity.Parameter;
import com.dockingsoftware.autorepairsystem.persistence.entity.Payment;
import com.dockingsoftware.autorepairsystem.persistence.entity.ProjectCategory;
import com.dockingsoftware.autorepairsystem.persistence.entity.VehicleType;
import com.dockingsoftware.autorepairsystem.util.DAOUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.util.Callback;

public class SettingsDialogController {
    
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
    
    public Callback saveOrUpdateParameters = new Callback() {
        @Override
        public Object call(Object param) {
            return parameterDAO.saveOrUpdate((Parameter) param);
        }
    };
    
    public Callback listVehicleType = new Callback() {
        @Override
        public Object call(Object param) {
            return vehicleTypeDAO.list();
        }
    };
    
    public Callback saveOrUpdateVehicleType = new Callback() {
        @Override
        public Object call(Object param) {
            return vehicleTypeDAO.saveOrUpdate((VehicleType) param);
        }
    };
    
    public Callback deleteVehicleType = new Callback() {
        @Override
        public Object call(Object param) {
            vehicleTypeDAO.delete((VehicleType) param);
            return "";
        }
    };
    
    public Callback listBusinessType = new Callback() {
        @Override
        public Object call(Object param) {
            return businessTypeDAO.list();
        }
    };
    
    public Callback saveOrUpdateBusinessType = new Callback() {
        @Override
        public Object call(Object param) {
            return businessTypeDAO.saveOrUpdate((BusinessType) param);
        }
    };
    
    public Callback deleteBusinessType = new Callback() {
        @Override
        public Object call(Object param) {
            businessTypeDAO.delete((BusinessType) param);
            return "";
        }
    };
    
    public Callback listProjectCategory = new Callback() {
        @Override
        public Object call(Object param) {
            return projectCategoryDAO.list();
        }
    };
    
    public Callback saveOrUpdateProjectCategory = new Callback() {
        @Override
        public Object call(Object param) {
            return projectCategoryDAO.saveOrUpdate((ProjectCategory) param);
        }
    };
    
    public Callback deleteProjectCategory = new Callback() {
        @Override
        public Object call(Object param) {
            projectCategoryDAO.delete((ProjectCategory) param);
            return "";
        }
    };
    
    public Callback listPayment = new Callback() {
        @Override
        public Object call(Object param) {
            return paymentDAO.list();
        }
    };
    
    public Callback saveOrUpdatePayment = new Callback() {
        @Override
        public Object call(Object param) {
            return paymentDAO.saveOrUpdate((Payment) param);
        }
    };
    
    public Callback deletePayment = new Callback() {
        @Override
        public Object call(Object param) {
            paymentDAO.delete((Payment) param);
            return "";
        }
    };
    
    private ProjectCategoryDAO projectCategoryDAO = DAOUtils.getInstance().getProjectCategoryDAO();
    private BusinessTypeDAO businessTypeDAO = DAOUtils.getInstance().getBusinessTypeDAO();
    private VehicleTypeDAO vehicleTypeDAO = DAOUtils.getInstance().getVehicleTypeDAO();
    private ParameterDAO parameterDAO = DAOUtils.getInstance().getParameterDAO();
    private PaymentDAO paymentDAO = DAOUtils.getInstance().getPaymentDAO();
}
