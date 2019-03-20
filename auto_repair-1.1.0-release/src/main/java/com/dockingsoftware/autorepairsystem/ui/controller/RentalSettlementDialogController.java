/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.ui.controller;

import com.dockingsoftware.autorepairsystem.persistence.PaymentDAO;
import com.dockingsoftware.autorepairsystem.persistence.RentalDAO;
import com.dockingsoftware.autorepairsystem.persistence.entity.Rental;
import com.dockingsoftware.autorepairsystem.util.DAOUtils;
import javafx.util.Callback;

public class RentalSettlementDialogController {

    public Callback listPayment = new Callback() {
        @Override
        public Object call(Object param) {
            return paymentDAO.list();
        }
    };
    
    public Callback saveOrUpdateRental = new Callback() {
        @Override
        public Object call(Object param) {
            return rentalDAO.saveOrUpdate((Rental) param);
        }
    };
    
    private RentalDAO rentalDAO = DAOUtils.getInstance().getRentalDAO();
    private PaymentDAO paymentDAO = DAOUtils.getInstance().getPaymentDAO();
}
