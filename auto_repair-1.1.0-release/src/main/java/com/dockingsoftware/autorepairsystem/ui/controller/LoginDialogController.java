/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.ui.controller;

import com.dockingsoftware.autorepairsystem.persistence.UserDAO;
import com.dockingsoftware.autorepairsystem.util.DAOUtils;
import javafx.util.Callback;

public class LoginDialogController {

    public Callback getUserByNameAndPassword = new Callback() {
        @Override
        public Object call(Object param) {
            String[] params = (String[]) param;
            return userDAO.get(params[0], params[1]);
        }
    };
    
    private UserDAO userDAO = DAOUtils.getInstance().getUserDAO();
}
