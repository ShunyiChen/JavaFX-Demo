/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dockingsoftware.autorepairsystem.persistence.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "D_IAE")
public class IAE implements Serializable {

    /** ID主键 */
    @Id
    @GenericGenerator(name = "systemUUID", strategy = "uuid")
    @GeneratedValue(generator = "systemUUID")
    @Column(name = "ID", insertable = true, updatable = false, nullable = false)
    private String id;

    /** 科目 */
    @Column(name = "SUBJECT", insertable = true, updatable = true, nullable = true)
    private String subject;
    
    /** 摘要 */
    @Column(name = "SUMMARY", insertable = true, updatable = true, nullable = true)
    private String summary;
    
    /** 收入金额 */
    @Column(name = "INCOME_AMOUNT", insertable = true, updatable = true, nullable = true)
    private BigDecimal incomeAmount;
    
    /** 支出金额 */
    @Column(name = "EXPENSE_AMOUNT", insertable = true, updatable = true, nullable = true)
    private BigDecimal expenseAmount;

    /** 经手人 */
    @Column(name = "HANDLER_NAME", insertable = true, updatable = true, nullable = true)
    private String handlerName;
    
    /** 支付方式 */
    @Column(name = "PAYMENT", insertable = true, updatable = true, nullable = true)
    private String payment;
    
    /** 支付日期 */
    @Column(name = "PAYMENT_DATE", insertable = true, updatable = true, nullable = true)
    private Date paymentDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public BigDecimal getIncomeAmount() {
        return incomeAmount;
    }

    public void setIncomeAmount(BigDecimal incomeAmount) {
        this.incomeAmount = incomeAmount;
    }

    public BigDecimal getExpenseAmount() {
        return expenseAmount;
    }

    public void setExpenseAmount(BigDecimal expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    public String getHandlerName() {
        return handlerName;
    }

    public void setHandlerName(String handlerName) {
        this.handlerName = handlerName;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }
    
    
}
