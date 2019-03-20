/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.persistence;

import com.dockingsoftware.autorepairsystem.persistence.entity.VehicleType;
import java.util.List;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class VehicleTypeDAOImpl implements VehicleTypeDAO {

    private SessionFactory sessionFactory;

    public VehicleTypeDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    @Override
    public List<VehicleType> list() {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        List<VehicleType> results = (List<VehicleType>) sessionFactory.getCurrentSession().createCriteria(VehicleType.class)
                .list();
        tx.commit();
        return results;
    }

    @Override
    public VehicleType saveOrUpdate(VehicleType vehicleType) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        sessionFactory.getCurrentSession().saveOrUpdate(vehicleType);
        tx.commit();
        return vehicleType;
    }

    @Override
    public void delete(VehicleType vehicleType) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        sessionFactory.getCurrentSession().delete(vehicleType);
        tx.commit();
    }

   
}
