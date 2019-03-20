/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.component.model;

public class TenantCarModel {

    /** 序号 */
    private String No;
    /** ID主键 */
    private String id;
    /** 单号 */
    private String SN;
    /** 号码号牌 */
    private String licensePlateNumber;
    /** 烤漆部位 */
    private String paintParts;
    /** 备注 */
    private String notes;

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

    public String getLicensePlateNumber() {
        return licensePlateNumber;
    }

    public void setLicensePlateNumber(String licensePlateNumber) {
        this.licensePlateNumber = licensePlateNumber;
    }

    public String getPaintParts() {
        return paintParts;
    }

    public void setPaintParts(String paintParts) {
        this.paintParts = paintParts;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

}
