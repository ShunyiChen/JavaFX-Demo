/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.ui.controller;

import com.dockingsoftware.autorepairsystem.persistence.SubjectDAO;
import com.dockingsoftware.autorepairsystem.persistence.entity.Subject;
import com.dockingsoftware.autorepairsystem.util.DAOUtils;
import javafx.util.Callback;
import org.hibernate.criterion.Criterion;

public class ChooseSubjectDialogController {

    public Callback listSubject = new Callback() {
        @Override
        public Object call(Object param) {
            return subjectDAO.list((Criterion) param);
        }
    };
    
    public Callback getSubjectById = new Callback() {
        @Override
        public Object call(Object param) {
            return subjectDAO.getById((String) param);
        }
    };
    
    public Callback saveOrUpdateSubject = new Callback() {
        @Override
        public Object call(Object param) {
            return subjectDAO.saveOrUpdate((Subject) param);
        }
    };
    
    public Callback deleteSubject = new Callback() {
        @Override
        public Object call(Object param) {
            subjectDAO.delete((Subject) param);
            return "";
        }
    };
    
    private SubjectDAO subjectDAO = DAOUtils.getInstance().getSubjectDAO();
}
