/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dockingsoftware.autorepairsystem.ui.controller;

import com.dockingsoftware.autorepairsystem.persistence.ItemDetailsDAO;
import com.dockingsoftware.autorepairsystem.persistence.PaymentDAO;
import com.dockingsoftware.autorepairsystem.persistence.SettlementDetailsDAO;
import com.dockingsoftware.autorepairsystem.persistence.entity.SettlementDetails;
import com.dockingsoftware.autorepairsystem.util.DAOUtils;
import java.util.Map;
import javafx.util.Callback;
import org.hibernate.criterion.Criterion;

public class ItemSalesReportTabContentController {

    public Callback listPayment = new Callback() {
        @Override
        public Object call(Object param) {
            return paymentDAO.list();
        }
    };

    public Callback listItemDetails = new Callback() {
        @Override
        public Object call(Object param) {
            return itemDetailsDAO.list((Map<String, Object>) param);
        }
    };
    
    private ItemDetailsDAO itemDetailsDAO = DAOUtils.getInstance().getItemDetailsDAO();
    private PaymentDAO paymentDAO = DAOUtils.getInstance().getPaymentDAO();
}
