/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dockingsoftware.autorepairsystem.component.model;

import java.math.BigDecimal;
import java.time.LocalDate;
/**
 * 项目/项目明细模型
 * @author Shunyi Chen
 */
public class ProjectModel {
    
    /** 单号ID */
    private String orderId;
    /** 单号ID */
    private String orderSN;
    /** 序号 */
    private String No;
    /** ID */
    private String id;
    /** 项目ID */
    private String projectId;
    /** 项目编码 */
    private String SN;
    /** 项目名称 */
    private String name;
    /** 单价 */
    private BigDecimal price;
    /** 工时  */
    private Float laborHour;
    /** 优惠 */
    private BigDecimal discount;
    /** 金额 */
    private BigDecimal amount;
    /** 开始时间 */
    private LocalDate startTime;
    /** 完工时间 */
    private LocalDate endTime;
     /** 车牌号 */
    private String licensePlateNumber;
    /** 客户姓名 */
    private String customerName;
    /** 施工人员 */
    private String mechanic;
    /** 项目分类	 */
    private String projectCategory;
    /** 项目备注 */
    private String notes;
    /** 接车人 */
    private String pickingCarPerson;
    /** 结算方式 */
    private String paymentName;
    /** 结算日期 */
    private LocalDate settlementDate;
    /** 业务类型 */
    private String businessType;
    
    // 附加
    private LocalDate billingDate;
    private String payment;
    private String settlementNotes;
    private String settlementState;
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNo() {
        return No;
    }

    public void setNo(String No) {
        this.No = No;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Float getLaborHour() {
        return laborHour;
    }

    public void setLaborHour(Float laborHour) {
        this.laborHour = laborHour;
    }

    public BigDecimal getDiscount() {
        if (discount == null) {
            return new BigDecimal(0.00); 
        } 
        return discount.setScale(2,java.math.BigDecimal.ROUND_HALF_UP);
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getAmount() {
        if (amount == null) {
            return new BigDecimal(0.00); 
        } 
        return amount.setScale(2,java.math.BigDecimal.ROUND_HALF_UP);
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDate startTime) {
        this.startTime = startTime;
    }

    public LocalDate getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDate endTime) {
        this.endTime = endTime;
    }

    public String getMechanic() {
        return mechanic;
    }

    public void setMechanic(String mechanic) {
        this.mechanic = mechanic;
    }

    public String getProjectCategory() {
        return projectCategory;
    }

    public void setProjectCategory(String projectCategory) {
        this.projectCategory = projectCategory;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getLicensePlateNumber() {
        return licensePlateNumber;
    }

    public void setLicensePlateNumber(String licensePlateNumber) {
        this.licensePlateNumber = licensePlateNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPickingCarPerson() {
        return pickingCarPerson;
    }

    public void setPickingCarPerson(String pickingCarPerson) {
        this.pickingCarPerson = pickingCarPerson;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }

    public LocalDate getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(LocalDate settlementDate) {
        this.settlementDate = settlementDate;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getOrderSN() {
        return orderSN;
    }

    public void setOrderSN(String orderSN) {
        this.orderSN = orderSN;
    }

    public LocalDate getBillingDate() {
        return billingDate;
    }

    public void setBillingDate(LocalDate billingDate) {
        this.billingDate = billingDate;
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
