/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.ui.controller;

import com.dockingsoftware.autorepairsystem.persistence.RentalDAO;
import com.dockingsoftware.autorepairsystem.persistence.TenantDAO;
import com.dockingsoftware.autorepairsystem.persistence.entity.Rental;
import com.dockingsoftware.autorepairsystem.util.DAOUtils;
import javafx.util.Callback;
import org.hibernate.criterion.Criterion;

public class RentalManagementTabContentController {

//    public Callback listTenant = new Callback() {
//        @Override
//        public Object call(Object param) {
//            return tenantDAO.list((Criterion) param);
//        }
//    };
//    
//    public Callback getTenantByID = new Callback() {
//        @Override
//        public Object call(Object param) {
//            return tenantDAO.getById((String) param);
//        }
//    };
//    
//    public Callback saveOrUpdateTentant = new Callback() {
//        @Override
//        public Object call(Object param) {
//            return tenantDAO.saveOrUpdate((Rental) param);
//        }
//    };
//    
//    public Callback deleteTentant = new Callback() {
//        @Override
//        public Object call(Object param) {
//            tenantDAO.delete((Rental) param);
//            return "";
//        }
//    };
    
//    public Callback saveOrUpdateRental = new Callback() {
//        @Override
//        public Object call(Object param) {
//            return rentalDAO.saveOrUpdate((Rental) param);
//        }
//    };
    
    public Callback getRentalById = new Callback() {
        @Override
        public Object call(Object param) {
            return rentalDAO.getById((String) param);
        }
    };
    
    public Callback listRental = new Callback() {
        @Override
        public Object call(Object param) {
            return rentalDAO.list((Criterion) param);
        }
    };
    
    public Callback deleteRental = new Callback() {
        @Override
        public Object call(Object param) {
            rentalDAO.delete((Rental) param);
            return "";
        }
    };
    
    public Callback saveOrUpdateRental = new Callback() {
        @Override
        public Object call(Object param) {
            return rentalDAO.saveOrUpdate((Rental) param);
        }
    };
    
    private RentalDAO rentalDAO = DAOUtils.getInstance().getRentalDAO();
}
