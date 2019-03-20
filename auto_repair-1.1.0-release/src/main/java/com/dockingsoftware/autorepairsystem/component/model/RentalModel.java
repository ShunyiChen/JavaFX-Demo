/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.component.model;

import com.dockingsoftware.autorepairsystem.persistence.entity.TenantCar;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RentalModel {

    private List<TenantCar> tenantCars = new ArrayList<>();
    
    private String No;
    
    private String id;
    /** 单号 */
    private String SN;
    /** 开单日期 */
    private LocalDate billingDate;
    /* 租户ID */
    private String tenantId;
    /* 租户名 */
    private String tenantName;
    /* 联系人 */
    private String contacts;
    /* 联系电话 */
    private String phoneNo;
    /** 优惠金额 */
    private BigDecimal discountAmount;
    /** 优惠原因 */
    private String discountReason;
    /** 应收租金 */
    private BigDecimal receivableAmount;
    /** 实际支付金额 */
    private BigDecimal actuallyPay;
    /** 已收金额 */
    private BigDecimal amountReceived;
    /** 尚欠金额 */
    private BigDecimal owingAmount ;
    /** 结算日期 */
    private LocalDate settlementDate;
    /** 结算方式 */
    private String payment;
    /** 结算备注 */
    private String settlementNotes;
    /** 结算状态 */
    private String settlementState;

    public List<TenantCar> getTenantCars() {
        return tenantCars;
    }

    public void setTenantCars(List<TenantCar> tenantCars) {
        this.tenantCars = tenantCars;
    }

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

    public String getSN() {
        return SN;
    }

    public void setSN(String SN) {
        this.SN = SN;
    }

    public LocalDate getBillingDate() {
        return billingDate;
    }

    public void setBillingDate(LocalDate billingDate) {
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

    public LocalDate getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(LocalDate settlementDate) {
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
