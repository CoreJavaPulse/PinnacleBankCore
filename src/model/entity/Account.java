package model.entity;

import java.util.ArrayList;
import java.util.List;

import exceptions.DailyLimitExceededException;
import exceptions.InsufficientFundsException;
import exceptions.InvalidAmountException;
import exceptions.InvalidIFSCException;
import exceptions.MinimumBalanceException;
import model.enums.AccountType;
import model.enums.TransactionType;

/**
 * Abstract base class for all bank accounts.
 * Provides common functionality: deposit, withdraw, transactions, interest.
 */
public abstract class Account implements BankAccount {

	// 1. Fields (private, final first)
	private final int accNo;
	private final String ifscCode;
	private double balance;
	private final AccountType accType;
	private final List<Transaction> transactions = new ArrayList<>();

	private double todayDebitTotal = 0.0;
	private java.time.LocalDate lastDebitDate = java.time.LocalDate.now();
	private static final double DAILY_LIMIT = 50000.0;

	// 2. Constructor
	public Account(int accNo, String ifscCode, double balance, AccountType accType)
			throws InvalidIFSCException, MinimumBalanceException{

		if (!ifscCode.toUpperCase().matches("[A-Z]{4}0[A-Z0-9]{6}")) {
		    throw new InvalidIFSCException(ifscCode);
		}

		this.accNo = accNo;
		this.ifscCode = ifscCode.toUpperCase();
		if (balance < util.BankConstants.MIN_BALANCE) {
			throw new MinimumBalanceException(balance);
		}
		this.balance = Math.max(0, balance);
		this.accType = accType;
	}

	// 3. Interface Methods (BankAccount)
	@Override
	public int getAccNo() {
		return accNo;
	}

	@Override
	public double getBalance() {
		return balance;
	}

	@Override
	public void deposit(double amount) throws InvalidAmountException {
	    if (amount <= 0) throw new InvalidAmountException(amount);
	    
	    balance += amount;
	    transactions.add(new Transaction(TransactionType.DEPOSIT, amount, balance, 
	        "Deposit to A/c " + accNo));
	    System.out.println("✅ Deposited ₹" + String.format("%.2f", amount));
	}

	@Override
	public void withdraw(double amount) throws InvalidAmountException, 
	InsufficientFundsException, DailyLimitExceededException, MinimumBalanceException {

		if (amount <= 0) 
			throw new InvalidAmountException(amount);

		if (balance < amount) 
			throw new InsufficientFundsException(amount, balance);

		double newBalance = balance - amount;
		if (newBalance < util.BankConstants.MIN_BALANCE) {
			throw new MinimumBalanceException(newBalance);
		}

		checkDailyLimit(amount);

		// ✅ Apply withdrawal
		balance = newBalance;  // Use calculated value
		todayDebitTotal += amount;
		transactions.add(new Transaction(TransactionType.TRANSFER_OUT, amount, balance,
				"Withdrawal from A/c " + accNo));
		System.out.println("✅ Withdrew ₹" + String.format("%.2f", amount));
	}

	// 4. Getters/Setters (Business fields)
	public String getIfscCode() {
		return ifscCode;
	}

	public AccountType getAccType() {
		return accType;
	}

	// 5. Transaction Management
	public List<Transaction> getTransactions() {
		return new ArrayList<>(transactions); // Defensive copy
	}

	/**
	 * Prints last N transactions in table format
	 */
	public void printStatement(int count) {
		System.out.println("\n=== LAST " + count + " TRANSACTIONS ===");
		System.out.println("Date      | Type      | Amount   | Balance  | Desc");
		System.out.println("------------------------------------------------");

		if (transactions.isEmpty()) {
			System.out.println("No transactions yet.");
			return;
		}

		int start = Math.max(0, transactions.size() - count);
		for (int i = start; i < transactions.size(); i++) {
			System.out.println(transactions.get(i));
		}
	}

	// 6. Interest Management (Abstract method + utility)
	/**
	 * Subclasses must implement interest calculation logic
	 */
	public abstract double calculateInterest();

	/**
	 * Adds calculated interest to balance and records transaction
	 */
	public final void addInterestToBalance() {
		double interest = calculateInterest();
		if (interest > 0) {
			balance += interest;
			transactions.add(new Transaction(TransactionType.DEPOSIT, interest, balance,
					"Interest credited to A/c " + accNo));
			System.out.println("✅ Interest added: ₹" + String.format("%.2f", interest));
		}
	}

	// 7. toString() - Professional format
	@Override
	public String toString() {
		return String.format("AccNo=%d, IFSC=%s, Bal=₹%.2f, Type=%s", 
				accNo, ifscCode, balance, accType);
	}

	private void checkDailyLimit(double amount) throws DailyLimitExceededException {
		java.time.LocalDate today = java.time.LocalDate.now();

		// reset if new day
		if (!today.equals(lastDebitDate)) {
			todayDebitTotal = 0.0;
			lastDebitDate = today;
		}

		if (todayDebitTotal + amount > DAILY_LIMIT) {
			throw new DailyLimitExceededException();
		}
	}
}
