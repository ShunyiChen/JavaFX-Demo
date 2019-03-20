/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.ui.controller;

import com.dockingsoftware.autorepairsystem.persistence.TenantDAO;
import com.dockingsoftware.autorepairsystem.persistence.entity.Tenant;
import com.dockingsoftware.autorepairsystem.util.DAOUtils;
import javafx.util.Callback;
import org.hibernate.criterion.Criterion;

public class ChooseTenantDialogController {

    public Callback getTenantByID = new Callback() {
        @Override
        public Object call(Object param) {
            return tenantDAO.getById((String) param);
        }
    };
    
    public Callback listTenantByCriterion = new Callback() {
        @Override
        public Object call(Object param) {
            return tenantDAO.list((Criterion) param);
        }
    };
    
    public Callback deleteTenant = new Callback() {
        @Override
        public Object call(Object param) {
            tenantDAO.delete((Tenant) param);
            return "";
        }
    };
    
    private TenantDAO tenantDAO = DAOUtils.getInstance().getTenantDAO();
}
