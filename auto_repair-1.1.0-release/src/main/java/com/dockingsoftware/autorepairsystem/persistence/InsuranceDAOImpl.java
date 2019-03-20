/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.persistence;

import com.dockingsoftware.autorepairsystem.persistence.entity.Insurance;
import java.util.List;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

public class InsuranceDAOImpl implements InsuranceDAO {

    private SessionFactory sessionFactory;

    public InsuranceDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    @Override
    public List<Insurance> list(Criterion criterion) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        List<Insurance> results;
        if (criterion != null) {
            results = (List<Insurance>) sessionFactory.getCurrentSession().createCriteria(Insurance.class)
                .add(criterion)
                .addOrder(org.hibernate.criterion.Order.asc("outDate"))
                .list();
        } else {
            results = (List<Insurance>) sessionFactory.getCurrentSession().createCriteria(Insurance.class)
                .addOrder(org.hibernate.criterion.Order.asc("outDate"))
                .list();
        }
        
        tx.commit();
        return results;
    }

    @Override
    public Insurance saveOrUpdate(Insurance insurance) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        sessionFactory.getCurrentSession().saveOrUpdate(insurance);
        tx.commit();
        return insurance;
    }

    @Override
    public Insurance getById(String id) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        List<Insurance> results = (List<Insurance>) sessionFactory.getCurrentSession().createCriteria(Insurance.class)
                .add(Restrictions.eq("id", id))
                .list();
        Insurance insurance;
        if (results.size() > 0) {
            insurance = results.get(0);
            insurance.getInsuranceItems().size();
        } else {
            insurance = new Insurance();
        }
        tx.commit();
        return insurance;
    }

    @Override
    public void delete(Insurance insurance) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        sessionFactory.getCurrentSession().delete(insurance);
        tx.commit();
    }
}
