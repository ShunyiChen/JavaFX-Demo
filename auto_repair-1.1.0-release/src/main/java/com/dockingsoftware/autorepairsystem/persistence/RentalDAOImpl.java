/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.persistence;

import com.dockingsoftware.autorepairsystem.persistence.entity.Rental;
import java.util.List;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class RentalDAOImpl implements RentalDAO {

    private SessionFactory sessionFactory;

    public RentalDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    @Override
    public Rental getById(String id) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        List<Rental> results = (List<Rental>) sessionFactory.getCurrentSession().createCriteria(Rental.class)
                .add(Restrictions.eq("id", id))
                .list();
        Rental rental;
        if (results.size() > 0) {
            rental = results.get(0);
            rental.getTenantCars().size();
        } else {
            rental = new Rental();
        }
        tx.commit();
        return rental;
    }

    @Override
    public List<Rental> list(Criterion criterion) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        List<Rental> results;
        if (criterion != null) {
            results = (List<Rental>) sessionFactory.getCurrentSession().createCriteria(Rental.class)
                .add(criterion)
                .addOrder(Order.desc("SN"))
                .list();
        } else {
            results = (List<Rental>) sessionFactory.getCurrentSession().createCriteria(Rental.class)
                .addOrder(Order.desc("SN"))
                .list();
        }
        
        for (Rental r : results) {
            r.getTenantCars().size();
        }
        
        tx.commit();
        return results; 
    }

    @Override
    public Rental saveOrUpdate(Rental rental) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        sessionFactory.getCurrentSession().saveOrUpdate(rental);
        tx.commit();
        return rental;
    }

    @Override
    public void delete(Rental rental) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        sessionFactory.getCurrentSession().delete(rental);
        tx.commit();
    }

}
