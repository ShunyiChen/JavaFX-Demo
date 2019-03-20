/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dockingsoftware.autorepairsystem.component.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Inventory {
    
    private final StringProperty id;
    private final StringProperty SN;
    private final StringProperty billingObject;
    private final StringProperty billingDate;
    private final StringProperty clientName;
    private final StringProperty phoneNo;
    private final StringProperty licensePlateNumber;
    private final StringProperty contacts;
    private final StringProperty notes;
    private final StringProperty discountAmount;
    private final StringProperty discountReason;
    private final StringProperty receivableAmount;
    private final StringProperty actuallyPay;
    private final StringProperty amountReceived;
    private final StringProperty owingAmount;
    private final StringProperty settlementDate;
    private final StringProperty payment;
    private final StringProperty settlementNotes;
    private final StringProperty businessState;
    private final StringProperty settlementState;
    
    
    public Inventory(
            String id,
            String SN,
            String billingObject,
            String billingDate,
            String clientName,
            String phoneNo,
            String licensePlateNumber,
            String contacts,
            String notes,
            String discountAmount,
            String discountReason,
            String receivableAmount,
            String actuallyPay,
            String amountReceived,
            String owingAmount,
            String settlementDate,
            String payment,
            String settlementNotes,
            String businessState,
            String settlementState) {
        this.id = new SimpleStringProperty(id);
        this.SN = new SimpleStringProperty(SN);
        this.billingObject = new SimpleStringProperty(billingObject);
        this.billingDate = new SimpleStringProperty(billingDate);
        this.clientName = new SimpleStringProperty(clientName);
        this.phoneNo = new SimpleStringProperty(phoneNo);
        this.licensePlateNumber = new SimpleStringProperty(licensePlateNumber);
        this.contacts = new SimpleStringProperty(contacts);
        this.notes = new SimpleStringProperty(notes);
        this.discountAmount = new SimpleStringProperty(discountAmount);
        this.discountReason = new SimpleStringProperty(discountReason);
        this.receivableAmount = new SimpleStringProperty(receivableAmount);
        this.actuallyPay = new SimpleStringProperty(actuallyPay);
        this.amountReceived = new SimpleStringProperty(amountReceived);
        this.owingAmount = new SimpleStringProperty(owingAmount);
        this.settlementDate = new SimpleStringProperty(settlementDate);
        this.payment = new SimpleStringProperty(payment);
        this.settlementNotes = new SimpleStringProperty(settlementNotes);
        this.businessState = new SimpleStringProperty(businessState);
        this.settlementState = new SimpleStringProperty(settlementState);
    }
    
    public StringProperty idProperty() {
        return id;
    }
    
    public StringProperty SNProperty() {
        return SN;
    }

    public StringProperty billingObjectProperty() {
        return billingObject;
    }
    
    public StringProperty billingDateProperty() {
        return billingDate;
    }
    
    public StringProperty clientNameProperty() {
        return clientName;
    }
    
    public StringProperty phoneNoProperty() {
        return phoneNo;
    }
    
    public StringProperty licensePlateNumberProperty() {
        return licensePlateNumber;
    }
    
    public StringProperty contactsProperty() {
        return contacts;
    }
   
    public StringProperty notesProperty() {
        return notes;
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
    
    public StringProperty businessStateProperty() {
        return businessState;
    }
    
    public StringProperty settlementStateProperty() {
        return settlementState;
    }
    
}
