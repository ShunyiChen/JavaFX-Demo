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

public class AddOrUpdateTenantDialogController {

    public Callback saveOrUpdateTentant = new Callback() {
        @Override
        public Object call(Object param) {
            return tenantDAO.saveOrUpdate((Tenant) param);
        }
    };
    
    private TenantDAO tenantDAO = DAOUtils.getInstance().getTenantDAO();
}
