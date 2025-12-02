package com.indbank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bank")
public class BankController {

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private TransactionRepo txRepo;

    @Autowired
    private KafkaProducerService kafkaProducer;

    @Autowired
    private Environment env;

    private String topicName() {
        // read spring.kafka.topic.name from application.yml
        return env.getProperty("spring.kafka.topic.name", "transactions");
    }

    // -------------------------
    // Create Customer
    // -------------------------
    @PostMapping("/customers")
    public Customer createCustomer(@RequestBody Customer c) {
        return customerRepo.save(c);
    }

    // -------------------------
    // Create Account
    // -------------------------
    @PostMapping("/accounts")
    public Account createAccount(@RequestBody Account a) {
        a.balance = 0.0;
        a.accountNumber = "ACCT-" + System.currentTimeMillis();
        return accountRepo.save(a);
    }

    // -------------------------
    // Deposit
    // -------------------------
    @PostMapping("/deposit/{id}")
    public String deposit(@PathVariable Long id, @RequestBody Transaction t) {
        Optional<Account> accOpt = accountRepo.findById(id);
        if (accOpt.isEmpty()) {
            return "Account not found";
        }

        Account account = accOpt.get();
        account.balance += t.amount;
        accountRepo.save(account);

        t.accountId = id;
        t.type = "DEPOSIT";
        txRepo.save(t);

        // Send Kafka event
	kafkaProducer.publishTransactionEvent(topicName(), id, "DEPOSIT", t.amount);
        return "Deposit successful";
    }

    // -------------------------
    // Withdraw
    // -------------------------
    @PostMapping("/withdraw/{id}")
    public String withdraw(@PathVariable Long id, @RequestBody Transaction t) {
        Optional<Account> accOpt = accountRepo.findById(id);
        if (accOpt.isEmpty()) {
            return "Account not found";
        }

        Account account = accOpt.get();
        if (account.balance < t.amount) {
            return "Insufficient funds";
        }

        account.balance -= t.amount;
        accountRepo.save(account);

        t.accountId = id;
        t.type = "WITHDRAW";
        txRepo.save(t);

        // Send Kafka event
	kafkaProducer.publishTransactionEvent(topicName(), id, "WITHDRAW", t.amount);
        return "Withdraw successful";
    }

    // -------------------------
    // Get single account
    // -------------------------
    @GetMapping("/accounts/{id}")
    public Account getAccount(@PathVariable Long id) {
        return accountRepo.findById(id).orElse(null);
    }

    // -------------------------
    // Get all customers
    // -------------------------
    @GetMapping("/customers")
    public List<Customer> getAllCustomers() {
        return customerRepo.findAll();
    }

    // -------------------------
    // Balance
    // -------------------------
    @GetMapping("/balance/{id}")
    public double getBalance(@PathVariable Long id) {
        return accountRepo.findById(id)
                .map(a -> a.balance)
                .orElse(0.0);
    }

    // -------------------------
    // Statement (all transactions)
    // -------------------------
    @GetMapping("/statement/{id}")
    public List<Transaction> getStatement(@PathVariable Long id) {
        return txRepo.findByAccountId(id);
    }
}
