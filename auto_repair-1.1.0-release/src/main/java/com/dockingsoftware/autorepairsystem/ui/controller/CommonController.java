/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.ui.controller;

import com.dockingsoftware.autorepairsystem.persistence.ParameterDAO;
import com.dockingsoftware.autorepairsystem.persistence.entity.Parameter;
import com.dockingsoftware.autorepairsystem.util.DAOUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.util.Callback;

public class CommonController {

    public Callback listAllParameters = new Callback() {
        @Override
        public Object call(Object param) {
            List<Parameter> lstAllParameters = parameterDAO.list(null);
            Map<String, String> keyValues = new HashMap<String, String>();
            for (Parameter p : lstAllParameters) {
                keyValues.put(p.getId(), p.getValue());
            }
            return keyValues;
        }
    };
     
    private ParameterDAO parameterDAO = DAOUtils.getInstance().getParameterDAO();
}
