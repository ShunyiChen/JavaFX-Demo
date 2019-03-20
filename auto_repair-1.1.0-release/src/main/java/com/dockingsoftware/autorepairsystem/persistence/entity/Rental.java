/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.persistence.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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

/**
 * 烤房租户
 * 
 * @author Shunyi Chen
 */
@Entity
@Table(name = "D_RENTAL")
public class Rental implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TenantCar> tenantCars = new ArrayList<>();
    
    @Id
    @GenericGenerator(name="systemUUID",strategy="uuid")
    @GeneratedValue(generator="systemUUID")
    @Column(name = "ID", insertable = true, updatable = false, nullable = false)
    private String id;

    /** 单号 */
    @Column(name = "SN", insertable = true, updatable = true, nullable = true)
    private String SN;
    
    /** 开单日期 */
    @Column(name = "BILLING_DATE", insertable = true, updatable = true, nullable = true)
    private Date billingDate;
    
    /* 租户ID */
    @Column(name = "TENANT_ID", insertable = true, updatable = true, nullable = true)
    private String tenantId;
    
    /* 租户名 */
    @Column(name = "TENANT_NAME", insertable = true, updatable = true, nullable = true)
    private String tenantName;
    
    /* 联系人 */
    @Column(name = "CONTACTS", insertable = true, updatable = true, nullable = true)
    private String contacts;
    
    /* 联系电话 */
    @Column(name = "PHONE_NO", insertable = true, updatable = true, nullable = true)
    private String phoneNo;
    
    /** 优惠金额 */
    @Column(name = "DISCOUNT_AMOUNT", insertable = true, updatable = true, nullable = true)
    private BigDecimal discountAmount;
    
    /** 优惠原因 */
    @Column(name = "DISCOUNT_REASON", insertable = true, updatable = true, nullable = true)
    private String discountReason;
    
    /** 应收租金 */
    @Column(name = "RECEIVABLE_AMOUNT", insertable = true, updatable = true, nullable = true)
    private BigDecimal receivableAmount;
    
    /** 实际支付金额 */
    @Column(name = " ACTUALLY_PAY", insertable = true, updatable = true, nullable = true)
    private BigDecimal actuallyPay;
    
    /** 已收金额 */
    @Column(name = "AMOUNT_RECEIVED", insertable = true, updatable = true, nullable = true)
    private BigDecimal amountReceived;
    
    /** 尚欠金额 */
    @Column(name = "OWING_AMOUNT", insertable = true, updatable = true, nullable = true)
    private BigDecimal owingAmount ;
    
    /** 结算日期 */
    @Column(name = "SETTLEMENT_DATE", insertable = true, updatable = true, nullable = true)
    private Date settlementDate;
    
    /** 结算方式 */
    @Column(name = "PAYMENT", insertable = true, updatable = true, nullable = true)
    private String payment;
    
    /** 结算备注 */
    @Column(name = "SETTLEMENT_NOTES", insertable = true, updatable = true, nullable = true)
    private String settlementNotes;
    
    /** 结算状态 */
    @Column(name = "SETTLEMENT_STATE", insertable = true, updatable = true, nullable = true)
    private String settlementState;

    public List<TenantCar> getTenantCars() {
        return tenantCars;
    }

    public void setTenantCars(List<TenantCar> tenantCars) {
        this.tenantCars = tenantCars;
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

    public Date getBillingDate() {
        return billingDate;
    }

    public void setBillingDate(Date billingDate) {
        this.billingDate = billingDate;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
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

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getDiscountReason() {
        return discountReason;
    }

    public void setDiscountReason(String discountReason) {
        this.discountReason = discountReason;
    }

    public BigDecimal getReceivableAmount() {
        return receivableAmount;
    }

    public void setReceivableAmount(BigDecimal receivableAmount) {
        this.receivableAmount = receivableAmount;
    }

    public BigDecimal getActuallyPay() {
        return actuallyPay;
    }

    public void setActuallyPay(BigDecimal actuallyPay) {
        this.actuallyPay = actuallyPay;
    }

    public BigDecimal getAmountReceived() {
        return amountReceived;
    }

    public void setAmountReceived(BigDecimal amountReceived) {
        this.amountReceived = amountReceived;
    }

    public BigDecimal getOwingAmount() {
        return owingAmount;
    }

    public void setOwingAmount(BigDecimal owingAmount) {
        this.owingAmount = owingAmount;
    }

    public Date getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(Date settlementDate) {
        this.settlementDate = settlementDate;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getSettlementNotes() {
        return settlementNotes;
    }

    public void setSettlementNotes(String settlementNotes) {
        this.settlementNotes = settlementNotes;
    }

    public String getSettlementState() {
        return settlementState;
    }

    public void setSettlementState(String settlementState) {
        this.settlementState = settlementState;
    }
    
    
}
