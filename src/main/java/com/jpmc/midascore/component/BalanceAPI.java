package com.jpmc.midascore.component;

import com.jpmc.midascore.foundation.Balance;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BalanceAPI {
    private final DatabaseConduit databaseConduit;

    public BalanceAPI(DatabaseConduit databaseConduit) {
        this.databaseConduit = databaseConduit;
    }

    @GetMapping(value = "/balance")
    public Balance query(@RequestParam("userId") Long userId) {
        float balance = databaseConduit.queryUserBalance(userId);
        return new Balance(balance);
    }
}
