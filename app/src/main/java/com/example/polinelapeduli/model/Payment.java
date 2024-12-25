package com.example.polinelapeduli.model;

import org.jetbrains.annotations.NotNull;

public class Payment {

    private int paymentId;
    private int transactionId;
    private int amount;
    private String method;
    private String paidAt;

    public Payment(){}

    public Payment(int paymentId, int transactionId, int amount, String method, String paidAt) {
        this.paymentId = paymentId;
        this.transactionId = transactionId;
        this.amount = amount;
        this.method = method;
        this.paidAt = paidAt;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(String paidAt) {
        this.paidAt = paidAt;
    }

    @NotNull
    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", transactionId=" + transactionId +
                ", amount=" + amount +
                ", method='" + method + '\'' +
                ", paidAt='" + paidAt + '\'' +
                '}';
    }
}
