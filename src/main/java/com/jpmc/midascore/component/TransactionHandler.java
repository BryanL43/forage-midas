package com.jpmc.midascore.component;

import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionHandler {
    private final DatabaseConduit databaseConduit;
    private final IncentiveAPI incentiveAPI;

    public TransactionHandler(DatabaseConduit databaseConduit, IncentiveAPI incentiveAPI) {
        this.databaseConduit = databaseConduit;
        this.incentiveAPI = incentiveAPI;
    }

    public void handleTransaction(Transaction transaction) {
        if (databaseConduit.isValid(transaction)) {
            Incentive incentive = incentiveAPI.query(transaction);
            transaction.setIncentive(incentive.getAmount());
            databaseConduit.save(transaction);
        }
    }
}
