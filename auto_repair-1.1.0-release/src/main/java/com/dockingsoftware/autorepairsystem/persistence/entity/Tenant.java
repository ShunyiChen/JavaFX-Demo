/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.persistence.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "D_TENANT")
public class Tenant implements Serializable {
    
    @Id
    @GenericGenerator(name="systemUUID",strategy="uuid")
    @GeneratedValue(generator="systemUUID")
    @Column(name = "ID", insertable = true, updatable = false, nullable = false)
    private String id;
    
    /* 租户名 */
    @Column(name = "NAME", insertable = true, updatable = true, nullable = true)
    private String name;
    
    /* 联系人 */
    @Column(name = "CONTACTS", insertable = true, updatable = true, nullable = true)
    private String contacts;
    
    /* 联系电话 */
    @Column(name = "PHONE_NO", insertable = true, updatable = true, nullable = true)
    private String phoneNo;
    
    /* 备注 */
    @Column(name = "NOTES", insertable = true, updatable = true, nullable = true)
    private String notes;

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

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
    
}
