package ui;

import java.util.Scanner;
import exceptions.*;
import model.entity.*;
import model.enums.AccountType;
import service.BankServices;
import util.BankConstants;

public class InputHandler {
	private final Scanner sc;

	public InputHandler(Scanner sc) {
		this.sc = sc;
	}

	public int getInt(String prompt, int min, int max) {
		while (true) {
			System.out.print(prompt);
			try {
				String line = sc.nextLine().trim();
				int val = Integer.parseInt(line);
				if (val >= min && val <= max) return val;
				System.out.println("Must be " + min + "-" + max);
			} catch (NumberFormatException e) {
				System.out.println("Enter valid number!");
			}
		}
	}

	public double getDouble(String prompt, double min) {
		while (true) {
			System.out.print(prompt);
			try {
				String line = sc.nextLine().trim();
				double val = Double.parseDouble(line);
				if (val >= min) return val;
				System.out.println("Must be >= " + min);
			} catch (NumberFormatException e) {
				System.out.println("Enter valid amount!");
			}
		}
	}

	public String getString(String prompt) {
		System.out.print(prompt);
		return sc.nextLine().trim();
	}

	public Customer createCustomer() throws Exception {
		int custId = getInt("Customer ID: ", 1, 999999);
		String name = getString("Customer Name: ");
		int accNo = getInt("Account No: ", 100000, 999999);
		String ifsc = getString("IFSC Code: ");
		double balance = getDouble("Initial Balance (₹): ", util.BankConstants.MIN_BALANCE);
		String type = getString("Type (SAVINGS/CURRENT): ").toUpperCase();

		BankAccount acc = switch (type) {
		case "SAVINGS" -> {
			double rate = getDouble("Interest Rate (%): ", 0.0);
			yield new SavingsAccount(accNo, ifsc, balance, AccountType.SAVINGS, rate);
		}
		case "CURRENT" -> {
			String comp = getString("Company Name: ");
			yield new CurrentAccount(accNo, ifsc, balance, AccountType.CURRENT, comp);
		}
		default -> throw new IllegalArgumentException("Invalid type: " + type);
		};

		Address addr = getAddress();
		return new Customer(custId, name, acc, addr);
	}

	public void search(BankServices bank) {
		if (bank.getAllCustomers().isEmpty()) {
			System.out.println("No accounts found!");
			return;
		}

		String cont;
		do {
			System.out.println("\n🔍 SEARCH BY:");
			System.out.println("1. Customer ID  2. Account Number  3. Name");
			int choice = getInt("Choose (1-3): ", 1, 3);

			switch (choice) {
			case 1 -> searchByCustomerId(bank);
			case 2 -> searchByAccountNo(bank);
			case 3 -> searchByName(bank);
			}
			cont = getString("Continue search? (yes/no): ");
		} while ("yes".equalsIgnoreCase(cont));
	}

	private void searchByCustomerId(BankServices bank) {
		try {
			int id = getInt("Enter Customer ID: ", 1, 999999);
			Customer cust = bank.findById(id);
			displayCustomerDetails(cust);
		} catch (AccountNotFoundException e) {
			System.out.println("❌ " + e.getMessage());
		}
	}

	private void searchByAccountNo(BankServices bank) {
		int accNo = getInt("Enter Account Number: ", 100000, 999999);
		boolean found = false;

		for (Customer cust : bank.getAllCustomers()) {
			if (cust.getCustAcc().getAccNo() == accNo) {
				displayCustomerDetails(cust);
				found = true;
				break;
			}
		}
		if (!found) System.out.println("❌ Account not found!");
	}

	private void searchByName(BankServices bank) {
		String name = getString("Enter name (partial OK): ").toLowerCase();
		boolean foundAny = false;

		for (Customer cust : bank.getAllCustomers()) {
			if (cust.getCustName().toLowerCase().contains(name)) {
				displayCustomerDetails(cust);
				foundAny = true;
			}
		}
		if (!foundAny) System.out.println("❌ No customers found with name: '" + name + "'");
	}

