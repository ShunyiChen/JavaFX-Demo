/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dockingsoftware.autorepairsystem.ui.controller;

import com.dockingsoftware.autorepairsystem.persistence.ItemDAO;
import com.dockingsoftware.autorepairsystem.persistence.ItemTagDAO;
import com.dockingsoftware.autorepairsystem.persistence.entity.Item;
import com.dockingsoftware.autorepairsystem.util.DAOUtils;
import javafx.util.Callback;
import org.hibernate.criterion.Criterion;

public class ChooseItemDialogController {

    public Callback listItemByCriterion = new Callback() {
        @Override
        public Object call(Object param) {
            return itemDAO.list((Criterion) param);
        }
    };
    
    public Callback getItemById = new Callback() {
        @Override
        public Object call(Object param) {
            return itemDAO.getById((String) param);
        }
    };
    
    public Callback getRootItemTag = new Callback() {
        @Override
        public Object call(Object param) {
            return itemTagDAO.getByPID("0");
        }
    };

    public Callback deleteItem = new Callback() {
        @Override
        public Object call(Object param) {
            itemDAO.delete((Item) param);
            return "";
        }
    };
    
    private ItemDAO itemDAO = DAOUtils.getInstance().getItemDAO();
    private ItemTagDAO itemTagDAO = DAOUtils.getInstance().getItemTagDAO();
}
