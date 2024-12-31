package com.example.polinelapeduli.model;

import org.jetbrains.annotations.NotNull;

public class Payment {

    private int paymentId;
    private int amount;
    private String method;
    private String paidAt;
    private int transactionId;
    private int transactionAmount;
    private String createdAt;
    private int donationId;
    private String donationName;
    private int categoryId;
    private String categoryName;


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

    public int getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(int transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getDonationId() {
        return donationId;
    }

    public void setDonationId(int donationId) {
        this.donationId = donationId;
    }

    public String getDonationName() {
        return donationName;
    }

    public void setDonationName(String donationName) {
        this.donationName = donationName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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
