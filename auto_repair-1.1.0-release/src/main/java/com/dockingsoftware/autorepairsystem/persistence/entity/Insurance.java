/**
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
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "D_INSURANCE")
public class Insurance implements Serializable {

    /** 商业险项目 */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InsuranceItem> insuranceItems = new ArrayList<>();
    
    /** 客户 */
    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    
    /** ID主键 */
    @Id
    @GenericGenerator(name="systemUUID",strategy="uuid")
    @GeneratedValue(generator="systemUUID")
    @Column(name = "ID", insertable = true, updatable = false, nullable = false)
    private String id;
    
    /** 出单日期 */
    @Column(name = "OUT_DATE", insertable = true, updatable = true, nullable = true)
    private Date outDate;
    
    /** 生效日期 */
    @Column(name = "FROM_DATE", insertable = true, updatable = true, nullable = true)
    private Date fromDate;
    
    /** 被保险人 */
    @Column(name = "NAME", insertable = true, updatable = true, nullable = true)
    private String name;
    
    /** 车牌号 */
    @Column(name = "LICENSE_PLATE_NUMBER", insertable = true, updatable = true, nullable = true)
    private String licensePlateNumber;
    
    /** 商业险金额 */
    @Column(name = "COMMERCE_INSURANCE_AMOUNT", insertable = true, updatable = true, nullable = true)
    private BigDecimal commerceInsuranceAmount;
    
    /** 交强险金额 */
    @Column(name = "COMPULSORY_INSURANCE_AMOUNT", insertable = true, updatable = true, nullable = true)
    private BigDecimal compulsoryInsuranceAmount;
    
    /** 车船使用税 */
    @Column(name = "USAGE_TAX", insertable = true, updatable = true, nullable = true)
    private BigDecimal usageTax;
    
    /** 合计 */
    @Column(name = "TOTAL", insertable = true, updatable = true, nullable = true)
    private BigDecimal total;
    
    /** 备注 */
    @Column(name = "INSURANCE_NOTES", insertable = true, updatable = true, nullable = true)
    private String insuranceNotes;
    
    /** 销售员 */
    @Column(name = "SELLER", insertable = true, updatable = true, nullable = true)
    private String seller;
    
    /** 手续费 */
    @Column(name = "FEE", insertable = true, updatable = true, nullable = true)
    private BigDecimal fee;
    
    /** 客户保费回款 */
    @Column(name = "PAYBACK", insertable = true, updatable = true, nullable = true)
    private BigDecimal payback;
    
    /** 客户返利 */
    @Column(name = "CUSTOMER_REBATE", insertable = true, updatable = true, nullable = true)
    private BigDecimal customerRebate;
    
    /** 收入 */
    @Column(name = "INCOME", insertable = true, updatable = true, nullable = true)
    private BigDecimal income;

    /** 完成状态 */
    @Column(name = "STATUS", insertable = true, updatable = true, nullable = true)
    private String status;
    
    /** 商业险费率 */
    @Column(name = "COMMERCIAL_INSURANCE_RATE", insertable = true, updatable = true, nullable = true)
    private String commercialInsuranceRate;
    
    /** 交强险费率 */
    @Column(name = "COMPULSORY_INSURANCE_RATE", insertable = true, updatable = true, nullable = true)
    private String compulsoryInsuranceRate;

    public List<InsuranceItem> getInsuranceItems() {
        return insuranceItems;
    }

    public void setInsuranceItems(List<InsuranceItem> insuranceItems) {
        this.insuranceItems = insuranceItems;
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

    public Date getOutDate() {
        return outDate;
    }

    public void setOutDate(Date outDate) {
        this.outDate = outDate;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
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
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCommercialInsuranceRate() {
        return commercialInsuranceRate;
    }

    public void setCommercialInsuranceRate(String commercialInsuranceRate) {
        this.commercialInsuranceRate = commercialInsuranceRate;
    }

    public String getCompulsoryInsuranceRate() {
        return compulsoryInsuranceRate;
    }

    public void setCompulsoryInsuranceRate(String compulsoryInsuranceRate) {
        this.compulsoryInsuranceRate = compulsoryInsuranceRate;
    }
    
}
