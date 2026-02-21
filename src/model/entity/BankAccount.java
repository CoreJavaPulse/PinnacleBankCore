package model.entity;

import exceptions.DailyLimitExceededException;
import exceptions.InsufficientFundsException;
import exceptions.InvalidAmountException;
import exceptions.MinimumBalanceException;

public interface BankAccount {
    public int getAccNo();
    double getBalance();
    void deposit(double amount) throws InvalidAmountException;
    void withdraw(double amount) throws InvalidAmountException, InsufficientFundsException, DailyLimitExceededException, MinimumBalanceException;
}
