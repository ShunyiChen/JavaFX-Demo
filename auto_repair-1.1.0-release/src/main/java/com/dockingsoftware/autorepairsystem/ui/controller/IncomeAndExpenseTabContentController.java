/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.ui.controller;

import com.dockingsoftware.autorepairsystem.persistence.IAEDAO;
import com.dockingsoftware.autorepairsystem.persistence.entity.IAE;
import com.dockingsoftware.autorepairsystem.util.DAOUtils;
import javafx.util.Callback;
import org.hibernate.criterion.Criterion;

public class IncomeAndExpenseTabContentController {

    public Callback getIAEById = new Callback() {
        @Override
        public Object call(Object param) {
            return IAEDAO.getById((String) param);
        }
    };
    
    public Callback listIAE = new Callback() {
        @Override
        public Object call(Object param) {
            return IAEDAO.list((Criterion) param);
        }
    };
    
    public Callback deleteIAE = new Callback() {
        @Override
        public Object call(Object param) {
            IAEDAO.delete((IAE) param);
            return "";
        }
    };

    private IAEDAO IAEDAO = DAOUtils.getInstance().getIAEDAO();
}
