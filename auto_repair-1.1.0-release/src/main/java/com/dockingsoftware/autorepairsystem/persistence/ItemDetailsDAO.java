/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.persistence;

import java.util.List;
import java.util.Map;

public interface ItemDetailsDAO {
    
    List<Object[]> list(Map<String, Object> param);
}
