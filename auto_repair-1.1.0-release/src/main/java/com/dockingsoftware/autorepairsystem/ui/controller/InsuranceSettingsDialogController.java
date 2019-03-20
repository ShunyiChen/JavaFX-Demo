/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.ui.controller;

import com.dockingsoftware.autorepairsystem.persistence.InsuranceDAO;
import com.dockingsoftware.autorepairsystem.persistence.entity.Insurance;
import com.dockingsoftware.autorepairsystem.util.DAOUtils;
import javafx.util.Callback;

public class InsuranceSettingsDialogController {


    public Callback getInsuranceByID = new Callback() {
        @Override
        public Object call(Object param) {
            return insuranceDAO.getById((String) param);
        }
    };
    
    public Callback saveOrUpdateInsurance = new Callback() {
        @Override
        public Object call(Object param) {
            return insuranceDAO.saveOrUpdate((Insurance) param);
        }
    };
    
    private InsuranceDAO insuranceDAO = DAOUtils.getInstance().getInsuranceDAO(); 
}
