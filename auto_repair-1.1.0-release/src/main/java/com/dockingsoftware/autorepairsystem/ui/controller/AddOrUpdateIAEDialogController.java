/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.ui.controller;

import com.dockingsoftware.autorepairsystem.persistence.IAEDAO;
import com.dockingsoftware.autorepairsystem.persistence.PaymentDAO;
import com.dockingsoftware.autorepairsystem.persistence.SubjectDAO;
import com.dockingsoftware.autorepairsystem.persistence.entity.IAE;
import com.dockingsoftware.autorepairsystem.persistence.entity.Subject;
import com.dockingsoftware.autorepairsystem.util.DAOUtils;
import javafx.util.Callback;
import org.hibernate.criterion.Criterion;

public class AddOrUpdateIAEDialogController {

    public Callback listSubject = new Callback() {
        @Override
        public Object call(Object param) {
            return subjectDAO.list((Criterion) param);
        }
    };
    
    public Callback saveOrUpdateSubject = new Callback() {
        @Override
        public Object call(Object param) {
            return subjectDAO.saveOrUpdate((Subject) param);
        }
    };
    
    public Callback listPayment = new Callback() {
        @Override
        public Object call(Object param) {
            return paymentDAO.list();
        }
    };
    
    public Callback saveOrUpdateIAE = new Callback() {
        @Override
        public Object call(Object param) {
            return IAEDAO.saveOrUpdate((IAE) param);
        }
    };
    
    private PaymentDAO paymentDAO = DAOUtils.getInstance().getPaymentDAO();
    private IAEDAO IAEDAO = DAOUtils.getInstance().getIAEDAO();
    private SubjectDAO subjectDAO = DAOUtils.getInstance().getSubjectDAO();
}
