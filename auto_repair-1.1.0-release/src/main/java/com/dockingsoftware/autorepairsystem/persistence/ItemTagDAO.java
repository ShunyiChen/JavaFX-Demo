/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.persistence;

import com.dockingsoftware.autorepairsystem.persistence.entity.ItemTag;
import java.util.List;
import org.hibernate.criterion.Criterion;

public interface ItemTagDAO {

    ItemTag getById(String id);
    
    ItemTag getByPID(String pid);
    
    List<ItemTag> list(Criterion criterion);
    
    ItemTag saveOrUpdate(ItemTag tag);
    
    void delete(ItemTag tag);
}
