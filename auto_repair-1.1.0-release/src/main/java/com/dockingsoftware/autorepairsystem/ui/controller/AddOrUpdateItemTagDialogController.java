/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.ui.controller;

import com.dockingsoftware.autorepairsystem.persistence.ItemTagDAO;
import com.dockingsoftware.autorepairsystem.persistence.entity.ItemTag;
import com.dockingsoftware.autorepairsystem.util.DAOUtils;
import javafx.util.Callback;

public class AddOrUpdateItemTagDialogController {

    public Callback saveOrUpdateItemTag = new Callback() {
        @Override
        public Object call(Object param) {
            return itemTagDAO.saveOrUpdate((ItemTag) param);
        }
    };
    
    private ItemTagDAO itemTagDAO = DAOUtils.getInstance().getItemTagDAO();
}
