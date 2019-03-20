/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.persistence;

import com.dockingsoftware.autorepairsystem.persistence.entity.Settlement;
import java.util.List;
import org.hibernate.criterion.Criterion;

public interface SettlementDAO {
    
    Settlement getById(String id);
    
    List<Settlement> list(Criterion criterion);
    
    Settlement saveOrUpdate(Settlement settlement);
    
    void delete(Settlement settlement);
}
