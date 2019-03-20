/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.persistence;

import com.dockingsoftware.autorepairsystem.persistence.entity.IAE;
import java.util.List;
import org.hibernate.criterion.Criterion;

public interface IAEDAO {

    IAE getById(String id);
    
    List<IAE> list(Criterion criterion);
    
    IAE saveOrUpdate(IAE iae);
    
    void delete(IAE iae);
}
