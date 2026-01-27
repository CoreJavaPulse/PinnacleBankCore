# 🏦 PinnacleBankCore  
### A Core Java Console Banking System (OOP-Driven Design)

PinnacleBankCore is a fully menu-driven **console banking application** developed using **Core Java**, designed to demonstrate strong **Object-Oriented Programming (OOP)** principles, **custom exception handling**, and **clean layered architecture**.

This project intentionally avoids frameworks and databases to showcase **core Java fundamentals**, making it ideal for **Java freshers, interview preparation, and OOP practice**.

---

## 🎯 Project Objective

- Apply Core Java concepts in a real-world banking domain  
- Practice abstraction, inheritance, polymorphism, and encapsulation  
- Implement custom checked exceptions for business rules  
- Design a clean, scalable console-based application  

---

## 🧠 High-Level Flow
```
User
 ↓
BankMain (Menu Controller)
 ↓
BankServices (Business Layer)
 ↓
Customer → Account → Transactions
 ↓
Enums + Exceptions enforce rules

```
---

## 🗂️ Project Structure
```
PinnacleBankCore
│
├── BankMain.java
│   └── Entry point
│   └── Displays menu & routes user actions
│
├── service
│   └── BankServices.java
│       ├── Account creation
│       ├── Transactions
│       ├── Search operations
│       ├── Update & delete logic
│       └── Interest calculation
│
├── model
│   ├── entity
│   │   ├── BankAccount.java      → Interface
│   │   ├── Account.java          → Abstract base class
│   │   ├── SavingsAccount.java   → Interest-earning account
│   │   ├── CurrentAccount.java   → Business account
│   │   ├── Customer.java         → Customer profile
│   │   ├── Address.java          → Immutable value object
│   │   └── Transaction.java      → Transaction record
│   │
│   └── enums
│       ├── AccountType.java
│       └── TransactionType.java
│
├── exceptions
│   ├── AccountNotFoundException.java
│   ├── DuplicateAccountException.java
│   ├── InvalidAmountException.java
│   ├── InsufficientFundsException.java
│   ├── MinimumBalanceException.java
│   ├── DailyLimitExceededException.java
│   └── InvalidIFSCException.java
```
---

## ⚙️ Features

### 🧾 Account Management
- Savings and Current account creation  
- Customer profile with address  
- In-memory storage using `ArrayList`

### 💸 Transactions
- Deposit and withdrawal operations  
- Validation for invalid amounts  
- Protection against insufficient balance  
- Automatic transaction history tracking  

### 📄 Account Statement
- View last **N transactions**
- View complete transaction history  
- Timestamped transaction records  

### 🔍 Search
- Search by Customer ID  
- Search by Account Number  
- Search by partial Customer Name  

### ✏️ Update Operations
- Update customer name  
- Update customer address  
- Safe immutable account design  

### 🗑️ Delete Account (Protected)
- Balance must be zero  
- Double confirmation required  
- Prevents accidental deletion  

### 📈 Interest Calculation
- Monthly interest for Savings Accounts  
- No interest for Current Accounts  
- Interest credited as a transaction  

---

## 🧠 OOP Concepts Used

- Abstraction (`Account`, `BankAccount`)
- Inheritance (`SavingsAccount`, `CurrentAccount`)
- Polymorphism (base class references)
- Encapsulation (private fields with validation)
- Composition (Customer → Account → Address → Transactions)
- Enums for domain modeling
- Custom checked exceptions

---

## 🚨 Custom Exceptions

- AccountNotFoundException  
- InvalidAmountException  
- InsufficientFundsException  
- DuplicateAccountException  
- MinimumBalanceException  
- DailyLimitExceededException  
- InvalidIFSCException  

---

## ▶️ How to Run

1. Clone the repository  
2. Open the project in **Eclipse / IntelliJ IDEA**  
3. Run `BankMain.java`  
4. Use the console menu to interact  
---

## 🎓 Target Audience

- Java Freshers  
- Core Java learners  
- Interview preparation  
- OOP practice  

---

## 🚀 Future Enhancements

- Account-to-account transfers  
- Database integration (JDBC)  
- File persistence  
- JUnit test cases  
- Logging framework  

---


## 👨‍💻 Author
**Hitesh Mane**  
Java Backend Developer  
Pune, India
📧 Email: [hiteshmane5hm@gmail.com](mailto:hiteshmane5hm@gmail.com)
💻 GitHub: [https://github.com/CoreJavaPulse](https://github.com/CoreJavaPulse)

---

⭐ This project focuses on **strong fundamentals, clean design, and real-world logic**, not frameworks.
