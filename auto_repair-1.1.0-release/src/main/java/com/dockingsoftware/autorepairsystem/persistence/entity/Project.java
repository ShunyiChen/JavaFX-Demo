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
 * 项目
 * @author Shunyi Chen
 */
@Entity
@Table(name = "D_PROJECT")
public class Project implements Serializable {
    
    @Id
    @GenericGenerator(name="systemUUID",strategy="uuid")
    @GeneratedValue(generator="systemUUID")
    @Column(name = "ID", insertable = true, updatable = false, nullable = false)
    private String id;
    
    /* 项目编码 */
    @Column(name = "SN", insertable = true, updatable = true, nullable = true)
    private String SN;
    
    /* 名称 */
    @Column(name = "NAME", insertable = true, updatable = true, nullable = true)
    private String name;

    /* 工时 */
    @Column(name = "LABOR_HOUR", insertable = true, updatable = true, nullable = true)
    private float laborHour;
    
    /* 单价 */
    @Column(name = "PRICE", insertable = true, updatable = true, nullable = true)
    private BigDecimal price;
    
    /* 项目分类 */
    @Column(name = "PROJECT_CATEGORY", insertable = true, updatable = true, nullable = true)
    private String projectCategory;
    
    /* 备注 */
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

    public String getProjectCategory() {
        return projectCategory;
    }

    public void setProjectCategory(String projectCategory) {
        this.projectCategory = projectCategory;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
    
}