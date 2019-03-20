/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.persistence.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/***
 * 商品
 * @author Shunyi Chen
 */
@Entity
@Table(name = "D_ITEM")
public class Item implements Serializable {
    
    @Id
    @GenericGenerator(name="systemUUID",strategy="uuid")
    @GeneratedValue(generator="systemUUID")
    @Column(name = "ID", insertable = true, updatable = false, nullable = false)
    private String id;
    
    /** 标签ID */
    @Column(name = "TAG_ID", insertable = true, updatable = true, nullable = true)
    private String tagId;
    
    /** 标签名称 */
    @Column(name = "TAG_NAME", insertable = true, updatable = true, nullable = true)
    private String tagName;
    
    /** 商品编码 */
    @Column(name = "SN", insertable = true, updatable = true, nullable = true)
    private String SN;
    
    /** 商品名称 */
    @Column(name = "NAME", insertable = true, updatable = true, nullable = true)
    private String name;
    
    /** 单位 */
    @Column(name = "UNIT", insertable = true, updatable = true, nullable = true)
    private String unit;
    
    /** 初期成本价 */
    @Column(name = "COST_PRICE", insertable = true, updatable = true, nullable = true)
    private BigDecimal costPrice;
    
    /** 初期库存 */
    @Column(name = "STOCK", insertable = true, updatable = true, nullable = true)
    private float stock;
    
    /** 数量 */
    @Column(name = "QUANTITY", insertable = true, updatable = true, nullable = true)
    private float quantity; 
    
    /** 品牌 */
    @Column(name = "BRAND", insertable = true, updatable = true, nullable = true)
    private String brand;
    
    /** 规格 */
    @Column(name = "SPECIFICATION", insertable = true, updatable = true, nullable = true)
    private String specification;
    
    /** 适用车牌号 */
    @Column(name = "SUITABLE_FOR_LICENSE_PLATE_NUMBER", insertable = true, updatable = true, nullable = true)
    private String suitableForLicensePlateNumber;
    
    /** 产地 */
    @Column(name = "ORIGIN", insertable = true, updatable = true, nullable = true)
    private String origin;
    
    /** 销售单价 */
    @Column(name = "SALES_PRICE", insertable = true, updatable = true, nullable = true)
    private BigDecimal salesPrice;
    
    /** 采购单价 */
    @Column(name = "PURCHASE_PRICE", insertable = true, updatable = true, nullable = true)
    private BigDecimal purchasePrice;
    
    /** 采购单号 */
    @Column(name = "PURCHASE_ORDER_SN", insertable = true, updatable = true, nullable = true)
    private String purchaseOrderSN;
    
    /** 供货商名 */
    @Column(name = "SUPPLIER_NAME", insertable = true, updatable = true, nullable = true)
    private String supplierName;
    
    /** 仓库名称 */
    @Column(name = "STORE_NAME", insertable = true, updatable = true, nullable = true)
    private String storeName; 
    
    /** 安全库存 */
    @Column(name = "SAFETY_STOCK", insertable = true, updatable = true, nullable = true)
    private float safetyStock; 
    
    /** 适用车型 */
    @Column(name = "CAR_MODEL", insertable = true, updatable = true, nullable = true)
    private String carModel;
    
    /** OEM码 */
    @Column(name = "OEM_CODE", insertable = true, updatable = true, nullable = true)
    private String oemCode;
    
    /** 备注 */
    @Column(name = "NOTES", insertable = true, updatable = true, nullable = true)
    private String notes;

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

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
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
    
}
