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
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "D_SETTLEMENT_DETAILS")
public class SettlementDetails implements Serializable {
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectDetails> projects = new ArrayList<>();
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemDetails> items = new ArrayList<>();
    
    /** ID主键 */
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
    
    /** 进厂日期 */
    @Column(name = "ENTERING_DATE", insertable = true, updatable = true, nullable = true)
    private Date enteringDate;
    
    /** 送修人 */
    @Column(name = "SENDER", insertable = true, updatable = true, nullable = true)
    private String sender;
    
    /** 客户ID */
    @Column(name = "CUSTOMER_ID", insertable = true, updatable = true, nullable = true)
    private String customerId;
    
    /** 客户姓名 */
    @Column(name = "CUSTOMER_NAME", insertable = true, updatable = true, nullable = true)
    private String customerName;
    
    /** 客户联系电话 */
    @Column(name = "PHONE_NO", insertable = true, updatable = true, nullable = true)
    private String phoneNo;
    
    /** 号码号牌 */
    @Column(name = "LICENSE_PLATE_NUMBER", insertable = true, updatable = true, nullable = true)
    private String licensePlateNumber;
    
    /** 厂牌型号 */
    @Column(name = "FACTORY_PLATE_MODEL", insertable = true, updatable = true, nullable = true)
    private String factoryPlateModel ;
    
    /** VIN码 */
    @Column(name = "VIN_CODE", insertable = true, updatable = true, nullable = true)
    private String vinCode;
    
    /** 机动车种类名称 */
    @Column(name = "VEHICLE_TYPE_NAME", insertable = true, updatable = true, nullable = true)
    private String vehicleTypeName;
    
    /** 发动机号 */
    @Column(name = "ENGINE_NO", insertable = true, updatable = true, nullable = true)
    private String engineNo;
    
    /** 车型 */
    @Column(name = "MODEL", insertable = true, updatable = true, nullable = true)
    private String model;
    
    /** 车身颜色 */
    @Column(name = "COLOR", insertable = true, updatable = true, nullable = true)
    private String color;
    
    /** 登记日期 */
    @Column(name = "REGISTRATION_DATE", insertable = true, updatable = true, nullable = true)
    private Date registrationDate;
    
    /** 最新公里数 */
    @Column(name = "LATEST_MILEAGE", insertable = true, updatable = true, nullable = true)
    private int latestMileage;
    
    /** 上次公里数 */
    @Column(name = "LAST_MILEAGE", insertable = true, updatable = true, nullable = true)
    private int lastMileage;
    
     /** 下次保养里程 */
    @Column(name = "NEXT_MAINTENANCE_MILAGE", insertable = true, updatable = true, nullable = true)
    private int nextMaintenanceMilage;

    /** 下次保养时间 */
    @Column(name = "NEXT_MAINTENANCE_DATE", insertable = true, updatable = true, nullable = true)
    private Date nextMaintenanceDate;

    /** 提醒保险日期（月,日）*/
    @Column(name = "INSURANCE_MONTH_DAY", insertable = true, updatable = true, nullable = true)
    private String insuranceMonthDay;

    /** 提醒检车月份（月） */
    @Column(name = "INSPECTION_MONTH", insertable = true, updatable = true, nullable = true)
    private int inspectionMonth;
    
    /** 业务类型 */
    @Column(name = "BUSINESS_TYPE", insertable = true, updatable = true, nullable = true)
    private String businessType;
    
    /** 油量 */
    @Column(name = "OIL", insertable = true, updatable = true, nullable = true)
    private String oil;
    
    /** 接车人 */
    @Column(name = "RECEPTIONIST", insertable = true, updatable = true, nullable = true)
    private String receptionist;
    
    /** 故障描述 */
    @Column(name = "DESCRIPTION", insertable = true, updatable = true, nullable = true)
    private String description ;
    
    /** 维修建议 */
    @Column(name = "RECOMMEND", insertable = true, updatable = true, nullable = true)
    private String recommend;
    
    /** 备注 */
    @Column(name = "NOTES", insertable = true, updatable = true, nullable = true)
    private String notes;

