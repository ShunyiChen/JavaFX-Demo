/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.component.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Rent {
    
    private final StringProperty id;
    private final StringProperty SN;
    private final StringProperty billingDate;
    private final StringProperty tenantId;
    private final StringProperty tenantName;
    private final StringProperty contacts;
    private final StringProperty phoneNo;
    private final StringProperty discountAmount;
    private final StringProperty discountReason;
    private final StringProperty receivableAmount;
    private final StringProperty actuallyPay;
    private final StringProperty amountReceived;
    private final StringProperty owingAmount;
    private final StringProperty settlementDate;
    private final StringProperty payment;
    private final StringProperty settlementNotes;
    private final StringProperty settlementState;
    private final StringProperty licensePlateNumber;
    private final StringProperty paintParts;
    private final StringProperty notes;
    
    public Rent(
            String id,
            String SN,
            String billingDate,
            String tenantId,
            String tenantName,
            String contacts,
            String phoneNo,
            String discountAmount,
            String discountReason,
            String receivableAmount,
            String actuallyPay,
            String amountReceived,
            String owingAmount,
            String settlementDate,
            String payment,
            String settlementNotes,
            String settlementState,
            String licensePlateNumber,
            String paintParts,
            String notes) {
        this.id = new SimpleStringProperty(id);
        this.SN = new SimpleStringProperty(SN);
        this.billingDate = new SimpleStringProperty(billingDate);
        this.tenantId = new SimpleStringProperty(tenantId);
        this.tenantName = new SimpleStringProperty(tenantName);
        this.contacts = new SimpleStringProperty(contacts);
        this.phoneNo = new SimpleStringProperty(phoneNo);
        this.discountAmount = new SimpleStringProperty(discountAmount);
        this.discountReason = new SimpleStringProperty(discountReason);
        this.receivableAmount = new SimpleStringProperty(receivableAmount);
        this.actuallyPay = new SimpleStringProperty(actuallyPay);
        this.amountReceived = new SimpleStringProperty(amountReceived);
        this.owingAmount = new SimpleStringProperty(owingAmount);
        this.settlementDate = new SimpleStringProperty(settlementDate);
        this.payment = new SimpleStringProperty(payment);
        this.settlementNotes = new SimpleStringProperty(settlementNotes);
        this.settlementState = new SimpleStringProperty(settlementState);
        this.licensePlateNumber = new SimpleStringProperty(licensePlateNumber);
        this.paintParts = new SimpleStringProperty(paintParts);
        this.notes = new SimpleStringProperty(notes);
    }
    
    public StringProperty idProperty() {
        return id;
    }
    
    public StringProperty SNProperty() {
        return SN;
    }
    
    public StringProperty billingDateProperty() {
        return billingDate;
    }
    
    public StringProperty tenantIdProperty() {
        return tenantId;
    }
    
    public StringProperty tenantNameProperty() {
        return tenantName;
    }
    
    public StringProperty contactsProperty() {
        return contacts;
    }
    
    public StringProperty phoneNoProperty() {
        return phoneNo;
    }
    
    public StringProperty discountAmountProperty() {
        return discountAmount;
    }
    
    public StringProperty discountReasonProperty() {
        return discountReason;
    }
    
    public StringProperty receivableAmountProperty() {
        return receivableAmount;
    }
    
    public StringProperty actuallyPayProperty() {
        return actuallyPay;
    }
    
    public StringProperty amountReceivedProperty() {
        return amountReceived;
    }
    
    public StringProperty owingAmountProperty() {
        return owingAmount;
    }
    
    public StringProperty settlementDateProperty() {
        return settlementDate;
    }
    
    public StringProperty paymentProperty() {
        return payment;
    }
    
    public StringProperty settlementNotesProperty() {
        return settlementNotes;
    }
    
    public StringProperty settlementStateProperty() {
        return settlementState;
    }
    
    public StringProperty licensePlateNumberProperty() {
        return licensePlateNumber;
    }
    
    public StringProperty paintPartsProperty() {
        return paintParts;
    }
    
     public StringProperty notesProperty() {
        return notes;
    }
}
