/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.persistence;

import com.dockingsoftware.autorepairsystem.persistence.entity.Item;
import java.util.List;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

public class ItemDAOImpl implements ItemDAO {

    private SessionFactory sessionFactory;

    public ItemDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    @Override
    public Item getById(String id) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        List<Item> results = (List<Item>) sessionFactory.getCurrentSession().createCriteria(Item.class)
                .add(Restrictions.eq("id", id))
                .list();
        Item item;
        if (results.size() > 0) {
            item = results.get(0);
        } else {
            item = new Item();
        }
        tx.commit();
        return item;
    }
    
    @Override
    public List<Item> list(Criterion criterion) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        List<Item> results;
        if (criterion != null) {
            results = (List<Item>) sessionFactory.getCurrentSession().createCriteria(Item.class)
                .add(criterion)
                .list();
        } else {
            results = (List<Item>) sessionFactory.getCurrentSession().createCriteria(Item.class)
                .list();
        }
        tx.commit();
        return results;
    }

    @Override
    public Item saveOrUpdate(Item item) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        sessionFactory.getCurrentSession().saveOrUpdate(item);
        tx.commit();
        return item;
    }

    @Override
    public void delete(Item item) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        sessionFactory.getCurrentSession().delete(item);
        tx.commit();
    }
    
}
