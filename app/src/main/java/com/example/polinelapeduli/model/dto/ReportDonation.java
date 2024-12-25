package com.example.polinelapeduli.model.dto;

public class ReportDonation {
    private String donationName;
    private String category;
    private int amount;
    private int targetAmount;

    // Constructor
    public ReportDonation(String donationName, String category, int amount, int targetAmount) {
        this.donationName = donationName;
        this.category = category;
        this.amount = amount;
        this.targetAmount = targetAmount;
    }

    // Getters and Setters
    public String getDonationName() {
        return donationName;
    }

    public void setDonationName(String donationName) {
        this.donationName = donationName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(int targetAmount) {
        this.targetAmount = targetAmount;
    }

    @Override
    public String toString() {
        return "ReportDonation{" +
                "donationName='" + donationName + '\'' +
                ", category='" + category + '\'' +
                ", amount=" + amount +
                ", targetAmount=" + targetAmount +
                '}';
    }
}
