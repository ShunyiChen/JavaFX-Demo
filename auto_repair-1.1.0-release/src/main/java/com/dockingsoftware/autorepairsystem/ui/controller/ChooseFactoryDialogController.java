/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.ui.controller;

import com.dockingsoftware.autorepairsystem.persistence.FactoryDAO;
import com.dockingsoftware.autorepairsystem.util.DAOUtils;
import javafx.util.Callback;
import org.hibernate.criterion.Criterion;

public class ChooseFactoryDialogController {

    public Callback getFactoryById = new Callback() {
        @Override
        public Object call(Object param) {
            return factoryDAO.getById((String) param);
        }
    };
    
    public Callback listFactory = new Callback() {
        @Override
        public Object call(Object param) {
            return factoryDAO.list((Criterion) param);
        }
    };
    
    public Callback deleteFactory = new Callback() {
        @Override
        public Object call(Object param) {
            factoryDAO.list((Criterion) param);
            return "";
        }
    };
    
    private FactoryDAO factoryDAO = DAOUtils.getInstance().getFactoryDAO();
}
