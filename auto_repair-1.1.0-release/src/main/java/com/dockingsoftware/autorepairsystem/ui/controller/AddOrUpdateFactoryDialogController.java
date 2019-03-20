/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.ui.controller;

import com.dockingsoftware.autorepairsystem.persistence.FactoryDAO;
import com.dockingsoftware.autorepairsystem.persistence.entity.Factory;
import com.dockingsoftware.autorepairsystem.util.DAOUtils;
import javafx.util.Callback;

public class AddOrUpdateFactoryDialogController {

    public Callback saveOrUpdateFactory = new Callback() {
        @Override
        public Object call(Object param) {
            return factoryDAO.saveOrUpdate((Factory) param);
        }
    };
    
    private FactoryDAO factoryDAO = DAOUtils.getInstance().getFactoryDAO();
}
