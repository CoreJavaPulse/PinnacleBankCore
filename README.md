# 🏦 PinnacleBankCore

[![Core Java](https://img.shields.io/badge/Core%20Java-17%2B-brightgreen?style=flat-square\&logo=java\&logoColor=white)](https://www.oracle.com/java/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg?style=flat-square)](https://opensource.org/licenses/MIT)

### 🚀 Production-Ready **Core Java Console Banking System** (OOP-Driven Design)

**PinnacleBankCore** is a fully menu-driven **console banking application** developed using **pure Core Java**, demonstrating **enterprise-level OOP principles**, **custom exception handling**, and **clean layered architecture**.

> ⚡ **No frameworks. No databases. Pure Core Java fundamentals** — Perfect for **Java interviews**, **OOP mastery**, and **fresher portfolios**.

---

## 🎯 Project Objective

Build a **scalable banking system** showcasing:

* ✅ SOLID principles in action
* ✅ 7+ custom business exceptions
* ✅ Real-world banking logic (interest, min balance, daily limits)
* ✅ Professional console UI with formatted tables

---

## 📱 Live Demo

```text
--------------------- Main Menu ---------------------
1:Add Account 2:Display All 3:Search Account
4:Transaction 5:Update 6:Delete Account
7:Add Interest 8:Account Statement

Choose (1-8): 1
Customer ID: 101
Customer Name: Hitesh Mane
Account No: 123456
IFSC Code: SBIN0001234
Initial Balance (₹): 5000
Type (SAVINGS/CURRENT): SAVINGS
Interest Rate (%): 6.5

✅ Account created: Hitesh Mane
```

---

## 🧠 Core Java Concepts Mastered

| Concept                | Implementation                               | Business Value     |
| ---------------------- | -------------------------------------------- | ------------------ |
| **Inheritance**        | `Account → SavingsAccount/CurrentAccount`    | Account hierarchy  |
| **Polymorphism**       | `addInterestToAllAccounts()`                 | Uniform processing |
| **Abstraction**        | `BankAccount` interface + `Account` abstract | Clean contracts    |
| **Encapsulation**      | Private fields + validation                  | Data integrity     |
| **Collections**        | `ArrayList<Customer>`, `List<Transaction>`   | Efficient storage  |
| **Exception Handling** | 7+ custom checked exceptions                 | Rule enforcement   |
| **Enums**              | `AccountType`, `TransactionType`             | Type safety        |
| **Streams/Lambdas**    | Duplicate checks, searches                   | Modern Java        |

---

## ⚙️ Production Features

| Feature          | Savings Account       | Current Account       | Status      |
| ---------------- | --------------------- | --------------------- | ----------- |
| Create Account   | ✅ ₹1000 min balance   | ✅ Business accounts   | 🟢 Complete |
| Deposit/Withdraw | ✅ Transaction history | ✅ Transaction history | 🟢 Complete |
| Interest         | ✅ 6.5% monthly        | ❌ Zero interest       | 🟢 Complete |
| Search           | ID/AccNo/Name         | ID/AccNo/Name         | 🟢 Complete |
| Statement        | Last N transactions   | Last N transactions   | 🟢 Complete |
| Update           | Name + Address        | Name + Address        | 🟢 Complete |
| Delete           | Balance=0 required    | Balance=0 required    | 🟢 Complete |

---

## 🗂️ Enterprise Architecture

```text
┌─────────────────┐      ┌──────────────────┐      ┌─────────────────┐
│    BankMain     │ ───▶ │   BankServices   │ ───▶ │     Customer    │
│   (Controller)  │      │ (Business Logic) │      │        ↕        │
└─────────────────┘      └──────────────────┘      │     Account     │
                                                   │        ↕        │
                                                   │   Transactions  │
                                                   │     Address     │
                                                   └─────────────────┘
```

---

## 📁 Project Structure

```text
PinnacleBankCore/
├── BankMain.java
├── service/
│   └── BankServices.java
├── model/
│   ├── entity/
│   │   ├── BankAccount.java
│   │   ├── Account.java
│   │   ├── SavingsAccount.java
│   │   ├── CurrentAccount.java
│   │   ├── Customer.java
│   │   ├── Transaction.java
│   │   └── Address.java
│   └── enums/
│       ├── AccountType.java
│       └── TransactionType.java
├── exceptions/
│   ├── AccountNotFoundException.java
│   ├── DuplicateAccountException.java
│   ├── InsufficientFundsException.java
│   ├── InvalidAmountException.java
│   ├── MinimumBalanceException.java
│   ├── DailyLimitExceededException.java
│   └── InvalidIFSCException.java
└── util/
    └── BankConstants.java
```

---

## 🚨 Business Exceptions Enforced

```text
❌ AccountNotFoundException(101)
❌ DuplicateAccountException(123456)
❌ InsufficientFundsException(5000.00/2000.00)
❌ InvalidAmountException(-100.00)
❌ MinimumBalanceException(500.00)
❌ DailyLimitExceededException()
❌ InvalidIFSCException(INVALID123)
```

---

## ▶️ Quick Start

```bash
1. Clone the repository  
2. Open the project in **Eclipse / IntelliJ IDEA**  
3. Run `BankMain.java`  
4. Use the console menu to interact
```

### 🔧 Prerequisites

* JDK 17+
* Any Java IDE

---

## 🎓 Perfect For

* ✅ Java Fresher Interviews
* ✅ OOP Concept Demonstration
* ✅ Core Java Portfolio
* ✅ Technical Round Preparation
* ✅ College Projects

---

## 🚀 Production Enhancements Planned

* 📁 File Persistence (ObjectInputStream)
* 🔁 Account Transfers
* 🗄️ JDBC Integration (MySQL/PostgreSQL)
* 🧪 JUnit 5 Tests (95% coverage target)
* ⚡ Multithreading (concurrent transactions)

---

## 👨‍💻 Author

**Hitesh Mane**
Java Backend Developer | Pune, India

📧 [hiteshmane5hm@gmail.com](mailto:hiteshmane5hm@gmail.com)

---

⭐ **If you like this project, don't forget to star the repo!**
