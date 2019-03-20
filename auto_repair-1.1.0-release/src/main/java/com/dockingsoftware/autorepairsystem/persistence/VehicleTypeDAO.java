/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.persistence;

import com.dockingsoftware.autorepairsystem.persistence.entity.VehicleType;
import java.util.List;

public interface VehicleTypeDAO {

    List<VehicleType> list();
    
    VehicleType saveOrUpdate(VehicleType vehicleType);
    
    void delete(VehicleType vehicleType);
}
