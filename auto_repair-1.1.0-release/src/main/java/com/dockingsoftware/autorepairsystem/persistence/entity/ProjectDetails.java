/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.persistence.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "D_PROJECT_DETAILS")
public class ProjectDetails implements Serializable {

    /** ID */
    @Id
    @GenericGenerator(name="systemUUID",strategy="uuid")
    @GeneratedValue(generator="systemUUID")
    @Column(name = "ID", insertable = true, updatable = false, nullable = false)
    private String id;
    
    /** 项目ID */
    @Column(name = "PROJECT_ID", insertable = true, updatable = false, nullable = false)
    private String projectId;
    
    /* 项目明细名称 */
    @Column(name = "NAME", insertable = true, updatable = true, nullable = true)
    private String name;
    
    /* 项目分类 */
    @Column(name = "PROJECT_CATEGORY", insertable = true, updatable = true, nullable = true)
    private String projectCategory;
    
    /* 工时 */
    @Column(name = "LABOR_HOUR", insertable = true, updatable = true, nullable = true)
    private float laborHour;
    
    /* 单价 */
    @Column(name = "PRICE", insertable = true, updatable = true, nullable = true)
    private BigDecimal price;
    
    /* 优惠金额 */
    @Column(name = "DISCOUNT", insertable = true, updatable = true, nullable = true)
    private BigDecimal discount;
    
    /* 金额 */
    @Column(name = "AMOUNT", insertable = true, updatable = true, nullable = true)
    private BigDecimal amount;
    
    /* 车牌号 */
    @Column(name = "LICENSE_PLATE_NUMBER", insertable = true, updatable = true, nullable = true)
    private String licensePlateNumber;
    
    /* 客户姓名 */
    @Column(name = "CUSTOMER_NAME", insertable = true, updatable = true, nullable = true)
    private String customerName;
    
    /* 修车工 */
    @Column(name = "MECHANIC", insertable = true, updatable = true, nullable = true)
    private String mechanic;
    
    /* 接车人 */
    @Column(name = "PICKING_CAR_PERSON", insertable = true, updatable = true, nullable = true)
    private String pickingCarPerson;
    
    /* 开工时间 */
    @Column(name = "START_TIME", insertable = true, updatable = true, nullable = true)
    private Date startTime;
    
    /* 完工时间 */
    @Column(name = "END_TIME", insertable = true, updatable = true, nullable = true)
    private Date endTime;
    
    /* 结算方式 */
    @Column(name = "PAYMENT_NAME", insertable = true, updatable = true, nullable = true)
    private String paymentName;
    
    /** 结算日期 */
    @Column(name = "SETTLEMENT_DATE", insertable = true, updatable = true, nullable = true)
    private Date settlementDate;
    
    /* 业务类型 */
    @Column(name = "BUSINESS_TYPE", insertable = true, updatable = true, nullable = true)
    private String businessType;
    
    /* 备注 */
    @Column(name = "NOTES", insertable = true, updatable = true, nullable = true)
    private String notes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProjectCategory() {
        return projectCategory;
    }

    public void setProjectCategory(String projectCategory) {
        this.projectCategory = projectCategory;
    }

    public float getLaborHour() {
        return laborHour;
    }

    public void setLaborHour(float laborHour) {
        this.laborHour = laborHour;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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

    public String getMechanic() {
        return mechanic;
    }

    public void setMechanic(String mechanic) {
        this.mechanic = mechanic;
    }

    public String getPickingCarPerson() {
        return pickingCarPerson;
    }

    public void setPickingCarPerson(String pickingCarPerson) {
        this.pickingCarPerson = pickingCarPerson;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }

    public Date getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(Date settlementDate) {
        this.settlementDate = settlementDate;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    
}
