/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.persistence;

import com.dockingsoftware.autorepairsystem.persistence.entity.Rental;
import java.util.List;
import org.hibernate.criterion.Criterion;

public interface RentalDAO {

    Rental getById(String id);
    
    List<Rental> list(Criterion criterion);
    
    Rental saveOrUpdate(Rental rental);
    
    void delete(Rental rental);
}
