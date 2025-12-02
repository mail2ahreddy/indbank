package com.indbank.sms;

import org.springframework.stereotype.Service;

@Service
public class SmsService {

    private final SmsLogRepository repo;

    public SmsService(SmsLogRepository repo) {
        this.repo = repo;
    }

    public void save(String account, String type, double amount) {
        SmsLog log = new SmsLog();
        log.setAccount(account);
        log.setType(type);
        log.setAmount(amount);
        log.setMessage(type + " of " + amount + " for account " + account);
        log.setTimestamp(System.currentTimeMillis());
        repo.save(log);
    }
}
