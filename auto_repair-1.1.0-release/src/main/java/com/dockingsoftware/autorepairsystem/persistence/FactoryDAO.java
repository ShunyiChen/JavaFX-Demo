/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.persistence;

import com.dockingsoftware.autorepairsystem.persistence.entity.Factory;
import java.util.List;
import org.hibernate.criterion.Criterion;

public interface FactoryDAO {

    Factory getById(String id);
    
    List<Factory> list(Criterion criterion);
    
    Factory saveOrUpdate(Factory factory);
    
    void delete(Factory factory);
}
