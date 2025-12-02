package com.indbank;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public Long accountId;
    public String type;
    public Double amount;
    public Instant createdAt = Instant.now();
    public String description;
}

