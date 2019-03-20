/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.persistence;

import com.dockingsoftware.autorepairsystem.persistence.entity.BusinessType;
import java.util.List;

public interface BusinessTypeDAO {

    List<BusinessType> list();
    
    BusinessType saveOrUpdate(BusinessType bt);
    
    void delete(BusinessType bt);
}
