package com.indbank.sms;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class SmsController {

    private final SmsLogRepository repo;

    public SmsController(SmsLogRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/sms/logs")
    public List<SmsLog> logs() {
        return repo.findAll();
    }
}
