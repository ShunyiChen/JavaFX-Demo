/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.persistence;

import com.dockingsoftware.autorepairsystem.persistence.entity.IAE;
import java.util.List;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

public class IAEDAOImpl implements IAEDAO {

    private SessionFactory sessionFactory;

    public IAEDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    @Override
    public IAE getById(String id) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        List<IAE> results = (List<IAE>) sessionFactory.getCurrentSession().createCriteria(IAE.class)
                .add(Restrictions.eq("id", id))
                .list();
        IAE f;
        if (results.size() > 0) {
            f = results.get(0);
        } else {
            f = new IAE();
        }
        tx.commit();
        return f;
    }

    @Override
    public List<IAE> list(Criterion criterion) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        List<IAE> results;
        if (criterion != null) {
            results = (List<IAE>) sessionFactory.getCurrentSession().createCriteria(IAE.class)
                .add(criterion)
                .list();
        } else {
            results = (List<IAE>) sessionFactory.getCurrentSession().createCriteria(IAE.class)
                .list();
        }
        
        tx.commit();
        return results;
    }

    @Override
    public IAE saveOrUpdate(IAE iae) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        sessionFactory.getCurrentSession().saveOrUpdate(iae);
        tx.commit();
        return iae;
    }

    @Override
    public void delete(IAE iae) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        sessionFactory.getCurrentSession().delete(iae);
        tx.commit();
    }

}
