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
}
