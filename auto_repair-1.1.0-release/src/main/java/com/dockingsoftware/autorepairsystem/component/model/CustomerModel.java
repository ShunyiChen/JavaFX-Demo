/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.component.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CustomerModel {

    /** 序号 */
    private String No;
    
    /** ID */
    private String id;

    /** 客户姓名 */
    private String name;
    
    /** 联系手机 */
    private String phoneNo;

    /** 客户地址 */
    private String address;
    
    /** 客户单位 */
    private String company;

    /** 客户单位电话 */
    private String tel;

    /** 客户备注 */
    private String notes;
    
    /** 车辆备注 */
    private String carNotes;

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
    
    /** 登记日期 */
    private LocalDate registrationDate;
    
    /** 核定载客 */
    private int approvedPassenger;
    
    /** 年平均行驶公里 */
    private float averageMileage;
    
    /** 使用性质 */
    private String useOfFacilities;
    
    /** 行驶区域 */
    private String area;
    
    /** 新车购置价 */
    private BigDecimal purchaseValue;
    
    /** 车型 */
    private String model;
    
    /** 车身颜色 */
    private String color;

    /** 最新公里数 */
    private int latestMileage;
    
    /** 下次保养里程 */
    private int nextMaintenanceMilage;

    /** 下次保养时间 */
    private LocalDate nextMaintenanceDate;

    /** 提醒保险日期（月,日）*/
    private String insuranceMonthDay;

    /** 提醒检车月份（月） */
    private int inspectionMonth;
    
    /** 保险公司 */
    private String insuranceCompany;
    
    /** 保险备注 */
    private String insuranceNotes;
    
    /** 下次回访时间 */
    private LocalDate nextVisitDate;
    
    /** 下次年审时间 */
    private LocalDate  nextAnnualReviewDate;

    /** 提醒类型 */
    private String reminderType;
    
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

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
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

    public LocalDate getNextVisitDate() {
        return nextVisitDate;
    }

    public void setNextVisitDate(LocalDate nextVisitDate) {
        this.nextVisitDate = nextVisitDate;
    }

    public LocalDate getNextAnnualReviewDate() {
        return nextAnnualReviewDate;
    }

    public void setNextAnnualReviewDate(LocalDate nextAnnualReviewDate) {
        this.nextAnnualReviewDate = nextAnnualReviewDate;
    }

    public String getNo() {
        return No;
    }

    public void setNo(String No) {
        this.No = No;
    }

    public String getReminderType() {
        return reminderType;
    }

    public void setReminderType(String reminderType) {
        this.reminderType = reminderType;
    }
    
}
