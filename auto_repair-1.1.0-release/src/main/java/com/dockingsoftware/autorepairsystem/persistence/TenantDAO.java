/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.persistence;

import com.dockingsoftware.autorepairsystem.persistence.entity.Tenant;
import java.util.List;
import org.hibernate.criterion.Criterion;

public interface TenantDAO {

    Tenant getById(String id);
    
    List<Tenant> list(Criterion criterion);
    
    Tenant saveOrUpdate(Tenant tenant);
    
    void delete(Tenant tenant);
}
