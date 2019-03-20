/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.component.model;

import com.dockingsoftware.autorepairsystem.persistence.entity.SettlementDetails;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class SettlementModel {
    
    /** 明细 */
    private List<SettlementDetails> details = new ArrayList<>();
    
    /** 选择 */
    private final BooleanProperty invited = new SimpleBooleanProperty(false);
    
    /** 序号 */
    private String No;
    
    /** ID主键 */
    private String id;
    
    /** 单号 */
    private String SN;
    
    /** 开单日期 */
    private LocalDate billingDate;
    
    /** 开单对象 */
    private String billingObject;
    
    /** 进厂日期 */
    private LocalDate enteringDate;
    
    /** 送修人 */
    private String sender;
    
    /** 客户ID */
    private String customerId;
    
    /** 客户姓名 */
    private String customerName;
    
    /** 客户联系电话 */
    private String phoneNo;
    
    /** 号码号牌 */
    private String licensePlateNumber;
    
    /** 厂牌型号 */
    private String factoryPlateModel ;
    
    /** VIN码 */
    private String vinCode;
    
    /** 机动车种类名称 */
    private String vehicleTypeName;
    
    /** 发动机号 */
    private String engineNo;
    
    /** 车型 */
    private String model;
    
    /** 车身颜色 */
    private String color;
    
    /** 最新公里数 */
    private int latestMileage;
    
    /** 上次公里数 */
    private int lastMileage;
    
    /** 登记日期 */
    private LocalDate registrationDate;
    
     /** 下次保养里程 */
    private int nextMaintenanceMilage;

    /** 下次保养时间 */
    private LocalDate nextMaintenanceDate;

    /** 提醒保险日期（月,日）*/
    private String insuranceMonthDay;

    /** 提醒检车月份（月） */
    private int inspectionMonth;
    
    /** 业务类型 */
    private String businessType;
    
    /** 油量 */
    private String oil;
    
    /** 接车人 */
    private String receptionist;
    
    /** 故障描述 */
    private String description ;
    
    /** 维修建议 */
    private String recommend;
    
    /** 备注 */
    private String notes;

    /** 优惠金额 */
    private BigDecimal discountAmount;
    
    /** 优惠原因 */
    private String discountReason;
    
    /** 应收金额 */
    private BigDecimal receivableAmount;
    
    /** 实际支付金额 */
    private BigDecimal actuallyPay;
    
    /** 已收金额 */
    private BigDecimal amountReceived;
    
    /** 尚欠金额 */
    private BigDecimal owingAmount;
    
    /** 结算日期 */
    private LocalDate settlementDate;
    
    /** 结算方式 */
    private String payment;
    
    /** 结算备注 */
    private String settlementNotes;
    
    /** 业务状态 */
    private String businessState;
    
    /** 结算状态 */
    private String settlementState;
    
    // 附加
    private BigDecimal costPrice;
    private BigDecimal itemAmount;
    private BigDecimal projectAmount;
    private BigDecimal profit;

    // 附加2
    private String clientName;
    private String contacts;
    
    public BooleanProperty invitedProperty() { 
        return invited;
    }

    public List<SettlementDetails> getDetails() {
        return details;
    }

    public void setDetails(List<SettlementDetails> details) {
        this.details = details;
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

    public String getBillingObject() {
        return billingObject;
    }

    public void setBillingObject(String billingObject) {
        this.billingObject = billingObject;
    }

    public LocalDate getEnteringDate() {
        return enteringDate;
    }

    public void setEnteringDate(LocalDate enteringDate) {
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

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
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

    public LocalDate getNextMaintenanceDate() {
        return nextMaintenanceDate;
    }

    public void setNextMaintenanceDate(LocalDate nextMaintenanceDate) {
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

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public BigDecimal getItemAmount() {
        return itemAmount;
    }

    public void setItemAmount(BigDecimal itemAmount) {
        this.itemAmount = itemAmount;
    }

    public BigDecimal getProjectAmount() {
        return projectAmount;
    }

    public void setProjectAmount(BigDecimal projectAmount) {
        this.projectAmount = projectAmount;
    }

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }
    
}
