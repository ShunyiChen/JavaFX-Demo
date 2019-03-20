/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.ui.controller;

import com.dockingsoftware.autorepairsystem.persistence.ProjectCategoryDAO;
import com.dockingsoftware.autorepairsystem.persistence.ProjectDAO;
import com.dockingsoftware.autorepairsystem.persistence.entity.Project;
import com.dockingsoftware.autorepairsystem.util.DAOUtils;
import javafx.util.Callback;

public class AddProjectDialogController {

    public Callback saveOrUpdateProject = new Callback() {
        @Override
        public Object call(Object param) {
            return projectDAO.saveOrUpdate((Project) param);
        }
    };
    
    public Callback list = new Callback() {
        @Override
        public Object call(Object param) {
            return projectCategoryDAO.list();
        }
    };
    
    private ProjectCategoryDAO projectCategoryDAO = DAOUtils.getInstance().getProjectCategoryDAO();
    private ProjectDAO projectDAO = DAOUtils.getInstance().getProjectDAO();
}
