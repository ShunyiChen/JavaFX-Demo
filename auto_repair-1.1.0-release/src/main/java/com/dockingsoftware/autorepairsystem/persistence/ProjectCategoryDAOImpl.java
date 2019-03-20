/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.persistence;

import com.dockingsoftware.autorepairsystem.persistence.entity.ProjectCategory;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class ProjectCategoryDAOImpl implements ProjectCategoryDAO {

    private SessionFactory sessionFactory;

    public ProjectCategoryDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    @Override
    public List<ProjectCategory> list() {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        List<ProjectCategory> results = (List<ProjectCategory>) sessionFactory.getCurrentSession().createCriteria(ProjectCategory.class)
                .list();

        tx.commit();
        return results;
    }

    @Override
    public ProjectCategory saveOrUpdate(ProjectCategory projectCategory) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        sessionFactory.getCurrentSession().saveOrUpdate(projectCategory);
        tx.commit();
        return projectCategory;
    }

    @Override
    public void delete(ProjectCategory projectCategory) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        sessionFactory.getCurrentSession().delete(projectCategory);
        tx.commit();
    }
}
