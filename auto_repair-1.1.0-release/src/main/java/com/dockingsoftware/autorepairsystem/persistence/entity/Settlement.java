/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.persistence.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "D_SETTLEMENT")
public class Settlement implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<SettlementDetails> details = new ArrayList<>();
    
    /** ID主键 */
    @Id
    @GenericGenerator(name="systemUUID",strategy="uuid")
    @GeneratedValue(generator="systemUUID")
    @Column(name = "ID", insertable = true, updatable = false, nullable = false)
    private String id;
    
    /** 单号 */
    @Column(name = "SN", insertable = true, updatable = true, nullable = true)
    private String SN;
    
    /** 开单对象 */
    @Column(name = "BILLING_OBJECT", insertable = true, updatable = true, nullable = true)
    private String billingObject;
    
    /** 开单日期 */
    @Column(name = "BILLING_DATE", insertable = true, updatable = true, nullable = true)
    private Date billingDate;
    
    /** 客户ID */
    @Column(name = "CLIENT_ID", insertable = true, updatable = true, nullable = true)
    private String clientId;
    
    /** 客户名 */
    @Column(name = "CLIENT_NAME", insertable = true, updatable = true, nullable = true)
    private String clientName;
    
    /** 联系电话 */
    @Column(name = "PHONE_NO", insertable = true, updatable = true, nullable = true)
    private String phoneNo;
    
    /** 车牌号 */
    @Column(name = "LICENSE_PLATE_NUMBER", insertable = true, updatable = true, nullable = true)
    private String licensePlateNumber;
    
    /** 联系人 */
    @Column(name = "CONTACTS", insertable = true, updatable = true, nullable = true)
    private String contacts;
    
    public List<SettlementDetails> getDetails() {
        return details;
    }

    public void setDetails(List<SettlementDetails> details) {
        this.details = details;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSN() {
        return SN;
    }

    public void setSN(String SN) {
        this.SN = SN;
    }

    public String getBillingObject() {
        return billingObject;
    }

    public void setBillingObject(String billingObject) {
        this.billingObject = billingObject;
    }

    public Date getBillingDate() {
        return billingDate;
    }

    public void setBillingDate(Date billingDate) {
        this.billingDate = billingDate;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getLicensePlateNumber() {
        return licensePlateNumber;
    }

    public void setLicensePlateNumber(String licensePlateNumber) {
        this.licensePlateNumber = licensePlateNumber;
    }
    
    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }
 
}
