/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.persistence;

import com.dockingsoftware.autorepairsystem.persistence.entity.Subject;
import java.util.List;
import org.hibernate.criterion.Criterion;

public interface SubjectDAO {

    Subject getById(String id);
    
    List<Subject> list(Criterion criterion);
    
    Subject saveOrUpdate(Subject subject);
    
    void delete(Subject subject);
}
