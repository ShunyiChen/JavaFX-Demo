/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.config;

import com.dockingsoftware.autorepairsystem.persistence.BusinessTypeDAO;
import com.dockingsoftware.autorepairsystem.persistence.ItemTagDAO;
import com.dockingsoftware.autorepairsystem.persistence.ParameterDAO;
import com.dockingsoftware.autorepairsystem.persistence.PaymentDAO;
import com.dockingsoftware.autorepairsystem.persistence.ProjectCategoryDAO;
import com.dockingsoftware.autorepairsystem.util.DAOUtils;
import com.dockingsoftware.autorepairsystem.persistence.UserDAO;
import com.dockingsoftware.autorepairsystem.persistence.VehicleTypeDAO;
import com.dockingsoftware.autorepairsystem.persistence.entity.BusinessType;
import com.dockingsoftware.autorepairsystem.persistence.entity.ItemTag;
import com.dockingsoftware.autorepairsystem.persistence.entity.Parameter;
import com.dockingsoftware.autorepairsystem.persistence.entity.Payment;
import com.dockingsoftware.autorepairsystem.persistence.entity.ProjectCategory;
import com.dockingsoftware.autorepairsystem.persistence.entity.User;
import com.dockingsoftware.autorepairsystem.persistence.entity.VehicleType;
import com.dockingsoftware.autorepairsystem.util.MD5Utils;
import java.util.List;

public class SystemInitializer {

    public void init() {
        // Save default admin
        UserDAO userDAO = DAOUtils.getInstance().getUserDAO();
        List<User> lstUser = userDAO.list();
        if (lstUser.isEmpty()) {
            User[] users = {
                User.create("admin", MD5Utils.md5("admin"), "管理员"),
            };
            for (User u : users) {
                userDAO.saveOrUpdate(u);
            }   
        }
        
        // 创建业务类型
        BusinessTypeDAO businessTypeDAO = DAOUtils.getInstance().getBusinessTypeDAO();
        List<BusinessType> lstBusinessTypes = businessTypeDAO.list();
        if (lstBusinessTypes.isEmpty()) {
            BusinessType[] businessTypes = {
                BusinessType.create("保养"),
                BusinessType.create("维修"),
                BusinessType.create("钣喷")
            };
            for (BusinessType bt : businessTypes) {
                businessTypeDAO.saveOrUpdate(bt);
            }
        }
        // 创建默认的支付方式
        PaymentDAO paymentDAO = DAOUtils.getInstance().getPaymentDAO();
        List<Payment> lstPayment = paymentDAO.list();
        if (lstPayment.isEmpty()) {
            Payment[] payments = {
                Payment.create("现金"),
                Payment.create("支付宝"),
                Payment.create("微信"),
                Payment.create("POS刷卡"),
                Payment.create("其它")
            };
            for (Payment p : payments) {
                paymentDAO.saveOrUpdate(p);
            }
        }
        // 创建默认的项目分类
        ProjectCategoryDAO projectCategoryDAO = DAOUtils.getInstance().getProjectCategoryDAO();
        List<ProjectCategory> lstProjectCategory = projectCategoryDAO.list();
        if (lstProjectCategory.isEmpty()) {
            ProjectCategory[] projectCategory = {
                ProjectCategory.create("钣金"),
                ProjectCategory.create("喷漆"),
                ProjectCategory.create("更换空滤"),
                ProjectCategory.create("救援"),
                ProjectCategory.create("保养"),
                ProjectCategory.create("销售")
            };
            for (ProjectCategory p : projectCategory) {
                projectCategoryDAO.saveOrUpdate(p);
            }
        }
        // 创建i默认的商品标签
        ItemTagDAO itemTagDAO = DAOUtils.getInstance().getItemTagDAO();
        List<ItemTag> lstItemTag = itemTagDAO.list(null);
        if (lstItemTag.isEmpty()) {
            itemTagDAO.saveOrUpdate(ItemTag.createRoot());
        }
        // 创建默认的机动车种类
        VehicleTypeDAO vehicleTypeDAO = DAOUtils.getInstance().getVehicleTypeDAO();
        List<VehicleType> listVehicleType = vehicleTypeDAO.list();
        if (listVehicleType.isEmpty()) {
            VehicleType[] types  = {
                VehicleType.create("客车")
            };
            for (VehicleType vt : types) {
                vehicleTypeDAO.saveOrUpdate(vt);
            }
        }
        // 创建默认客户提醒参数
        ParameterDAO parameterDAO = DAOUtils.getInstance().getParameterDAO();
        List<Parameter> lstParameters = (List<Parameter>) parameterDAO.list(null);
        if (lstParameters.isEmpty()) {
            Parameter[] params  = {
                Parameter.create("COMMERCIALINSURANCERATE", "0.25"),
                Parameter.create("COMPULSORYINSURANCERATE", "0.04"),
                Parameter.create("BYTS", "30"),
                Parameter.create("BXTS", "90"),
                Parameter.create("NSTS", "30"),
                Parameter.create("HFTS", "30"),
                Parameter.create("RETAIN_FILES", "false"),
                Parameter.create("SAVING_BEFORE_EXITING", "true"),
                Parameter.create("SAVE_PATH", "")
            };
            for (Parameter p : params) {
                parameterDAO.saveOrUpdate(p);
            }
        }
        
    }
}
