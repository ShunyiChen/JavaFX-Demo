/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.persistence;

import com.dockingsoftware.autorepairsystem.persistence.entity.Tenant;
import java.util.List;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

public class TenantDAOImpl implements TenantDAO {

    private SessionFactory sessionFactory;

    public TenantDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    @Override
    public List<Tenant> list(Criterion criterion) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        List<Tenant> results;
        if (criterion != null) {
            results = (List<Tenant>) sessionFactory.getCurrentSession().createCriteria(Tenant.class)
                .add(criterion)
                .list();
        } else {
            results = (List<Tenant>) sessionFactory.getCurrentSession().createCriteria(Tenant.class)
                .list();
        }
        tx.commit();
        return results;
    }

    @Override
    public Tenant saveOrUpdate(Tenant tenant) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        sessionFactory.getCurrentSession().saveOrUpdate(tenant);
        tx.commit();
        return tenant;
    }

    @Override
    public Tenant getById(String id) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        List<Tenant> results = (List<Tenant>) sessionFactory.getCurrentSession().createCriteria(Tenant.class)
                .add(Restrictions.eq("id", id))
                .list();
        Tenant t;
        if (results.size() > 0) {
            t = results.get(0);
        } else {
            t = new Tenant();
        }
        tx.commit();
        return t;
    }

    @Override
    public void delete(Tenant tenant) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        sessionFactory.getCurrentSession().delete(tenant);
        tx.commit();
    }

}
