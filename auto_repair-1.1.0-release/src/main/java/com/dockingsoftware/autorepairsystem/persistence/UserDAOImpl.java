/**
 * Copyright CodeJava.net To Present
 * All rights reserved.
 */
package com.dockingsoftware.autorepairsystem.persistence;

import com.dockingsoftware.autorepairsystem.persistence.entity.User;
import com.dockingsoftware.autorepairsystem.util.MD5Utils;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

public class UserDAOImpl implements UserDAO {

    private SessionFactory sessionFactory;

    public UserDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public User get(String userName, String password) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        List<User> results = (List<User>) sessionFactory.getCurrentSession().createCriteria(User.class)
                .add(Restrictions.eq("deleteFlag", 0))
                .add(Restrictions.eq("userName", userName))
                .add(Restrictions.eq("password", MD5Utils.md5(password)))
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                .list();
        tx.commit();
        if (results.size() > 0) {
            return results.get(0);
        }
        return new User();
    }
    
    @Override
    @Transactional
    public List<User> list() {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        List<User> results = (List<User>) sessionFactory.getCurrentSession().createCriteria(User.class)
                .add(Restrictions.eq("deleteFlag", 0))
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                .list();
        tx.commit();
        return results;
    }

    @Override
    @Transactional
    public User saveOrUpdate(User user) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        sessionFactory.getCurrentSession().saveOrUpdate(user);
        tx.commit();
        return user;
    }

    @Override
    public User getById(String id) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        List<User> results = (List<User>) sessionFactory.getCurrentSession().createCriteria(User.class)
                .add(Restrictions.eq("deleteFlag", 0))
                .add(Restrictions.eq("id", id))
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                .list();
        tx.commit();
        if (results.size() > 0) {
            return results.get(0);
        }
        return new User();
    }


}
