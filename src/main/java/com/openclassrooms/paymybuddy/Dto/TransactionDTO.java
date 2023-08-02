package com.openclassrooms.paymybuddy.Dto;

import lombok.Data;

@Data
public class TransactionDTO {

    private String connection;
    private double amount;
    private String description;

    public TransactionDTO() {
    }
    public TransactionDTO(String connection, double amount, String description) {
        this.connection = connection;
        this.amount = amount;
        this.description = description;
    }

    public String getConnection() {
        return connection;
    }

    public void setConnection(String connection) {
        this.connection = connection;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
