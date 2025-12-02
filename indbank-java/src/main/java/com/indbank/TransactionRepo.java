package com.indbank;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionRepo extends JpaRepository<Transaction, Long> {

    // return all transactions of a given account
    List<Transaction> findByAccountId(Long accountId);
}
