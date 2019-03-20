/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.common;

import com.dockingsoftware.autorepairsystem.Constants;
import com.dockingsoftware.autorepairsystem.persistence.SettlementDAO;
import com.dockingsoftware.autorepairsystem.persistence.entity.Rental;
import com.dockingsoftware.autorepairsystem.persistence.entity.Settlement;
import com.dockingsoftware.autorepairsystem.util.DAOUtils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

public class Generator {
    
    /**
     * 
     * @param functionId
     * @return 
     */
    public static String generateSN(String functionId) {
        String number = sdf.format(new Date());
        if (functionId.equals(Constants.WXKD)) {
            String SN = Constants.WXKD + (Integer.parseInt(number) * 100L + 1L);
            
            Criterion c = Restrictions.like("SN", Constants.WXKD + number, MatchMode.ANYWHERE);
            List<Settlement> lstSettlement = DAOUtils.getInstance().getSettlementDAO().list(c);
            if (lstSettlement.size() > 0) {
                SN = Constants.WXKD + (Long.parseLong(lstSettlement.get(0).getSN().substring(4)) + 1);
            }
            return SN;
        } else if (functionId.equals(Constants.KFCZ)) {
            String SN = Constants.KFCZ + (Integer.parseInt(number) * 100L + 1L);
            
            Criterion c = Restrictions.like("SN", Constants.KFCZ + number, MatchMode.ANYWHERE);
            List<Rental> lstRental = DAOUtils.getInstance().getRentalDAO().list(c);
            if (lstRental.size() > 0) {
                SN = Constants.KFCZ + (Long.parseLong(lstRental.get(0).getSN().substring(4)) + 1);
            }
            return SN;
        } else {
            
            return "";
        }
    }
    
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
}
