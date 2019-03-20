/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.ui.controller;

import com.dockingsoftware.autorepairsystem.persistence.UserDAO;
import com.dockingsoftware.autorepairsystem.persistence.entity.User;
import com.dockingsoftware.autorepairsystem.util.DAOUtils;
import javafx.util.Callback;

public class ChangePasswordDialogController {

    public Callback getUserById = new Callback() {
        @Override
        public Object call(Object param) {
            return userDAO.getById((String) param);
        }
    };
    
    public Callback saveOrUpdatePassword = new Callback() {
        @Override
        public Object call(Object param) {
            return userDAO.saveOrUpdate((User) param);
        }
    };
    
    private UserDAO userDAO = DAOUtils.getInstance().getUserDAO();
}
