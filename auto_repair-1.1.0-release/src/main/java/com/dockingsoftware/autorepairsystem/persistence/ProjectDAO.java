/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.persistence;

import com.dockingsoftware.autorepairsystem.persistence.entity.Project;
import java.util.List;

public interface ProjectDAO {

    Project getById(String id);
    
    List<Project> list(String keyword);
    
    Project saveOrUpdate(Project project);
    
    void delete(Project project);
}
