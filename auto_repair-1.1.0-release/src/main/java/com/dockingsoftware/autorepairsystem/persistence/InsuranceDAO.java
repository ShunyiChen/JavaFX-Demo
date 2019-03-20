/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.persistence;

import com.dockingsoftware.autorepairsystem.persistence.entity.Insurance;
import java.util.List;
import org.hibernate.criterion.Criterion;

public interface InsuranceDAO {

    List<Insurance> list(Criterion criterion);
    
    Insurance getById(String id);
    
    Insurance saveOrUpdate(Insurance insurance);
    
    void delete(Insurance insurance);
}
