/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.persistence;

import com.dockingsoftware.autorepairsystem.persistence.entity.Payment;
import java.util.List;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class PaymentDAOImpl implements PaymentDAO {

    private SessionFactory sessionFactory;

    public PaymentDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    @Override
    public List<Payment> list() {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        List<Payment> results = (List<Payment>) sessionFactory.getCurrentSession().createCriteria(Payment.class)
                .addOrder(Order.asc("id"))
                .list();

        tx.commit();
        return results;
    }

    @Override
    public Payment saveOrUpdate(Payment payment) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        sessionFactory.getCurrentSession().saveOrUpdate(payment);
        tx.commit();
        return payment;
    }

    @Override
    public void delete(Payment payment) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        sessionFactory.getCurrentSession().delete(payment);
        tx.commit();
    }
}
