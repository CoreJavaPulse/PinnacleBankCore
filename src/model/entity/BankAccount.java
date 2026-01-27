package model.entity;

import exceptions.InsufficientFundsException;
import exceptions.InvalidAmountException;

public interface BankAccount {
    public int getAccNo();
    double getBalance();
    void deposit(double amount) throws InvalidAmountException;
    void withdraw(double amount) throws InvalidAmountException, InsufficientFundsException;
}
