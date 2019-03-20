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

public class AddOrUpdateSubjectDialogController {

    public Callback saveOrUpdateSubject = new Callback() {
        @Override
        public Object call(Object param) {
            return subjectDAO.saveOrUpdate((Subject) param);
        }
    };
    
    private SubjectDAO subjectDAO = DAOUtils.getInstance().getSubjectDAO();
}
