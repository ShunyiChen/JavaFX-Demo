/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.persistence;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class ProjectDetailsDAOImpl implements ProjectDetailsDAO {

    private SessionFactory sessionFactory;

    public ProjectDetailsDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    @Override
    public List<Object[]> list(Map<String, Object> param) {
        StringBuilder where = new StringBuilder(" WHERE T3.SN <> '0' AND");
        if (param != null) {
//            Iterator<String> iter = param.keySet().iterator();
//            while (iter.hasNext()) {
//                String key = iter.next();
//                Object value = param.get(key);
//                System.out.println(key+"="+value);
//            }
            
            if (!param.get("SN").toString().isEmpty()) {
                where.append(" T3.SN LIKE '%").append(param.get("SN").toString()).append("%'").append(" AND");
            }
            if (param.get("billFromDate") != null && param.get("billToDate") != null) {
                String fromDate = dateFormat.format((Date) param.get("billFromDate"));
                String toDate = dateFormat.format((Date) param.get("billToDate"));
                where.append(" T3.BILLING_DATE BETWEEN '").append(fromDate).append("' AND '").append(toDate).append("' AND");
            }
            if (param.get("settleFromDate") != null && param.get("settleToDate") != null) {
                String fromDate = dateFormat.format((Date) param.get("settleFromDate"));
                String toDate = dateFormat.format((Date) param.get("settleToDate"));
                where.append(" T3.SETTLEMENT_DATE BETWEEN '").append(fromDate).append("' AND '").append(toDate).append("' AND");
            }
            if (!param.get("payment").toString().isEmpty()) {
                where.append(" T3.PAYMENT LIKE '%").append(param.get("payment").toString()).append("%'").append(" AND");
            }
            if (!param.get("customerName").toString().isEmpty()) {
                where.append(" T3.CUSTOMER_NAME LIKE '%").append(param.get("customerName").toString()).append("%'").append(" AND");
            }
            if (!param.get("licensePlateNumber").toString().isEmpty()) {
                where.append(" T3.LICENSE_PLATE_NUMBER LIKE '%").append(param.get("licensePlateNumber").toString()).append("%'").append(" AND");
            }
            if (!param.get("projectName").toString().isEmpty()) {
                where.append(" T1.NAME LIKE '%").append(param.get("projectName").toString()).append("%'").append(" AND");
            }
        }
        else {
            
            // 默认查询当月的所有记录
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE,0);
            cal.set(Calendar.SECOND,0);
            cal.set(Calendar.MILLISECOND,0);
            
            String fromDate = dateFormat.format(cal.getTime());
            String toDate = dateFormat.format(new Date());
            where.append(" BILLING_DATE BETWEEN '").append(fromDate).append("' AND '").append(toDate).append("' AND");
        }
        where.delete(where.length() - 3, where.length());
        
        String sql = "SELECT T3.SN,T3.BILLING_DATE,T1.NAME,T1.PRICE,T1.LABOR_HOUR,T1.DISCOUNT,T1.AMOUNT,T1.NOTES,T1.START_TIME,T1.END_TIME,T1.MECHANIC,T1.PROJECT_CATEGORY,T3.CUSTOMER_NAME,T3.LICENSE_PLATE_NUMBER,T3.PAYMENT,T3.SETTLEMENT_DATE,T3.SETTLEMENT_NOTES,T3.SETTLEMENT_STATE FROM AUTO_REPAIR.D_PROJECT_DETAILS AS T1 INNER JOIN AUTO_REPAIR.D_SETTLEMENT_DETAILS_D_PROJECT_DETAILS AS T2 ON T1.ID = T2.PROJECTS_ID INNER JOIN AUTO_REPAIR.D_SETTLEMENT_DETAILS AS T3 ON T3.ID = T2.SETTLEMENTDETAILS_ID" + where.toString();
        
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        List<Object[]> results = sessionFactory.getCurrentSession().createSQLQuery(sql).list();
        tx.commit();
        return results;
    }
    
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
}
