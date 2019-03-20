/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.ui.controller;

import com.dockingsoftware.autorepairsystem.persistence.CustomerDAO;
import com.dockingsoftware.autorepairsystem.persistence.VehicleTypeDAO;
import com.dockingsoftware.autorepairsystem.persistence.entity.Customer;
import com.dockingsoftware.autorepairsystem.util.DAOUtils;
import javafx.util.Callback;

public class AddOrUpdateInsuranceDialogController {

//    public Callback getCustomerByLicensePlateNumber = new Callback() {
//        @Override
//        public Object call(Object param) {
//            return customerDAO.getByLicensePlateNumber((String) param);
//        }
//    };
    
    public Callback saveCustomer = new Callback() {
        @Override
        public Object call(Object param) {
            return customerDAO.saveOrUpdate((Customer) param);
        }
    };
    
    public Callback listVehicleType = new Callback() {
        @Override
        public Object call(Object param) {
            return vehicleTypeDAO.list();
        }
    };
    
    private CustomerDAO customerDAO = DAOUtils.getInstance().getCustomerDAO();
    private VehicleTypeDAO vehicleTypeDAO = DAOUtils.getInstance().getVehicleTypeDAO();
}
