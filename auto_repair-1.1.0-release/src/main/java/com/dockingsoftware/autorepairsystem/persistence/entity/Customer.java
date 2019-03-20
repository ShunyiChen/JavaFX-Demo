/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dockingsoftware.autorepairsystem.persistence.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * 客户
 *
 * @author Shunyi Chen
 */
@Entity
@Table(name = "D_CUSTOMER")
public class Customer implements Serializable {

    /** ID主键 */
    @Id
    @GenericGenerator(name = "systemUUID", strategy = "uuid")
    @GeneratedValue(generator = "systemUUID")
    @Column(name = "ID", insertable = true, updatable = false, nullable = false)
    private String id;

    /** 客户姓名 */
    @Column(name = "NAME", insertable = true, updatable = true, nullable = true)
    private String name;
    
    /** 联系手机 */
    @Column(name = "PHONE_NO", insertable = true, updatable = true, nullable = true)
    private String phoneNo;

    /** 客户地址 */
    @Column(name = "ADDRESS", insertable = true, updatable = true, nullable = true)
    private String address;
    
    /** 客户单位 */
    @Column(name = "COMPANY", insertable = true, updatable = true, nullable = true)
    private String company;

    /** 客户单位电话 */
    @Column(name = "TEL", insertable = true, updatable = true, nullable = true)
    private String tel;

    /** 客户备注 */
    @Column(name = "NOTES", insertable = true, updatable = true, nullable = true)
    private String notes;

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
    
    /** 登记日期 */
    @Column(name = "REGISTRATION_DATE", insertable = true, updatable = true, nullable = true)
    private Date registrationDate;
    
    /** 核定载客 */
    @Column(name = "APPROVED_PASSENGER", insertable = true, updatable = true, nullable = true)
    private int approvedPassenger;
    
    /** 年平均行驶公里 */
    @Column(name = "AVERAGE_MILEAGE", insertable = true, updatable = true, nullable = true)
    private float averageMileage;
    
    /** 使用性质 */
    @Column(name = "USE_OF_FACILITIES", insertable = true, updatable = true, nullable = true)
    private String useOfFacilities;
    
    /** 行驶区域 */
    @Column(name = "AREA", insertable = true, updatable = true, nullable = true)
    private String area;
    
    /** 新车购置价 */
    @Column(name = "PURCHASE_VALUE", insertable = true, updatable = true, nullable = true)
    private BigDecimal purchaseValue;
    
    /** 车型 */
    @Column(name = "MODEL", insertable = true, updatable = true, nullable = true)
    private String model;
    
    /** 车身颜色 */
    @Column(name = "COLOR", insertable = true, updatable = true, nullable = true)
    private String color;

    /** 车辆备注 */
    @Column(name = "CAR_NOTES", insertable = true, updatable = true, nullable = true)
    private String carNotes;
    
    /** 最新公里数 */
    @Column(name = "LATEST_MILEAGE", insertable = true, updatable = true, nullable = true)
    private int latestMileage;
    
    /** 下次保养里程 */
    @Column(name = "NEXT_MAINTENANCE_MILAGE", insertable = true, updatable = true, nullable = true)
    private int nextMaintenanceMilage;

    /** 下次保养时间 */
    @Column(name = "NEXT_MAINTENANCE_DATE", insertable = true, updatable = true, nullable = true)
    private Date nextMaintenanceDate;

    /** 提醒保险日期（月,日）*/
    @Column(name = "INSURANCE_DATE", insertable = true, updatable = true, nullable = true)
    private String insuranceMonthDay;

    /** 提醒检车月份（月） */
    @Column(name = "INSPECTION_MONTH", insertable = true, updatable = true, nullable = true)
    private int inspectionMonth;
    
    /** 保险公司 */
    @Column(name = "INSURANCE_COMPANY", insertable = true, updatable = true, nullable = true)
    private String insuranceCompany;
    
    /** 保险备注 */
    @Column(name = "INSURANCE_NOTES", insertable = true, updatable = true, nullable = true)
    private String insuranceNotes;
    
    /** 下次回访时间 */
    @Column(name = "NEXT_VISIT_DATE", insertable = true, updatable = true, nullable = true)
    private Date nextVisitDate;
    
    /** 下次年审时间 */
    @Column(name = "NEXT_ANNUAL_REVIEW_DATE", insertable = true, updatable = true, nullable = true)
    private Date  nextAnnualReviewDate;

//    /** 历史消费 */
//    @Column(name = "HISTORICAL_AMOUNT", insertable = true, updatable = true, nullable = true)
//    private BigDecimal historicalAmount;
//    
//    /** 历史单数 */
//    @Column(name = "HISTORICAL_NUMBER", insertable = true, updatable = true, nullable = true)
//    private int historicalNumber;
//    
//    /** 历史欠款 */
//    @Column(name = "HISTORICAL_OWING", insertable = true, updatable = true, nullable = true)
//    private BigDecimal historicalOwing;
    
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

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public int getApprovedPassenger() {
        return approvedPassenger;
    }

    public void setApprovedPassenger(int approvedPassenger) {
        this.approvedPassenger = approvedPassenger;
    }

    public float getAverageMileage() {
        return averageMileage;
    }

    public void setAverageMileage(float averageMileage) {
        this.averageMileage = averageMileage;
    }

    public String getUseOfFacilities() {
        return useOfFacilities;
    }

    public void setUseOfFacilities(String useOfFacilities) {
        this.useOfFacilities = useOfFacilities;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public BigDecimal getPurchaseValue() {
        return purchaseValue;
    }

    public void setPurchaseValue(BigDecimal purchaseValue) {
        this.purchaseValue = purchaseValue;
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

    public String getCarNotes() {
        return carNotes;
    }

    public void setCarNotes(String carNotes) {
        this.carNotes = carNotes;
    }

    public int getLatestMileage() {
        return latestMileage;
    }

    public void setLatestMileage(int latestMileage) {
        this.latestMileage = latestMileage;
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

    public String getInsuranceCompany() {
        return insuranceCompany;
    }

    public void setInsuranceCompany(String insuranceCompany) {
        this.insuranceCompany = insuranceCompany;
    }

    public String getInsuranceNotes() {
        return insuranceNotes;
    }

    public void setInsuranceNotes(String insuranceNotes) {
        this.insuranceNotes = insuranceNotes;
    }

    public Date getNextVisitDate() {
        return nextVisitDate;
    }

    public void setNextVisitDate(Date nextVisitDate) {
        this.nextVisitDate = nextVisitDate;
    }

    public Date getNextAnnualReviewDate() {
        return nextAnnualReviewDate;
    }

    public void setNextAnnualReviewDate(Date nextAnnualReviewDate) {
        this.nextAnnualReviewDate = nextAnnualReviewDate;
    }

//    public BigDecimal getHistoricalAmount() {
//        return historicalAmount;
//    }
//
//    public void setHistoricalAmount(BigDecimal historicalAmount) {
//        this.historicalAmount = historicalAmount;
//    }
//
//    public int getHistoricalNumber() {
//        return historicalNumber;
//    }
//
//    public void setHistoricalNumber(int historicalNumber) {
//        this.historicalNumber = historicalNumber;
//    }
//
//    public BigDecimal getHistoricalOwing() {
//        return historicalOwing;
//    }
//
//    public void setHistoricalOwing(BigDecimal historicalOwing) {
//        this.historicalOwing = historicalOwing;
//    }
    
    
    
}
