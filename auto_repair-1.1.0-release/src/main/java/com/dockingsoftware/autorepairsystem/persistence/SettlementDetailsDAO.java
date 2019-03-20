/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.persistence;

import com.dockingsoftware.autorepairsystem.persistence.entity.SettlementDetails;
import java.util.List;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

public interface SettlementDetailsDAO {

    SettlementDetails getById(String id);
    
    List<SettlementDetails> list(Criterion criterion);
    
    List<SettlementDetails> list(Criterion criterion, Order order);
    
    SettlementDetails saveOrUpdate(SettlementDetails details);
    
    void delete(SettlementDetails details);
}
