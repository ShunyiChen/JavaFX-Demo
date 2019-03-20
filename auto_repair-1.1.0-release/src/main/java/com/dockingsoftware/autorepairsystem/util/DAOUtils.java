/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.util;

import com.dockingsoftware.autorepairsystem.config.Hibernate5Config;
import com.dockingsoftware.autorepairsystem.persistence.BusinessTypeDAO;
import com.dockingsoftware.autorepairsystem.persistence.CustomerDAO;
import com.dockingsoftware.autorepairsystem.persistence.FactoryDAO;
import com.dockingsoftware.autorepairsystem.persistence.IAEDAO;
import com.dockingsoftware.autorepairsystem.persistence.InsuranceDAO;
import com.dockingsoftware.autorepairsystem.persistence.ItemDAO;
import com.dockingsoftware.autorepairsystem.persistence.ItemDetailsDAO;
import com.dockingsoftware.autorepairsystem.persistence.ItemTagDAO;
import com.dockingsoftware.autorepairsystem.persistence.PaymentDAO;
import com.dockingsoftware.autorepairsystem.persistence.ProjectCategoryDAO;
import com.dockingsoftware.autorepairsystem.persistence.ProjectDAO;
import com.dockingsoftware.autorepairsystem.persistence.UserDAO;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import com.dockingsoftware.autorepairsystem.persistence.ParameterDAO;
import com.dockingsoftware.autorepairsystem.persistence.ProjectDetailsDAO;
import com.dockingsoftware.autorepairsystem.persistence.RentalDAO;
import com.dockingsoftware.autorepairsystem.persistence.TenantDAO;
import com.dockingsoftware.autorepairsystem.persistence.VehicleTypeDAO;
import com.dockingsoftware.autorepairsystem.persistence.SettlementDAO;
import com.dockingsoftware.autorepairsystem.persistence.SettlementDetailsDAO;
import com.dockingsoftware.autorepairsystem.persistence.SubjectDAO;

public class DAOUtils {
    
    private DAOUtils() {
        // Register Hibernate configuration
        appContext = new AnnotationConfigApplicationContext();
        appContext.register(new Class[] {Hibernate5Config.class});
        appContext.refresh();
    } 
    
    public static DAOUtils getInstance() {  
         if (single == null) {    
             single = new DAOUtils();  
         }    
        return single;  
    }
    
    public CustomerDAO getCustomerDAO() {
        return (CustomerDAO) appContext.getBean("customerDAO");
    }
    
    public UserDAO getUserDAO() {
        return (UserDAO) appContext.getBean("userDAO");
    }
 
    public SettlementDAO getSettlementDAO() {
        return (SettlementDAO) appContext.getBean("settlementDAO");
    }
    
    public SettlementDetailsDAO getSettlementDetailsDAO() {
        return (SettlementDetailsDAO) appContext.getBean("settlementDetailsDAO");
    }
    
    public BusinessTypeDAO getBusinessTypeDAO() {
        return (BusinessTypeDAO) appContext.getBean("businessTypeDAO");
    }
    
    public PaymentDAO getPaymentDAO() {
        return (PaymentDAO) appContext.getBean("paymentDAO");
    }
    
    public ProjectDAO getProjectDAO() {
        return (ProjectDAO) appContext.getBean("projectDAO");
    }
    
    public ProjectCategoryDAO getProjectCategoryDAO() {
        return (ProjectCategoryDAO) appContext.getBean("projectCategoryDAO");
    }
    
    public ItemDAO getItemDAO() {
        return (ItemDAO) appContext.getBean("itemDAO");
    }
    
    public ItemDetailsDAO getItemDetailsDAO() {
        return (ItemDetailsDAO) appContext.getBean("itemDetailsDAO");
    }
    
    public ItemTagDAO getItemTagDAO() {
        return (ItemTagDAO) appContext.getBean("itemTagDAO");
    }
    
    public InsuranceDAO getInsuranceDAO() {
        return (InsuranceDAO) appContext.getBean("insuranceDAO");
    }
    
    public VehicleTypeDAO getVehicleTypeDAO() {
        return (VehicleTypeDAO) appContext.getBean("vehicleTypeDAO");
    }
    
    public TenantDAO getTenantDAO() {
        return (TenantDAO) appContext.getBean("tenantDAO");
    }
   
    public ProjectDetailsDAO getProjectDetailsDAO() {
        return (ProjectDetailsDAO) appContext.getBean("projectDetailsDAO");
    }
    
    public ParameterDAO getParameterDAO() {
        return (ParameterDAO) appContext.getBean("parameterDAO");
    }
    
    public FactoryDAO getFactoryDAO() {
        return (FactoryDAO) appContext.getBean("factoryDAO");
    }
    
    public RentalDAO getRentalDAO() {
        return (RentalDAO) appContext.getBean("rentalDAO");
    }
    
    public IAEDAO getIAEDAO() {
        return (IAEDAO) appContext.getBean("IAEDAO");
    }
    
    public SubjectDAO getSubjectDAO() {
        return (SubjectDAO) appContext.getBean("subjectDAO");
    }
    
    private AnnotationConfigApplicationContext appContext;
    private static DAOUtils single = null;  
}
