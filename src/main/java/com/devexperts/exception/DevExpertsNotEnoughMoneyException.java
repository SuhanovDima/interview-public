package com.devexperts.exception;

public class DevExpertsNotEnoughMoneyException extends IllegalStateException {
    public DevExpertsNotEnoughMoneyException(String msg) {
        super(msg);
    }
}
