package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class DatabaseConduit {
    private final UserRepository userRepository;
    private final TransactionRecordRepository transactionRecordRepository;

    public DatabaseConduit(UserRepository userRepository, TransactionRecordRepository transactionRecordRepository) {
        this.userRepository = userRepository;
        this.transactionRecordRepository = transactionRecordRepository;
    }

    public UserRecord queryUser(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public boolean isValid(Transaction transaction) {
        UserRecord sender = queryUser(transaction.getSenderId());
        if (sender == null) {
            return false;
        }

        UserRecord recipient = queryUser(transaction.getRecipientId());
        if (recipient == null) {
            return false;
        }

        return !(sender.getBalance() < transaction.getAmount());
    }

    public void save(UserRecord userRecord) {
        userRepository.save(userRecord);
    }

    public void save(Transaction transaction) {
        // Record transaction
        UserRecord sender = queryUser(transaction.getSenderId());
        UserRecord recipient = queryUser(transaction.getRecipientId());
        TransactionRecord transactionRecord = new TransactionRecord(
            sender,
            recipient,
            transaction.getAmount(),
            transaction.getIncentive()
        );
        transactionRecordRepository.save(transactionRecord);

        // Update user balances
        sender.setBalance(sender.getBalance() - transaction.getAmount());
        save(sender);
        recipient.setBalance(recipient.getBalance() + transaction.getAmount() + transaction.getIncentive());
        save(recipient);
    }

    public float queryUserBalance(Long userId) {
        UserRecord userRecord = queryUser(userId);
        if (userRecord == null) {
            return 0.0f;
        }

        return userRecord.getBalance();
    }
}
