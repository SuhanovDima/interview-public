package com.devexperts.account;

import com.devexperts.exception.DevExpertsNotEnoughMoneyException;

public class Account {
    private final AccountKey accountKey;
    private final String firstName;
    private final String lastName;
    private Double balance;

    public Account(AccountKey accountKey, String firstName, String lastName, Double balance) {
        this.accountKey = accountKey;
        this.firstName = firstName;
        this.lastName = lastName;
        this.balance = balance;
    }

    public AccountKey getAccountKey() {
        return accountKey;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Double getBalance() {
        return balance;
    }

    public void increaseBalance(Double delta) {
        balance += delta;
    }

    public void reduceBalance(Double delta) {
        checkBalance(delta);
        balance -= delta;
    }

    public void checkBalance(Double amount) {
        if (balance < amount) {
            throw new DevExpertsNotEnoughMoneyException("Balance of source is not enough full");
        }
    }
}
