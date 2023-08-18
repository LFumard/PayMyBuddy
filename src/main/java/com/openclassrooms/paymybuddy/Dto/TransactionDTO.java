package com.openclassrooms.paymybuddy.Dto;

import lombok.Data;

@Data
public class TransactionDTO {

    private String connection;
    private double amount;
    private String description;

    public TransactionDTO() {
    }

    public String getConnection() {
        return connection;
    }

    public void setConnection(String connection) {
        this.connection = connection;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
