/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.persistence;

import com.dockingsoftware.autorepairsystem.persistence.entity.Item;
import java.util.List;
import org.hibernate.criterion.Criterion;

public interface ItemDAO {

    Item getById(String id);
    
    List<Item> list(Criterion criterion);
    
    Item saveOrUpdate(Item item);
    
    void delete(Item item);
}
