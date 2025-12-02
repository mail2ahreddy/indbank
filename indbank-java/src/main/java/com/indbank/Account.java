package com.indbank;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public Long customerId;
    public String accountNumber;
    public Double balance = 0.0;
    public String currency = "INR";

    public Instant createdAt = Instant.now();
}