    /** 优惠金额 */
    @Column(name = "DISCOUNT_AMOUNT", insertable = true, updatable = true, nullable = true)
    private BigDecimal discountAmount;
    
    /** 优惠原因 */
    @Column(name = "DISCOUNT_REASON", insertable = true, updatable = true, nullable = true)
    private String discountReason;
    
    /** 应收金额 */
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
    private BigDecimal owingAmount;
    
    /** 结算日期 */
    @Column(name = "SETTLEMENT_DATE", insertable = true, updatable = true, nullable = true)
    private Date settlementDate;
    
    /** 结算方式 */
    @Column(name = "PAYMENT", insertable = true, updatable = true, nullable = true)
    private String payment;
    
    /** 结算备注 */
    @Column(name = "SETTLEMENT_NOTES", insertable = true, updatable = true, nullable = true)
    private String settlementNotes;
    
    /** 业务状态 */
    @Column(name = "BUSINESS_STATE", insertable = true, updatable = true, nullable = true)
    private String businessState;
    
    /** 结算状态 */
    @Column(name = "SETTLEMENT_STATE", insertable = true, updatable = true, nullable = true)
    private String settlementState;
    
    public List<ProjectDetails> getProjects() {
        return projects;
    }

    public void setProjects(List<ProjectDetails> projects) {
        this.projects = projects;
    }

    public List<ItemDetails> getItems() {
        return items;
    }

    public void setItems(List<ItemDetails> items) {
        this.items = items;
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

    public Date getEnteringDate() {
        return enteringDate;
    }

    public void setEnteringDate(Date enteringDate) {
        this.enteringDate = enteringDate;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public String getFactoryPlateModel() {
        return factoryPlateModel;
    }

    public void setFactoryPlateModel(String factoryPlateModel) {
        this.factoryPlateModel = factoryPlateModel;
    }

    public String getVinCode() {
        return vinCode;
    }

    public void setVinCode(String vinCode) {
        this.vinCode = vinCode;
    }

    public String getVehicleTypeName() {
        return vehicleTypeName;
    }

    public void setVehicleTypeName(String vehicleTypeName) {
        this.vehicleTypeName = vehicleTypeName;
    }

    public String getEngineNo() {
        return engineNo;
    }

    public void setEngineNo(String engineNo) {
        this.engineNo = engineNo;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public int getLatestMileage() {
        return latestMileage;
    }

    public void setLatestMileage(int latestMileage) {
        this.latestMileage = latestMileage;
    }

    public int getLastMileage() {
        return lastMileage;
    }

    public void setLastMileage(int lastMileage) {
        this.lastMileage = lastMileage;
    }

    public int getNextMaintenanceMilage() {
        return nextMaintenanceMilage;
    }

    public void setNextMaintenanceMilage(int nextMaintenanceMilage) {
        this.nextMaintenanceMilage = nextMaintenanceMilage;
    }

    public Date getNextMaintenanceDate() {
        return nextMaintenanceDate;
    }

    public void setNextMaintenanceDate(Date nextMaintenanceDate) {
        this.nextMaintenanceDate = nextMaintenanceDate;
    }

    public String getInsuranceMonthDay() {
        return insuranceMonthDay;
    }

    public void setInsuranceMonthDay(String insuranceMonthDay) {
        this.insuranceMonthDay = insuranceMonthDay;
    }

    public int getInspectionMonth() {
        return inspectionMonth;
    }

    public void setInspectionMonth(int inspectionMonth) {
        this.inspectionMonth = inspectionMonth;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getOil() {
        return oil;
    }

    public void setOil(String oil) {
        this.oil = oil;
    }

    public String getReceptionist() {
        return receptionist;
    }

    public void setReceptionist(String receptionist) {
        this.receptionist = receptionist;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRecommend() {
        return recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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

    public String getBusinessState() {
        return businessState;
    }

    public void setBusinessState(String businessState) {
        this.businessState = businessState;
    }

    public String getSettlementState() {
        return settlementState;
    }

    public void setSettlementState(String settlementState) {
        this.settlementState = settlementState;
    }
}
