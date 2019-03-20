/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.ui.controller;

import com.dockingsoftware.autorepairsystem.persistence.PaymentDAO;
import com.dockingsoftware.autorepairsystem.persistence.ProjectDetailsDAO;
import com.dockingsoftware.autorepairsystem.util.DAOUtils;
import java.util.Map;
import javafx.util.Callback;

public class ProjectSalesReportTabContentController {

    public Callback listPayment = new Callback() {
        @Override
        public Object call(Object param) {
            return paymentDAO.list();
        }
    };

    public Callback listProjectDetails = new Callback() {
        @Override
        public Object call(Object param) {
            return projectDetailsDAO.list((Map<String, Object>) param);
        }
    };
    
    private ProjectDetailsDAO projectDetailsDAO = DAOUtils.getInstance().getProjectDetailsDAO();
    private PaymentDAO paymentDAO = DAOUtils.getInstance().getPaymentDAO();
}
