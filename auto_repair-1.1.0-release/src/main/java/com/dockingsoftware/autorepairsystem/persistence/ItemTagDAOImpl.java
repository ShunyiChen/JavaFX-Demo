/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.persistence;

import com.dockingsoftware.autorepairsystem.persistence.entity.ItemTag;
import java.util.List;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

public class ItemTagDAOImpl implements ItemTagDAO {

    private SessionFactory sessionFactory;

    public ItemTagDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public ItemTag getById(String id) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        List<ItemTag> results = (List<ItemTag>) sessionFactory.getCurrentSession().createCriteria(ItemTag.class)
                .add(Restrictions.eq("id", id))
                .list();
        ItemTag tag;
        if (results.size() > 0) {
            tag = results.get(0);
        } else {
            tag = new ItemTag();
        }
        tx.commit();
        return tag;
    }
    
    @Override
    public ItemTag getByPID(String pid) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        List<ItemTag> results = (List<ItemTag>) sessionFactory.getCurrentSession().createCriteria(ItemTag.class)
                .add(Restrictions.eq("pid", pid))
                .list();
        ItemTag tag;
        if (results.size() > 0) {
            tag = results.get(0);
        } else {
            tag = new ItemTag();
        }
        tx.commit();
        return tag;
    }

    @Override
    public List<ItemTag> list(Criterion criterion) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        List<ItemTag> results;
        if (criterion != null) {
            results = (List<ItemTag>) sessionFactory.getCurrentSession().createCriteria(ItemTag.class)
                .add(criterion)
                .list();
        } else {
            results = (List<ItemTag>) sessionFactory.getCurrentSession().createCriteria(ItemTag.class)
                .list();
        }
        tx.commit();
        return results;
    }

    @Override
    public ItemTag saveOrUpdate(ItemTag tag) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        sessionFactory.getCurrentSession().saveOrUpdate(tag);
        tx.commit();
        return tag;
    }

    @Override
    public void delete(ItemTag tag) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        sessionFactory.getCurrentSession().delete(tag);
        tx.commit();
    }

}
