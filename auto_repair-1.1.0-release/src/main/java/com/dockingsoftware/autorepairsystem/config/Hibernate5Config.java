/**
 * Copyright CodeJava.net To Present
 * All rights reserved.
 */
package com.dockingsoftware.autorepairsystem.config;

import com.dockingsoftware.autorepairsystem.persistence.BusinessTypeDAO;
import com.dockingsoftware.autorepairsystem.persistence.BusinessTypeDAOImpl;
import com.dockingsoftware.autorepairsystem.persistence.CustomerDAO;
import com.dockingsoftware.autorepairsystem.persistence.CustomerDAOImpl;
import com.dockingsoftware.autorepairsystem.persistence.FactoryDAO;
import com.dockingsoftware.autorepairsystem.persistence.FactoryDAOImpl;
import com.dockingsoftware.autorepairsystem.persistence.IAEDAO;
import com.dockingsoftware.autorepairsystem.persistence.IAEDAOImpl;
import com.dockingsoftware.autorepairsystem.persistence.InsuranceDAO;
import com.dockingsoftware.autorepairsystem.persistence.InsuranceDAOImpl;
import com.dockingsoftware.autorepairsystem.persistence.ItemDAO;
import com.dockingsoftware.autorepairsystem.persistence.ItemDAOImpl;
import com.dockingsoftware.autorepairsystem.persistence.ItemDetailsDAO;
import com.dockingsoftware.autorepairsystem.persistence.ItemDetailsDAOImpl;
import com.dockingsoftware.autorepairsystem.persistence.ItemTagDAO;
import com.dockingsoftware.autorepairsystem.persistence.ItemTagDAOImpl;
import com.dockingsoftware.autorepairsystem.persistence.PaymentDAO;
import com.dockingsoftware.autorepairsystem.persistence.PaymentDAOImpl;
import com.dockingsoftware.autorepairsystem.persistence.ProjectCategoryDAO;
import com.dockingsoftware.autorepairsystem.persistence.ProjectCategoryDAOImpl;
import com.dockingsoftware.autorepairsystem.persistence.ProjectDAO;
import com.dockingsoftware.autorepairsystem.persistence.ProjectDAOImpl;
import com.dockingsoftware.autorepairsystem.persistence.UserDAO;
import com.dockingsoftware.autorepairsystem.persistence.UserDAOImpl;
import java.io.File;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import com.dockingsoftware.autorepairsystem.persistence.ParameterDAO;
import com.dockingsoftware.autorepairsystem.persistence.ParameterDAOImpl;
import com.dockingsoftware.autorepairsystem.persistence.ProjectDetailsDAO;
import com.dockingsoftware.autorepairsystem.persistence.ProjectDetailsDAOImpl;
import com.dockingsoftware.autorepairsystem.persistence.RentalDAO;
import com.dockingsoftware.autorepairsystem.persistence.RentalDAOImpl;
import com.dockingsoftware.autorepairsystem.persistence.SettlementDAOImpl;
import com.dockingsoftware.autorepairsystem.persistence.TenantDAO;
import com.dockingsoftware.autorepairsystem.persistence.TenantDAOImpl;
import com.dockingsoftware.autorepairsystem.persistence.VehicleTypeDAO;
import com.dockingsoftware.autorepairsystem.persistence.VehicleTypeDAOImpl;
import com.dockingsoftware.autorepairsystem.persistence.SettlementDAO;
import com.dockingsoftware.autorepairsystem.persistence.SettlementDetailsDAO;
import com.dockingsoftware.autorepairsystem.persistence.SettlementDetailsDAOImpl;
import com.dockingsoftware.autorepairsystem.persistence.SubjectDAO;
import com.dockingsoftware.autorepairsystem.persistence.SubjectDAOImpl;

@Configuration
@ComponentScan("com.dockingsoftware.autorepairsystem")
@EnableTransactionManagement
public class Hibernate5Config {
    
    @Bean(name = "dataSource")
    public DataSource getDataSource() {
        return new BasicDataSource();
    }

