/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.component.model;

import java.math.BigDecimal;

public class FactoryModel {

    /** 序号 */
    private String No;
    
    /** ID主键 */
    private String id;

    /** 厂名 */
    private String name;
    
    /** 联系人 */
    private String contacts;
    
    /** 联系人手机 */
    private String phoneNo;
    
    /** 备注 */
    private String notes;

     /** 历史消费 */
    private BigDecimal historicalAmount;
    
    /** 历史单数 */
    private int historicalNumber;
    
    /** 历史欠款 */
    private BigDecimal historicalOwing;
    
    public String getNo() {
        return No;
    }

    public void setNo(String No) {
        this.No = No;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public BigDecimal getHistoricalAmount() {
        return historicalAmount;
    }

    public void setHistoricalAmount(BigDecimal historicalAmount) {
        this.historicalAmount = historicalAmount;
    }

    public int getHistoricalNumber() {
        return historicalNumber;
    }

    public void setHistoricalNumber(int historicalNumber) {
        this.historicalNumber = historicalNumber;
    }

    public BigDecimal getHistoricalOwing() {
        return historicalOwing;
    }

    public void setHistoricalOwing(BigDecimal historicalOwing) {
        this.historicalOwing = historicalOwing;
    }
    
}
