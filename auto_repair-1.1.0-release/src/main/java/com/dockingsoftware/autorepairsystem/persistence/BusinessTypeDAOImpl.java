/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.persistence;

import com.dockingsoftware.autorepairsystem.persistence.entity.BusinessType;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class BusinessTypeDAOImpl implements BusinessTypeDAO {

    private SessionFactory sessionFactory;

    public BusinessTypeDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    @Override
    public List<BusinessType> list() {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        List<BusinessType> results = (List<BusinessType>) sessionFactory.getCurrentSession().createCriteria(BusinessType.class).list();
        tx.commit();
        return results;
    }

    @Override
    public BusinessType saveOrUpdate(BusinessType bt) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        sessionFactory.getCurrentSession().saveOrUpdate(bt);
        tx.commit();
        return bt;
    }

    @Override
    public void delete(BusinessType bt) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        sessionFactory.getCurrentSession().delete(bt);
        tx.commit();
    }
}
