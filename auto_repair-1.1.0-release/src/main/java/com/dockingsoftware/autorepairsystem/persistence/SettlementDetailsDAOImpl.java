/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.persistence;

import com.dockingsoftware.autorepairsystem.persistence.entity.SettlementDetails;
import java.util.List;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class SettlementDetailsDAOImpl implements SettlementDetailsDAO {

    private SessionFactory sessionFactory;

    public SettlementDetailsDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    @Override
    public SettlementDetails getById(String id) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        List<SettlementDetails> results = (List<SettlementDetails>) sessionFactory.getCurrentSession().createCriteria(SettlementDetails.class)
                .add(Restrictions.eq("id", id))
                .list();
        SettlementDetails details;
        if (results.size() > 0) {
            details = results.get(0);
            details.getProjects().size();
            details.getItems().size();
        } else {
            details = new SettlementDetails();
        }
        tx.commit();
        return details;
    }

    @Override
    public List<SettlementDetails> list(Criterion criterion) {
        return list(criterion, Order.desc("SN"));
    }
    
    @Override
    public List<SettlementDetails> list(Criterion criterion, Order order) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        List<SettlementDetails> results;
        if (criterion != null) {
            results = (List<SettlementDetails>) sessionFactory.getCurrentSession().createCriteria(SettlementDetails.class)
                .add(criterion)
                .addOrder(order)
                .list();
        } else {
            results = (List<SettlementDetails>) sessionFactory.getCurrentSession().createCriteria(SettlementDetails.class)
                .addOrder(order)    
                .list();
        }
        
        for (SettlementDetails details : results) {
            details.getProjects().size();
            details.getItems().size();
        }
        
        tx.commit();
        return results;
    }

    @Override
    public SettlementDetails saveOrUpdate(SettlementDetails details) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        sessionFactory.getCurrentSession().saveOrUpdate(details);
        tx.commit();
        return details;
    }

    @Override
    public void delete(SettlementDetails details) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        sessionFactory.getCurrentSession().delete(details);
        tx.commit();
    }

}
