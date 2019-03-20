/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.persistence;

import com.dockingsoftware.autorepairsystem.persistence.entity.Customer;
import java.util.List;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

public class CustomerDAOImpl implements CustomerDAO {

    private SessionFactory sessionFactory;

    public CustomerDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    @Override
    public Customer getById(String id) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        List<Customer> results = (List<Customer>) sessionFactory.getCurrentSession().createCriteria(Customer.class)
                .add(Restrictions.eq("id", id))
                .list();
        Customer customer;
        if (results.size() > 0) {
            customer = results.get(0);
        } else {
            customer = new Customer();
        }
        tx.commit();
        return customer;
    }
    
    @Override
    public List<Customer> list(Criterion criterion) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        List<Customer> results;
        if (criterion != null) {
            results = (List<Customer>) sessionFactory.getCurrentSession().createCriteria(Customer.class)
                .add(criterion)
                .list();
        } else {
            results = (List<Customer>) sessionFactory.getCurrentSession().createCriteria(Customer.class)
                .list();
        }
        
        tx.commit();
        return results; 
    }

    @Override
    public Customer saveOrUpdate(Customer customer) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        sessionFactory.getCurrentSession().saveOrUpdate(customer);
        tx.commit();
        return customer;
    }

    @Override
    public void delete(Customer customer) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        sessionFactory.getCurrentSession().delete(customer);
        tx.commit();
    }

}
