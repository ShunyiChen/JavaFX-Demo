/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.component.model;

import com.dockingsoftware.autorepairsystem.persistence.entity.Customer;
import com.dockingsoftware.autorepairsystem.persistence.entity.InsuranceItem;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class InsuranceModel {
    
     /** 商业险项目 */
    private List<InsuranceItem> items;
    
    /** 序列号 */
    private String No;
    
    /** 客户 */
    private Customer customer;
    
    /** ID主键 */
    private String id;
    
    /** 出单日期 */
    private LocalDate outDate;
    
    /** 生效日期 */
    private LocalDate fromDate;
    
    /** 被保险人 */
    private String name;
    
    /** 车牌号 */
    private String licensePlateNumber;
    
    /** 商业险金额 */
    private BigDecimal commerceInsuranceAmount;
    
    /** 交强险金额 */
    private BigDecimal compulsoryInsuranceAmount;
    
    /** 车船使用税 */
    private BigDecimal usageTax;
    
    /** 合计 */
    private BigDecimal total;
    
    /** 备注 */
    private String insuranceNotes;
    
    /** 销售员 */
    private String Seller;
    
    /** 手续费 */
    private BigDecimal fee;
    
    /** 客户保费回款 */
    private BigDecimal payback;
    
    /** 客户返利 */
    private BigDecimal customerRebate;
    
    /** 收入 */
    private BigDecimal income;
    
    /** 商业险费率 */
    private BigDecimal commercialInsuranceRate;
    
    /** 交强险费率 */
    private BigDecimal compulsoryInsuranceRate;
    
    /** 完成状态 */
    private String status;
    
    public String getNo() {
        return No;
    }

    public void setNo(String No) {
        this.No = No;
    }
    
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getOutDate() {
        return outDate;
    }

    public void setOutDate(LocalDate outDate) {
        this.outDate = outDate;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLicensePlateNumber() {
        return licensePlateNumber;
    }

    public void setLicensePlateNumber(String licensePlateNumber) {
        this.licensePlateNumber = licensePlateNumber;
    }

    public BigDecimal getCommerceInsuranceAmount() {
        return commerceInsuranceAmount;
    }

    public void setCommerceInsuranceAmount(BigDecimal commerceInsuranceAmount) {
        this.commerceInsuranceAmount = commerceInsuranceAmount;
    }

    public BigDecimal getCompulsoryInsuranceAmount() {
        return compulsoryInsuranceAmount;
    }

    public void setCompulsoryInsuranceAmount(BigDecimal compulsoryInsuranceAmount) {
        this.compulsoryInsuranceAmount = compulsoryInsuranceAmount;
    }

    public BigDecimal getUsageTax() {
        return usageTax;
    }

    public void setUsageTax(BigDecimal usageTax) {
        this.usageTax = usageTax;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getInsuranceNotes() {
        return insuranceNotes;
    }

    public void setInsuranceNotes(String insuranceNotes) {
        this.insuranceNotes = insuranceNotes;
    }

    public String getSeller() {
        return Seller;
    }

    public void setSeller(String Seller) {
        this.Seller = Seller;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public BigDecimal getPayback() {
        return payback;
    }

    public void setPayback(BigDecimal payback) {
        this.payback = payback;
    }

    public BigDecimal getCustomerRebate() {
        return customerRebate;
    }

    public void setCustomerRebate(BigDecimal customerRebate) {
        this.customerRebate = customerRebate;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public List<InsuranceItem> getItems() {
        return items;
    }

    public void setItems(List<InsuranceItem> items) {
        this.items = items;
    }

    public BigDecimal getCommercialInsuranceRate() {
        return commercialInsuranceRate;
    }

    public void setCommercialInsuranceRate(BigDecimal commercialInsuranceRate) {
        this.commercialInsuranceRate = commercialInsuranceRate;
    }

    public BigDecimal getCompulsoryInsuranceRate() {
        return compulsoryInsuranceRate;
    }

    public void setCompulsoryInsuranceRate(BigDecimal compulsoryInsuranceRate) {
        this.compulsoryInsuranceRate = compulsoryInsuranceRate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
}
