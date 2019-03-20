/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.persistence;

import com.dockingsoftware.autorepairsystem.persistence.entity.Settlement;
import java.util.List;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class SettlementDAOImpl implements SettlementDAO {

    private SessionFactory sessionFactory;

    public SettlementDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Settlement getById(String id) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        List<Settlement> results = (List<Settlement>) sessionFactory.getCurrentSession().createCriteria(Settlement.class)
                .add(Restrictions.eq("id", id))
                .list();
        Settlement settlement;
        if (results.size() > 0) {
            settlement = results.get(0);
            for (int i = 0; i < settlement.getDetails().size(); i++) {
                settlement.getDetails().get(i).getProjects().size();
                settlement.getDetails().get(i).getItems().size();
            }
        } else {
            settlement = new Settlement();
        }
        tx.commit();
        return settlement;
    }

    @Override
    public List<Settlement> list(Criterion criterion) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        List<Settlement> results;
        if (criterion != null) {
            results = (List<Settlement>) sessionFactory.getCurrentSession().createCriteria(Settlement.class)
                .add(criterion)
                .addOrder(Order.desc("SN"))
                .list();
        } else {
            results = (List<Settlement>) sessionFactory.getCurrentSession().createCriteria(Settlement.class)
                .addOrder(Order.desc("SN"))    
                .list();
        }
 
        for (Settlement s : results) {
            s.getDetails().size();
        }
        
        tx.commit();
        return results;
    }

    @Override
    public Settlement saveOrUpdate(Settlement settlement) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        sessionFactory.getCurrentSession().saveOrUpdate(settlement);
        tx.commit();
        return settlement;
    }

    @Override
    public void delete(Settlement settlement) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        sessionFactory.getCurrentSession().delete(settlement);
        tx.commit();
    }
}
