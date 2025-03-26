package com.example.authentication.Objects;

public class User {
    private Account acc;

    public User() {}

    public void setAcc(Account acc) {
        this.acc = acc;
    }
    public Account getAcc() {
        return acc;
    }
}