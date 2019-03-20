/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.persistence;

import com.dockingsoftware.autorepairsystem.persistence.entity.Factory;
import java.util.List;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

public class FactoryDAOImpl implements FactoryDAO {

    private SessionFactory sessionFactory;

    public FactoryDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    @Override
    public Factory getById(String id) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        List<Factory> results = (List<Factory>) sessionFactory.getCurrentSession().createCriteria(Factory.class)
                .add(Restrictions.eq("id", id))
                .list();
        Factory f;
        if (results.size() > 0) {
            f = results.get(0);
        } else {
            f = new Factory();
        }
        tx.commit();
        return f;
    }

    @Override
    public List<Factory> list(Criterion criterion) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        List<Factory> results;
        if (criterion != null) {
            results = (List<Factory>) sessionFactory.getCurrentSession().createCriteria(Factory.class)
                .add(criterion)
                .list();
        } else {
            results = (List<Factory>) sessionFactory.getCurrentSession().createCriteria(Factory.class)
                .list();
        }
        
        tx.commit();
        return results;
    }

    @Override
    public Factory saveOrUpdate(Factory factory) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        sessionFactory.getCurrentSession().saveOrUpdate(factory);
        tx.commit();
        return factory;
    }

    @Override
    public void delete(Factory factory) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        sessionFactory.getCurrentSession().saveOrUpdate(factory);
        tx.commit();
    }

}
