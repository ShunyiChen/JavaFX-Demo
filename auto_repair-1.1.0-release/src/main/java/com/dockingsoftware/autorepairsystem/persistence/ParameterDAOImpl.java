/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.persistence;

import com.dockingsoftware.autorepairsystem.persistence.entity.Parameter;
import java.util.List;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

public class ParameterDAOImpl implements ParameterDAO {

    private SessionFactory sessionFactory;

    public ParameterDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    @Override
    public List<Parameter> list(Criterion criterion) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        List<Parameter> results;
        if (criterion != null) {
            results = (List<Parameter>) sessionFactory.getCurrentSession().createCriteria(Parameter.class)
                .add(criterion)
                .list();
        } else {
            results = (List<Parameter>) sessionFactory.getCurrentSession().createCriteria(Parameter.class)
                .list();
        }
        
        tx.commit();
        return results;
    }

    @Override
    public Parameter saveOrUpdate(Parameter param) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        sessionFactory.getCurrentSession().saveOrUpdate(param);
        tx.commit();
        return param;
    }

}
