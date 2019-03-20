/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.persistence;

import com.dockingsoftware.autorepairsystem.persistence.entity.Payment;
import java.util.List;

public interface PaymentDAO {

    List<Payment> list();
    
    Payment saveOrUpdate(Payment payment);
    
    void delete(Payment payment);
}
