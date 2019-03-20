/**
 * Copyright CodeJava.net To Present
 * All rights reserved.
 */
package com.dockingsoftware.autorepairsystem.persistence;


import com.dockingsoftware.autorepairsystem.persistence.entity.User;
import java.util.List;

public interface UserDAO {
    
    User getById(String id);

    User get(String userName, String password);
    
    List<User> list();
    
    User saveOrUpdate(User user);
}
