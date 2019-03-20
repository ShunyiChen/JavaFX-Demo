/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.ui.controller;

import com.dockingsoftware.autorepairsystem.persistence.ProjectDAO;
import com.dockingsoftware.autorepairsystem.persistence.entity.Project;
import com.dockingsoftware.autorepairsystem.util.DAOUtils;
import javafx.util.Callback;

public class ChooseProjectDialogController {

    public Callback getProjectById = new Callback() {
        @Override
        public Object call(Object param) {
            return projectDAO.getById((String) param);
        }
    };
    
    public Callback saveOrUpdateProject = new Callback() {
        @Override
        public Object call(Object param) {
            projectDAO.saveOrUpdate((Project) param);
            return "";
        }
    };
    
    public Callback deleteProject = new Callback() {
        @Override
        public Object call(Object param) {
            projectDAO.delete((Project) param);
            return "";
        }
    };
    
    public Callback list = new Callback() {
        @Override
        public Object call(Object param) {
            String keyword = (String) param;
            return projectDAO.list(keyword);
        }
    };
    
    private ProjectDAO projectDAO = DAOUtils.getInstance().getProjectDAO();
}
