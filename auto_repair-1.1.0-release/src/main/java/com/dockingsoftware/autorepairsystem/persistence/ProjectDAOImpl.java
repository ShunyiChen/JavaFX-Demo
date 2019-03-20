/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.persistence;

import com.dockingsoftware.autorepairsystem.persistence.entity.Project;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class ProjectDAOImpl implements ProjectDAO {

    private SessionFactory sessionFactory;

    public ProjectDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    @Override
    public List<Project> list(String keyword) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        List<Project> results = (List<Project>) sessionFactory.getCurrentSession().createCriteria(Project.class)
                .add(Restrictions.or(
                        Restrictions.ilike("name", keyword, MatchMode.ANYWHERE),
                        Restrictions.ilike("projectCategory", keyword, MatchMode.ANYWHERE)
                ))
                .list();

        tx.commit();
        return results;
    }
    
    @Override
    public Project getById(String id) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        List<Project> results = (List<Project>) sessionFactory.getCurrentSession().createCriteria(Project.class)
                .add(Restrictions.eq("id", id))
                .list();
        
        Project p;
        if (results.size() > 0) {
            p = results.get(0);
        } else {
            p = new Project();
        }
        tx.commit();
        return p;
    }

    @Override
    public Project saveOrUpdate(Project project) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        sessionFactory.getCurrentSession().saveOrUpdate(project);
        tx.commit();
        return project;
    }
    
    @Override
    public void delete(Project project) {
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        sessionFactory.getCurrentSession().delete(project);
        tx.commit();
    }
}
