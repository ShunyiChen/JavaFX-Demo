/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.component.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 商品模型
 * @author Shunyi Chen
 */
public class ItemModel {
    /** 序号 */
    private String No;
    /** ID */
    private String id;
    /** 标签Id */
    private String tagId;
    /** 标签名称 */
    private String tagName;
    /** 商品编码 */
    private String SN;
    /** 商品名称 */
    private String name;
    /** 单位 */
    private String unit;
    /** 初期成本价 */
    private BigDecimal costPrice = new BigDecimal(0);
    /** 初期库存 */
    private float stock; 
    /** 品牌 */
    private String brand;
    /** 规格 */
    private String specification;
    /** 适用车牌号 */
    private String suitableForLicensePlateNumber;
    /** 产地 */
    private String origin;
    /** 销售单价 */
    private BigDecimal salesPrice;
    /** 数量 */
    private float quantity;
    /** 优惠 */
    private BigDecimal discount = new BigDecimal(0);
    /** 金额 */
    private BigDecimal amount = new BigDecimal(0);
    /** 利润 */
    private BigDecimal profit = new BigDecimal(0);
    /** 采购单价 */
    private BigDecimal purchasePrice = new BigDecimal(0);
    /** 采购单号 */
    private String purchaseOrderSN;
    /** 供货商名 */
    private String supplierName;
    /** 仓库名称 */
    private String storeName; 
    /** 安全库存 */
    private float safetyStock; 
    /** 适用车型 */
    private String carModel;
    /** OEM码 */
    private String oemCode;
    /** 备注 */
    private String notes;

    // 附加
    private LocalDate billingDate;
    private String customerName;
    private String licensePlateNumber;
    private String payment;
    private LocalDate settlementDate;
    private String settlementNotes;
    private String settlementState;
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public float getStock() {
        return stock;
    }

    public void setStock(float stock) {
        this.stock = stock;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getSuitableForLicensePlateNumber() {
        return suitableForLicensePlateNumber;
    }

    public void setSuitableForLicensePlateNumber(String suitableForLicensePlateNumber) {
        this.suitableForLicensePlateNumber = suitableForLicensePlateNumber;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public BigDecimal getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(BigDecimal salesPrice) {
        this.salesPrice = salesPrice;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
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

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public String getPurchaseOrderSN() {
        return purchaseOrderSN;
    }

    public void setPurchaseOrderSN(String purchaseOrderSN) {
        this.purchaseOrderSN = purchaseOrderSN;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public float getSafetyStock() {
        return safetyStock;
    }

    public void setSafetyStock(float safetyStock) {
        this.safetyStock = safetyStock;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getOemCode() {
        return oemCode;
    }

    public void setOemCode(String oemCode) {
        this.oemCode = oemCode;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getNo() {
        return No;
    }

    public void setNo(String No) {
        this.No = No;
    }

    public LocalDate getBillingDate() {
        return billingDate;
    }

    public void setBillingDate(LocalDate billingDate) {
        this.billingDate = billingDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getLicensePlateNumber() {
        return licensePlateNumber;
    }

    public void setLicensePlateNumber(String licensePlateNumber) {
        this.licensePlateNumber = licensePlateNumber;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public LocalDate getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(LocalDate settlementDate) {
        this.settlementDate = settlementDate;
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
