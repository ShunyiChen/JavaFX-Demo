/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dockingsoftware.autorepairsystem.component.model;

import javax.persistence.Column;

/**
 * 供货商模型
 *
 * @author Shunyi Chen
 */
public class SupplierModel {
    
    /** 序号 */
    private int No;
    
    /** ID */
    private String id;
    
    /**  供货商编码 */
    private String SN;
    
    /**  供货商名称 */
    private String name;
    
    /** 供货商电话 */
    private String tel;
    
    /** 供货商传真 */
    private String fax;
    
    /** 供货商地址 */
    private String address;
    
    /** 供货商邮编 */
    private String postalCode;
    
    /** 联系人 */
    @Column(name = "SUPPLIER_CONTACTS", insertable = true, updatable = true, nullable = true)
    private String contacts;
    
    /**  联系人电话 */
    @Column(name = "CONTACTS_PHONE", insertable = true, updatable = true, nullable = true)
    private String contactsPhone;
    
    /**  供货商公司全称 */
    @Column(name = "COMPANY_FULLNAME", insertable = true, updatable = true, nullable = true)
    private String companyFullName;
    
    /* 法人 */
    @Column(name = "COMPANY_LEGAL", insertable = true, updatable = true, nullable = true)
    private String LEGAL;
    
    /** 注册银行名称 */
    @Column(name = "BANK_FULLNAME", insertable = true, updatable = true, nullable = true)
    private String bankFullName;
    
    /**  银行开户帐号 */
    @Column(name = "ACCOUNT", insertable = true, updatable = true, nullable = true)
    private String account;
    
    /**  税号 */
    @Column(name = "CNPJ", insertable = true, updatable = true, nullable = true)
    private String CNPJ;
    
    /** 开票地址 */
    @Column(name = "BILLING_ADDRESS", insertable = true, updatable = true, nullable = true)
    private String billingAddress;
    
    /** 备注 */
    @Column(name = "NOTES", insertable = true, updatable = true, nullable = true)
    private String notes;

    public int getNo() {
        return No;
    }

    public void setNo(int No) {
        this.No = No;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getContactsPhone() {
        return contactsPhone;
    }

    public void setContactsPhone(String contactsPhone) {
        this.contactsPhone = contactsPhone;
    }

    public String getCompanyFullName() {
        return companyFullName;
    }

    public void setCompanyFullName(String companyFullName) {
        this.companyFullName = companyFullName;
    }

    public String getLEGAL() {
        return LEGAL;
    }

    public void setLEGAL(String LEGAL) {
        this.LEGAL = LEGAL;
    }

    public String getBankFullName() {
        return bankFullName;
    }

    public void setBankFullName(String bankFullName) {
        this.bankFullName = bankFullName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getCNPJ() {
        return CNPJ;
    }

    public void setCNPJ(String CNPJ) {
        this.CNPJ = CNPJ;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    
}