    @Autowired
    @Bean(name = "sessionFactory")
    public SessionFactory getSessionFactory(DataSource dataSource) {
        LocalSessionFactoryBuilder sessionBuilder = new LocalSessionFactoryBuilder(dataSource);
        sessionBuilder.setProperties(hibernateProperties());
        // Set entity class
        sessionBuilder.scanPackages("com.dockingsoftware.autorepairsystem.persistence.entity");
        return sessionBuilder.buildSessionFactory();
    }

    @Autowired
    @Bean(name = "transactionManager")
    public HibernateTransactionManager getTransactionManager(
            SessionFactory sessionFactory) {
        return new HibernateTransactionManager(sessionFactory);
    }
    
//    /**
//     * Using MySQL DB   注意如果链接Mysql，数据不支持备份
//     * 
//     * @return 
//     */
//    private Properties hibernateProperties() {
//        return new Properties() {
//            private static final long serialVersionUID = 1L;
//            {
//                setProperty("hibernate.hbm2ddl.auto", "update");// create|create-drop|update|validate 
//                setProperty("hibernate.show_sql", "true");
//                setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
//                setProperty("hibernate.connection.provider_class", "org.hibernate.c3p0.internal.C3P0ConnectionProvider");
//
//                setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
//                setProperty("hibernate.connection.url","jdbc:mysql://localhost:3306/auto_repair?useSSL=false");
//                setProperty("hibernate.connection.username", "root");
//                setProperty("hibernate.connection.password","123456");
//
//                setProperty("hibernate.connection.pool_size", "10");
//                setProperty("hibernate.current_session_context_class","thread");
//                
//                setProperty("hibernate.c3p0.min_size", "5");
//                setProperty("hibernate.c3p0.max_size", "20");
//                setProperty("hibernate.c3p0.timeout", "300");
//                setProperty("hibernate.c3p0.max_statements", "50");
//                setProperty("hibernate.c3p0.idle_test_period", "60");
//            }
//        };
//    }
    
    
    /**
     * Using embedded Derby DB
     * 
     * @return 
     */
    private Properties hibernateProperties() {
        return new Properties() {
            private static final long serialVersionUID = 1L;
            {
                setProperty("hibernate.hbm2ddl.auto", "update");// create|create-drop|update|validate 
                setProperty("hibernate.show_sql", "false");
                setProperty("hibernate.dialect", "org.hibernate.dialect.DerbyDialect");
                setProperty("hibernate.connection.provider_class", "org.hibernate.c3p0.internal.C3P0ConnectionProvider");
//                setProperty("hibernate.connection.driver_class", "org.apache.derby.jdbc.ClientDriver");
//                setProperty("hibernate.connection.url","jdbc:derby://localhost:1527/"+dbName);
//                setProperty("hibernate.connection.username", dbUserName);
//                setProperty("hibernate.connection.password","");

                setProperty("hibernate.connection.driver_class", "org.apache.derby.jdbc.EmbeddedDriver");
                
                if (!new File(dbName).exists()) {
                    setProperty("hibernate.connection.url","jdbc:derby:"+dbName+";create=true");
                } else {
                    setProperty("hibernate.connection.url","jdbc:derby:"+dbName);
                }
                
                setProperty("hibernate.connection.username", dbUserName);
                setProperty("hibernate.connection.password","234561");

                setProperty("hibernate.connection.pool_size", "10");
                setProperty("hibernate.current_session_context_class","thread");
                
                setProperty("hibernate.c3p0.min_size", "5");
                setProperty("hibernate.c3p0.max_size", "20");
                setProperty("hibernate.c3p0.timeout", "300");
                setProperty("hibernate.c3p0.max_statements", "50");
                setProperty("hibernate.c3p0.idle_test_period", "60");
                
//              setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
            }
        };
    }
    
    @Autowired
    @Bean(name = "customerDAO")
    public CustomerDAO getCustomerDAO(SessionFactory sessionFactory) {
        return new CustomerDAOImpl(sessionFactory);
    }
    
    @Autowired
    @Bean(name = "userDAO")
    public UserDAO getUserDAO(SessionFactory sessionFactory) {
        return new UserDAOImpl(sessionFactory);
    }
    