	public void transaction(BankServices bank) {
		if (bank.getAllCustomers().isEmpty()) {
			System.out.println("No customers!");
			return;
		}

		String cont;
		do {
			try {
				System.out.println("\n 💳 TRANSACTION OPTIONS:");
				System.out.println("1:Deposit 2:Withdraw 3:Transfer");
				int choice = getInt("Choose (1-3): ",1,3); 

				switch (choice) {
				case 1 -> {
					int id = getInt("Enter Customer ID: ", 1, 999999);
					Customer cust = bank.findById(id);
					System.out.println("\nAccount: " + cust.getCustAcc());
					double amount = getDouble("Deposit amount (₹): ", 0.01);
					bank.deposit(id, amount);
					System.out.println("✅ Deposit successful");
				}
				case 2 -> {
					int id = getInt("Enter Customer ID: ", 1, 999999);
					Customer cust = bank.findById(id);
					System.out.println("\nAccount: " + cust.getCustAcc());
					double amount = getDouble("Withdraw amount (₹): ", 0.01);
					bank.withdraw(id, amount);
					System.out.println("✅ Withdrawal successful");
				}
				case 3 ->{
						transferBetweenAccount(bank);
				}
				}
			} catch (Exception e) {
				System.out.println("❌ " + e.getMessage());
			}
			cont = getString("Continue transaction? (yes/no): ");
		} while ("yes".equalsIgnoreCase(cont));
	}

	private void transferBetweenAccount(BankServices bank) {
		try {
			int fromId = getInt("From Customer Id: ",1,999999);
			int toId = getInt("To Customer Id: ",1,999999);
			double amount = getDouble("Transfer amount (₹): ", 0.01);
			Customer from = bank.findById(fromId);
			Customer to = bank.findById(toId);
			System.out.println("From Balance: ₹" + String.format("%.2f", from.getCustAcc().getBalance()));
			System.out.println("To Balance: ₹" + String.format("%.2f", to.getCustAcc().getBalance()));
			bank.transfer(fromId, toId, amount);
		}
		catch(Exception e) {
		System.out.println("❌ Transfer failed: " + e.getMessage());
		}
	}

	public void update(BankServices bank) {
		if (bank.getAllCustomers().isEmpty()) {
			System.out.println("No accounts!");
			return;
		}

		try {
			int id = getInt("Enter Customer ID: ", 1, 999999);
			Customer cust = bank.findById(id);
			displayCustomerDetails(cust);

			System.out.println("🔧 UPDATE OPTIONS:");
			System.out.println("1. Name only  2. Address only  3. Both");
			int choice = getInt("Choose (1-3): ", 1, 3);

			switch (choice) {
			case 1 -> {
				String newName = getString("Enter new name: ");
				cust.setCustName(newName);
			}
			case 2 -> cust.setCustAddr(getAddress());
			case 3 -> {
				String newName = getString("Enter new name: ");
				cust.setCustName(newName);
				cust.setCustAddr(getAddress());
			}
			}
			System.out.println("\n✅ UPDATE SUCCESSFUL!");
			displayCustomerDetails(cust);
		} catch (Exception e) {
			System.out.println("❌ " + e.getMessage());
		}
	}

	public void delete(BankServices bank) {
		if (bank.getAllCustomers().isEmpty()) {
			System.out.println("No accounts!");
			return;
		}

		try {
			int id = getInt("Enter Customer ID to DELETE: ", 1, 999999);
			Customer cust = bank.findById(id);
			displayCustomerDetails(cust);

			if (cust.getCustAcc().getBalance() > 0) {
				System.out.println("⚠️ Cannot delete - Balance ₹" + cust.getCustAcc().getBalance());
				return;
			}

			String confirm = getString("⚠️ PERMANENT DELETE? Type 'DELETE " + id + "': ");
			if (!confirm.equals("DELETE " + id)) {
				System.out.println("✅ Delete cancelled");
				return;
			}

			bank.deleteCustomer(id);
			System.out.println("🗑️ Account DELETED: " + cust.getCustName());
		} catch (AccountNotFoundException e) {
			System.out.println("❌ " + e.getMessage());
		}
	}

	public void statement(BankServices bank) {
		try {
			int id = getInt("Enter Customer ID: ", 1, 999999);
			String countStr = getString("Last how many? (10/all): ");
			int count = countStr.equalsIgnoreCase("all") ? BankConstants.MAX_STATEMENT : Integer.parseInt(countStr);
			bank.printStatement(id, count);
		} catch (Exception e) {
			System.out.println("❌ " + e.getMessage());
		}
	}

	// Helper methods
	private void displayCustomerDetails(Customer cust) {
		System.out.println("\n✅ ═══════════════════════════════════════════════");
		System.out.println("         CUSTOMER DETAILS");
		System.out.println("   ═══════════════════════════════════════════════");
		System.out.printf("   ID     : %d%n", cust.getCustId());
		System.out.printf("   Name   : %s%n", cust.getCustName());
		System.out.printf("   Account: %s%n", cust.getCustAcc());
		System.out.printf("   Address: %s%n", cust.getCustAddr());
		System.out.println("   ═══════════════════════════════════════════════");
	}

	private Address getAddress() {
		String city = getString("City: ");
		String state = getString("State: ");
		int pin = getInt("Pincode: ", 100000, 999999);
		return new Address(city, state, pin);
	}
}

