/*
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

/**
 * 商品明细
 * 
 * @author Shunyi Chen
 */
@Entity
@Table(name = "D_ITEM_DETAILS")
public class ItemDetails implements Serializable {
    
    /** 商品明细ID */
    @Id
    @GenericGenerator(name="systemUUID",strategy="uuid")
    @GeneratedValue(generator="systemUUID")
    @Column(name = "ID", insertable = true, updatable = false, nullable = false)
    private String id;
    
    /** 商品ID */
    @Column(name = "ITEM_ID", insertable = true, updatable = true, nullable = true)
    private String itemId;
    
    /** 商品名称 */
    @Column(name = "NAME", insertable = true, updatable = true, nullable = true)
    private String name;
    
    /** 销售单价 */
    @Column(name = "SALES_PRICE", insertable = true, updatable = true, nullable = true)
    private BigDecimal salesPrice;
    
    /** 成本价 */
    @Column(name = "COST_PRICE", insertable = true, updatable = true, nullable = true)
    private BigDecimal costPrice;
    
    /** 数量 */
    @Column(name = "QUANTITY", insertable = true, updatable = true, nullable = true)
    private float quantity;
    
    /** 优惠 */
    @Column(name = "DISCOUNT", insertable = true, updatable = true, nullable = true)
    private BigDecimal discount;
    
    /** 金额 */
    @Column(name = "AMOUNT", insertable = true, updatable = true, nullable = true)
    private BigDecimal amount;
    
    /** 利润 */
    @Column(name = "PROFIT", insertable = true, updatable = true, nullable = true)
    private BigDecimal profit;
    
    /** 备注 */
    @Column(name = "NOTES", insertable = true, updatable = true, nullable = true)
    private String notes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getSalesPrice() {
        return salesPrice;
    }

    public void setSalesPrice(BigDecimal salesPrice) {
        this.salesPrice = salesPrice;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    
}
