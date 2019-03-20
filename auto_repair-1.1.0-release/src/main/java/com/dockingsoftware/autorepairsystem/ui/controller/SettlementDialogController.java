/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.ui.controller;

import com.dockingsoftware.autorepairsystem.persistence.PaymentDAO;
import com.dockingsoftware.autorepairsystem.persistence.SettlementDetailsDAO;
import com.dockingsoftware.autorepairsystem.persistence.entity.SettlementDetails;
import com.dockingsoftware.autorepairsystem.util.DAOUtils;
import javafx.util.Callback;

public class SettlementDialogController {

    public Callback listPayment = new Callback() {
        @Override
        public Object call(Object param) {
            return paymentDAO.list();
        }
    };
    
    public Callback saveOrUpdateSettlementDetails = new Callback() {
        @Override
        public Object call(Object param) {
            return settlementDetailsDAO.saveOrUpdate((SettlementDetails) param);
        }
    };
    
    private SettlementDetailsDAO settlementDetailsDAO = DAOUtils.getInstance().getSettlementDetailsDAO();
    private PaymentDAO paymentDAO = DAOUtils.getInstance().getPaymentDAO();
}
