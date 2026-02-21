package service;

import java.util.ArrayList;
import java.util.List;
import exceptions.*;
import model.entity.*;
import ui.InputHandler;
import util.BankConstants;

public class BankServices {
	private final List<Customer> custlist = new ArrayList<>();

	// CORE BUSINESS METHODS ONLY
	public void addAccount(InputHandler input) throws Exception {
		Customer customer = input.createCustomer();
		if (hasDuplicate(customer)) {
			throw new DuplicateAccountException(customer.getCustAcc().getAccNo());
		}
		custlist.add(customer);
		System.out.println("✅ Account created: " + customer.getCustName());
	}

	public void deleteCustomer(int custId) throws AccountNotFoundException {
		Customer cust = findById(custId);
		custlist.remove(cust);
	}

	public void displayAllAccounts() {
		if (custlist.isEmpty()) {
			System.out.println("No accounts found!");
			return;
		}
		System.out.println("\n=== ALL ACCOUNTS ===");
		System.out.println("ID | Name | Account | City");
		System.out.println("------------------------------------------");
		custlist.forEach(c -> 
		System.out.printf("%-2d | %-12s | %-30s | %s%n", 
				c.getCustId(), c.getCustName(), c.getCustAcc(), c.getCustAddr().getCity()));
	}

	public Customer findById(int id) throws AccountNotFoundException {
		return custlist.stream()
				.filter(c -> c.getCustId() == id)
				.findFirst()
				.orElseThrow(() -> new AccountNotFoundException(id));
	}

	public void deposit(int custId, double amount) throws Exception {
		Customer cust = findById(custId);
		cust.getCustAcc().deposit(amount);
	}

	public void withdraw(int custId, double amount) throws Exception {
		Customer cust = findById(custId);
		cust.getCustAcc().withdraw(amount);
	}

	public void transfer(int fromCustId, int toCustId, double amount) throws Exception {
		if (fromCustId == toCustId) {
			throw new IllegalArgumentException("Cannot transfer to same account");
		}

		Customer fromCust = findById(fromCustId);
		Customer toCust = findById(toCustId);

		BankAccount fromAcc = fromCust.getCustAcc();
		BankAccount toAcc = toCust.getCustAcc();

		System.out.println("\n💰 TRANSFERRING ₹" + String.format("%.2f", amount));
		System.out.println("From: " + fromCust.getCustName() + " (Acc: " + fromAcc.getAccNo() + ")");
		System.out.println("To:   " + toCust.getCustName() + " (Acc: " + toAcc.getAccNo() + ")");

		boolean withdrawn = false;
		try {
			fromAcc.withdraw(amount);
			withdrawn = true;
			toAcc.deposit(amount);
			System.out.println("✅ Transfer successful!");
		} catch (Exception e) {
			if (withdrawn) {
				try {
					fromAcc.deposit(amount);
					System.out.println("↩️ Transfer rolled back.");
				} catch (Exception ex) {
					System.out.println("⚠️ CRITICAL: Rollback failed!");
				}
			}
			throw e;
		}
	}
	public void addInterestToAllAccounts() {
		if (custlist.isEmpty()) return;
		System.out.println("\n=== ADDING INTEREST ===");
		custlist.forEach(c -> ((Account) c.getCustAcc()).addInterestToBalance());
	}

	public void printStatement(int custId, int count) throws AccountNotFoundException {
		Customer cust = findById(custId);
		((Account) cust.getCustAcc()).printStatement(Math.min(count, BankConstants.MAX_STATEMENT));
	}

	public List<Customer> getAllCustomers() {
		return new ArrayList<>(custlist);
	}

	private boolean hasDuplicate(Customer customer) {
		return custlist.stream().anyMatch(c -> 
		c.getCustId() == customer.getCustId() || 
		c.getCustAcc().getAccNo() == customer.getCustAcc().getAccNo());
	}
	
	public void showDashboard() {
	    if (custlist.isEmpty()) {
	        System.out.println("🏦 BANK DASHBOARD - No accounts yet!");
	        return;
	    }
	    
	    double totalBalance = custlist.stream()
	        .mapToDouble(c -> c.getCustAcc().getBalance())
	        .sum();
	        
	    double avgBalance = totalBalance / custlist.size();
	    long highBalanceAccounts = custlist.stream()
	        .filter(c -> c.getCustAcc().getBalance() > 5000)
	        .count();
	    
	    System.out.println("\n🏦 ═══════════════════════════════════════════════");
	    System.out.println("                    BANK DASHBOARD");
	    System.out.println("   ═══════════════════════════════════════════════");
	    System.out.printf("   📊 Customers      : %d%n", custlist.size());
	    System.out.printf("   💰 Total Balance  : ₹%,.2f%n", totalBalance);
	    System.out.printf("   📈 Avg Balance    : ₹%,.2f%n", avgBalance);
	    System.out.printf("   ⚠️  High Balance  : %d (>%s)%n", 
	        highBalanceAccounts, "₹5000");
	    
	    // Top customer
	    Customer top = custlist.stream()
	        .max((c1, c2) -> Double.compare(
	            c1.getCustAcc().getBalance(), 
	            c2.getCustAcc().getBalance()))
	        .orElse(null);
	    if (top != null) {
	        System.out.printf("   👑 Top Customer   : %s (₹%,.2f)%n", 
	            top.getCustName(), top.getCustAcc().getBalance());
	    }
	    
	    System.out.println("   ═══════════════════════════════════════════════");
	    System.out.println("Press Enter to continue...");
	    try { System.in.read(); } catch (Exception e) {}
	}

}