    @Autowired
    @Bean(name = "settlementDAO")
    public SettlementDAO getSettlementDAO(SessionFactory sessionFactory) {
        return new SettlementDAOImpl(sessionFactory);
    }
    
    @Autowired
    @Bean(name = "settlementDetailsDAO")
    public SettlementDetailsDAO getSettlementDetailsDAO(SessionFactory sessionFactory) {
        return new SettlementDetailsDAOImpl(sessionFactory);
    }
    
    @Autowired
    @Bean(name = "businessTypeDAO")
    public BusinessTypeDAO getBusinessTypeDAO(SessionFactory sessionFactory) {
        return new BusinessTypeDAOImpl(sessionFactory);
    }
    
    @Autowired
    @Bean(name = "paymentDAO")
    public PaymentDAO getPaymentDAO(SessionFactory sessionFactory) {
        return new PaymentDAOImpl(sessionFactory);
    }
    
    @Autowired
    @Bean(name = "projectDAO")
    public ProjectDAO getProjectDAO(SessionFactory sessionFactory) {
        return new ProjectDAOImpl(sessionFactory);
    }
    
    @Autowired
    @Bean(name = "projectCategoryDAO")
    public ProjectCategoryDAO getProjectCategoryDAO(SessionFactory sessionFactory) {
        return new ProjectCategoryDAOImpl(sessionFactory);
    }
    
    @Autowired
    @Bean(name = "itemDAO")
    public ItemDAO getItemDAO(SessionFactory sessionFactory) {
        return new ItemDAOImpl(sessionFactory);
    }
    
    @Autowired
    @Bean(name = "itemDetailsDAO")
    public ItemDetailsDAO getItemDetailsDAO(SessionFactory sessionFactory) {
        return new ItemDetailsDAOImpl(sessionFactory);
    }
    
    @Autowired
    @Bean(name = "itemTagDAO")
    public ItemTagDAO getItemCategoryDAO(SessionFactory sessionFactory) {
        return new ItemTagDAOImpl(sessionFactory);
    }
    
    @Autowired
    @Bean(name = "insuranceDAO")
    public InsuranceDAO getInsuranceDAO(SessionFactory sessionFactory) {
        return new InsuranceDAOImpl(sessionFactory);
    }
    
    @Autowired
    @Bean(name = "vehicleTypeDAO")
    public VehicleTypeDAO getVehicleTypeDAO(SessionFactory sessionFactory) {
        return new VehicleTypeDAOImpl(sessionFactory);
    }
    
    @Autowired
    @Bean(name = "tenantDAO")
    public TenantDAO getTenantDAO(SessionFactory sessionFactory) {
        return new TenantDAOImpl(sessionFactory);
    }
    
    @Autowired
    @Bean(name = "projectDetailsDAO")
    public ProjectDetailsDAO getProjectDetailsDAO(SessionFactory sessionFactory) {
        return new ProjectDetailsDAOImpl(sessionFactory);
    }
    
    @Autowired
    @Bean(name = "parameterDAO")
    public ParameterDAO getParameterDAO(SessionFactory sessionFactory) {
        return new ParameterDAOImpl(sessionFactory);
    }
    
    @Autowired
    @Bean(name = "factoryDAO")
    public FactoryDAO getFactoryDAO(SessionFactory sessionFactory) {
        return new FactoryDAOImpl(sessionFactory);
    }
    
    @Autowired
    @Bean(name = "rentalDAO")
    public RentalDAO getRentalDAO(SessionFactory sessionFactory) {
        return new RentalDAOImpl(sessionFactory);
    }
    
    @Autowired
    @Bean(name = "IAEDAO")
    public IAEDAO getIAEDAO(SessionFactory sessionFactory) {
        return new IAEDAOImpl(sessionFactory);
    }
    
    @Autowired
    @Bean(name = "subjectDAO")
    public SubjectDAO getSubjectDAO(SessionFactory sessionFactory) {
        return new SubjectDAOImpl(sessionFactory);
    }
    
    private String dbName = "data";
    private String dbUserName = "auto_repair";
}
