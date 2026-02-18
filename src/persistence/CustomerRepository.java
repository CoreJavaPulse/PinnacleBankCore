package persistence;

import exceptions.*;
import model.entity.Customer;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepository {
    private final List<Customer> customers = new ArrayList<>();

    public void add(Customer customer) throws DuplicateAccountException {
        if (hasCustomerId(customer.getCustId()) || hasAccountNo(customer.getCustAcc().getAccNo())) {
            throw new DuplicateAccountException(customer.getCustAcc().getAccNo());
        }
        customers.add(customer);
    }

    public List<Customer> getAll() { return new ArrayList<>(customers); }

    public Customer findById(int id) throws AccountNotFoundException {
        return customers.stream()
            .filter(c -> c.getCustId() == id)
            .findFirst()
            .orElseThrow(() -> new AccountNotFoundException(id));
    }

    public void update(Customer customer) {
        customers.removeIf(c -> c.getCustId() == customer.getCustId());
        customers.add(customer);
    }

    public boolean delete(int id) throws AccountNotFoundException {
        Customer cust = findById(id);
        if (cust.getCustAcc().getBalance() > 0) return false;
        return customers.remove(cust);
    }

    private boolean hasCustomerId(int id) {
        return customers.stream().anyMatch(c -> c.getCustId() == id);
    }

    private boolean hasAccountNo(int accNo) {
        return customers.stream().anyMatch(c -> c.getCustAcc().getAccNo() == accNo);
    }
}
