/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.persistence;

import com.dockingsoftware.autorepairsystem.persistence.entity.Subject;
import com.dockingsoftware.autorepairsystem.persistence.entity.Subject;
import java.util.List;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

public class SubjectDAOImpl implements SubjectDAO {

    private SessionFactory sessionFactory;

    public SubjectDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    @Override
    public Subject getById(String id) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        List<Subject> results = (List<Subject>) sessionFactory.getCurrentSession().createCriteria(Subject.class)
                .add(Restrictions.eq("id", id))
                .list();
        Subject f;
        if (results.size() > 0) {
            f = results.get(0);
        } else {
            f = new Subject();
        }
        tx.commit();
        return f;
    }

    @Override
    public List<Subject> list(Criterion criterion) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        List<Subject> results;
        if (criterion != null) {
            results = (List<Subject>) sessionFactory.getCurrentSession().createCriteria(Subject.class)
                .add(criterion)
                .list();
        } else {
            results = (List<Subject>) sessionFactory.getCurrentSession().createCriteria(Subject.class)
                .list();
        }
        
        tx.commit();
        return results;
    }

    @Override
    public Subject saveOrUpdate(Subject subject) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        sessionFactory.getCurrentSession().saveOrUpdate(subject);
        tx.commit();
        return subject;
    }

    @Override
    public void delete(Subject subject) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        sessionFactory.getCurrentSession().delete(subject);
        tx.commit();
    }

}
