package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import com.devexperts.exception.DevExpertsArgumentException;
import com.devexperts.exception.DevExpertsNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class AccountServiceImpl implements AccountService {

    private final ConcurrentMap<AccountKey, Account> accounts = new ConcurrentHashMap<>();

    @Override
    public void clear() {
        accounts.clear();
    }

    @Override
    public void createAccount(Account account) {
        validateAccount(account);
        accounts.put(account.getAccountKey(), account);
    }

    @Override
    public Account getAccount(long id) {
        return accounts.get(AccountKey.valueOf(id));
    }

    @Override
    public void transfer(Account source, Account target, double amount) {
        validateBeforeTransfer(source, target, amount);
        Object lock1 = source.getAccountKey().hashCode() < target.getAccountKey().hashCode() ? source : target;
        Object lock2 = source.getAccountKey().hashCode() < target.getAccountKey().hashCode() ? target : source;
        synchronized (lock1) {
            synchronized (lock2) {
                source.reduceBalance(amount);
                target.increaseBalance(amount);
            }
        }
    }

    private void validateAccount(Account account) {
        if (account == null) {
            throw new DevExpertsArgumentException("account can't be null");
        }
        if (StringUtils.isEmpty(account.getFirstName())) {
            throw new DevExpertsArgumentException("First name by account can't be empry");
        }
        if (StringUtils.isEmpty(account.getLastName())) {
            throw new DevExpertsArgumentException("Last name by account can't be empry");
        }
        if (account.getBalance() < 0) {
            throw new DevExpertsArgumentException("Balance can't be less 0");
        }
    }

    private void validateBeforeTransfer(Account source, Account target, double amount) {
        if (source == null || target == null) {
            throw new DevExpertsNotFoundException("Source or target can't be null");
        }
        if (source == target || source.getAccountKey().equals(target.getAccountKey())) {
            throw new DevExpertsArgumentException("Source and target can't be the same");
        }
        if (amount <= 0) {
            throw new DevExpertsArgumentException("Amount can not be less then 0");
        }
    }
}
