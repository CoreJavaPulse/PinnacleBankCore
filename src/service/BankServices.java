package service;

import java.util.*;
import java.util.stream.Collectors;
import exceptions.*;
import model.entity.*;
import ui.InputHandler;
import util.BankConstants;

public class BankServices {
    private final List<Customer> custlist = new ArrayList<>();
    private final Map<Integer, Customer> custIdCache = new HashMap<>();
    private final Map<Integer, Customer> accNoCache = new HashMap<>();

    public void addAccount(InputHandler input) throws Exception {
        Customer customer = input.createCustomer();
        if (hasDuplicate(customer)) {
            throw new DuplicateAccountException(customer.getCustAcc().getAccNo());
        }
        custlist.add(customer);
        updateCaches(customer);
        System.out.println("✅ Account created: " + customer.getCustName());
    }

    public void deleteCustomer(int custId) throws AccountNotFoundException {
        Customer cust = findById(custId);
        custlist.remove(cust);
        custIdCache.remove(custId);
        accNoCache.remove(cust.getCustAcc().getAccNo());
    }

    public void displayAllAccounts() {
        if (custlist.isEmpty()) {
            System.out.println("No accounts found!");
            return;
        }
        System.out.println("\n=== ALL ACCOUNTS ===");
        System.out.println("ID | Name          | Account              | City");
        System.out.println("-----------------------------------------------");
        
        List<Customer> displayList = custlist.stream()
            .limit(BankConstants.MAX_DISPLAY_ACCOUNTS)
            .collect(Collectors.toList());
            
        displayList.forEach(c -> 
            System.out.printf("%-2d | %-12s | %-22s | %s%n", 
                c.getCustId(), c.getCustName(), c.getCustAcc(), c.getCustAddr().getCity()));
                
        if (custlist.size() > BankConstants.MAX_DISPLAY_ACCOUNTS) {
            System.out.println("... and " + (custlist.size() - BankConstants.MAX_DISPLAY_ACCOUNTS) + " more");
        }
    }

    public Customer findById(int id) throws AccountNotFoundException {
        Customer cached = custIdCache.get(id);
        if (cached != null) {
            return cached;
        }
        
        Customer customer = custlist.stream()
            .filter(c -> c.getCustId() == id)
            .findFirst()
            .orElseThrow(() -> new AccountNotFoundException(id));
        
        custIdCache.put(id, customer);
        return customer;
    }

    public Customer findByAccNo(int accNo) throws AccountNotFoundException {
        Customer cached = accNoCache.get(accNo);
        if (cached != null) {
            return cached;
        }
        
        Customer customer = custlist.stream()
            .filter(c -> c.getCustAcc().getAccNo() == accNo)
            .findFirst()
            .orElseThrow(() -> new AccountNotFoundException(accNo));
        
        accNoCache.put(accNo, customer);
        return customer;
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
        custlist.forEach(c -> {
            try {
                ((Account) c.getCustAcc()).addInterestToBalance();
            } catch (Exception e) {
                System.out.println("⚠️ Interest failed for " + c.getCustName() + ": " + e.getMessage());
            }
        });
    }

    public void printStatement(int custId, int count) throws AccountNotFoundException {
        Customer cust = findById(custId);
        ((Account) cust.getCustAcc()).printStatement(Math.min(count, BankConstants.MAX_STATEMENT));
    }

    public List<Customer> getAllCustomers() {
        return new ArrayList<>(custlist);
    }

    private boolean hasDuplicate(Customer customer) {
        return custIdCache.containsKey(customer.getCustId()) ||
               accNoCache.containsKey(customer.getCustAcc().getAccNo());
    }

    private void updateCaches(Customer customer) {
        custIdCache.put(customer.getCustId(), customer);
        accNoCache.put(customer.getCustAcc().getAccNo(), customer);
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
            .filter(c -> c.getCustAcc().getBalance() > BankConstants.HIGH_BALANCE_THRESHOLD)
            .count();
        
        System.out.println("\n🏦 ═══════════════════════════════════════════════");
        System.out.println("                    BANK DASHBOARD");
        System.out.println("   ═══════════════════════════════════════════════");
        System.out.printf("   📊 Customers      : %d%n", custlist.size());
        System.out.printf("   💰 Total Balance  : ₹%,.2f%n", totalBalance);
        System.out.printf("   📈 Avg Balance    : ₹%,.2f%n", avgBalance);
        System.out.printf("   ⚠️  High Balance  : %d (>%s)%n", 
            highBalanceAccounts, "₹5,000");
        
        // Top customer
        Customer top = custlist.stream()
            .max(Comparator.comparingDouble(c -> c.getCustAcc().getBalance()))
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
