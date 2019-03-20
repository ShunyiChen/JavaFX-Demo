/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.persistence.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "D_PARAMETER")
public class Parameter implements Serializable {

    @Id
    @Column(name = "ID", insertable = true, updatable = false, nullable = false)
    private String id;

    /* VALUE */
    @Column(name = "VALUE", insertable = true, updatable = true, nullable = true)
    private String value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static Parameter create(String id, String value) {
        Parameter p = new Parameter();
        p.setId(id);
        p.setValue(value);
        return p;
    }
}
