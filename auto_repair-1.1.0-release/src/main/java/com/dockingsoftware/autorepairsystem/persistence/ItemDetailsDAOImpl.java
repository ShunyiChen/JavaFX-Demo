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

public class ItemDetailsDAOImpl implements ItemDetailsDAO {

    private SessionFactory sessionFactory;

    public ItemDetailsDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    @Override
    public List<Object[]> list(Map<String, Object> param) {
        StringBuilder where = new StringBuilder(" WHERE T3.SN <> '0' AND");
        if (param != null) {
            
            if (!param.get("SN").toString().isEmpty()) {
                where.append(" SN LIKE '%").append(param.get("SN").toString()).append("%'").append(" AND");
            }
            if (param.get("billFromDate") != null && param.get("billToDate") != null) {
                String fromDate = dateFormat.format((Date) param.get("billFromDate"));
                String toDate = dateFormat.format((Date) param.get("billToDate"));
                where.append(" BILLING_DATE BETWEEN '").append(fromDate).append("' AND '").append(toDate).append("' AND");
            }
            if (param.get("settleFromDate") != null && param.get("settleToDate") != null) {
                String fromDate = dateFormat.format((Date) param.get("settleFromDate"));
                String toDate = dateFormat.format((Date) param.get("settleToDate"));
                where.append(" SETTLEMENT_DATE BETWEEN '").append(fromDate).append("' AND '").append(toDate).append("' AND");
            }
            if (!param.get("payment").toString().isEmpty()) {
                where.append(" PAYMENT LIKE '%").append(param.get("payment").toString()).append("%'").append(" AND");
            }
            if (!param.get("customerName").toString().isEmpty()) {
                where.append(" CUSTOMER_NAME LIKE '%").append(param.get("customerName").toString()).append("%'").append(" AND");
            }
            if (!param.get("licensePlateNumber").toString().isEmpty()) {
                where.append(" LICENSE_PLATE_NUMBER LIKE '%").append(param.get("licensePlateNumber").toString()).append("%'").append(" AND");
            }
            if (!param.get("itemName").toString().isEmpty()) {
                where.append(" NAME LIKE '%").append(param.get("itemName").toString()).append("%'").append(" AND");
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
        
        String sql = "SELECT T3.SN,T3.BILLING_DATE,T1.NAME,T1.SALES_PRICE,T1.QUANTITY,T1.DISCOUNT,T1.AMOUNT,T1.NOTES,T1.COST_PRICE,T1.PROFIT,T3.CUSTOMER_NAME,T3.LICENSE_PLATE_NUMBER,T3.PAYMENT,T3.SETTLEMENT_DATE,T3.SETTLEMENT_NOTES,T3.SETTLEMENT_STATE FROM D_ITEM_DETAILS AS T1 INNER JOIN D_SETTLEMENT_DETAILS_D_ITEM_DETAILS AS T2 ON T1.ID = T2.ITEMS_ID INNER JOIN D_SETTLEMENT_DETAILS AS T3 ON T3.ID = T2.SETTLEMENTDETAILS_ID" + where.toString();
        
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        List<Object[]> results = sessionFactory.getCurrentSession().createSQLQuery(sql).list();
        tx.commit();
        return results;
    }
    
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
}
