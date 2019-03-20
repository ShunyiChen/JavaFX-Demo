/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.ui.controller;

import com.dockingsoftware.autorepairsystem.persistence.ItemDAO;
import com.dockingsoftware.autorepairsystem.persistence.entity.Item;
import com.dockingsoftware.autorepairsystem.util.DAOUtils;
import javafx.util.Callback;

public class AddOrUpdateItemDialogController {

    /**
     * 插入或更新商品
     */
    public Callback saveOrUpdateItem = new Callback() {
        @Override
        public Object call(Object param) {
            return itemDAO.saveOrUpdate((Item) param);
        }
    };
    
    private ItemDAO itemDAO = DAOUtils.getInstance().getItemDAO();
}
