/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.persistence;

import com.dockingsoftware.autorepairsystem.persistence.entity.ProjectCategory;
import java.util.List;

public interface ProjectCategoryDAO {

    List<ProjectCategory> list();
    
    ProjectCategory saveOrUpdate(ProjectCategory projectCategory);
    
    void delete(ProjectCategory projectCategory);
}
