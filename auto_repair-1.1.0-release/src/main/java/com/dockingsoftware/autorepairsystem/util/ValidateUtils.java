/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dockingsoftware.autorepairsystem.util;

import com.dockingsoftware.autorepairsystem.persistence.entity.Parameter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.springframework.util.StringUtils;

public class ValidateUtils {

    public static boolean isCarnumberNO(String carnumber) {
        /*
         车牌号格式：汉字 + A-Z + 5位A-Z或0-9
        （只包括了普通车牌号，教练车和部分部队车等车牌号不包括在内）
         */
        String carnumRegex = "[\u4e00-\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{5}";
        if (StringUtils.isEmpty(carnumber)) {
            return false;
        } else {
            return carnumber.matches(carnumRegex);
        }
    }
    
    public static boolean isSerialNumberExpired() {
        try {
            List<Parameter> lstAllParameters = DAOUtils.getInstance().getParameterDAO().list(null);
            Map<String, String> keyValues = new HashMap<String, String>();
            for (Parameter p : lstAllParameters) {
                keyValues.put(p.getId(), p.getValue());
            }
            String SN = keyValues.get("LICENSE");
            if (StringUtils.isEmpty(SN)) {
                return true;
            }
            String dateStr = Encryptor.getInstance().decrypt(SN);
            Date expirationDate = F.parse(dateStr);
            return expirationDate.getTime() <= new Date().getTime();
        } catch (Exception ex) {
            org.apache.logging.log4j.Logger logger = LogManager.getLogger(ValidateUtils.class.getName());
            logger.error(ex);
            return true;
        }
    }
    
    public static boolean checkInputSerialNumber(String SN) {
        try {
            String dateStr = Encryptor.getInstance().decrypt(SN);
            Date expirationDate = F.parse(dateStr);
            return (expirationDate != null) && (expirationDate.getTime() > new Date().getTime());
        } catch (Exception ex) {
            org.apache.logging.log4j.Logger logger = LogManager.getLogger(ValidateUtils.class.getName());
            logger.error(ex);
            return false;
        }
    }
    
    private static SimpleDateFormat F = new SimpleDateFormat("yyyyMMdd");
}
