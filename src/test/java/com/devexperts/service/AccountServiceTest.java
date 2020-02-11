package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import com.devexperts.exception.DevExpertsArgumentException;
import com.devexperts.exception.DevExpertsNotFoundException;
import com.devexperts.exception.DevExpertsNotEnoughMoneyException;
import org.junit.Assert;
import org.junit.Test;

public class AccountServiceTest {

    private final long NON_EXISTENT_ACCOUNT_ID = Long.MAX_VALUE;

    @Test
    public void clearAccount() {
        AccountService accountService = new AccountServiceImpl();
        accountService.createAccount(new Account(AccountKey.valueOf(1), "Test", "Test2", 3d));
        accountService.clear();
        Account account2 = accountService.getAccount(1);
        Assert.assertNull(account2);
    }

    @Test
    public void getEmptyAccount() {
        AccountService accountService = new AccountServiceImpl();
        Account account = accountService.getAccount(NON_EXISTENT_ACCOUNT_ID);
        Assert.assertNull(account);
    }

    @Test
    public void getAccount() {
        AccountService accountService = new AccountServiceImpl();
        accountService.createAccount(new Account(AccountKey.valueOf(1), "Test", "Test2", 3d));
        Account account = accountService.getAccount(1);

        Assert.assertNotNull(account);
        Assert.assertEquals(account.getFirstName(), "Test");
        Assert.assertEquals(account.getLastName(), "Test2");
        Assert.assertEquals(account.getBalance(), 3, 0);
    }

    @Test
    public void transfer() {
        AccountService accountService = new AccountServiceImpl();
        Account account = new Account(AccountKey.valueOf(1), "Test", "Test2", 10d);
        Account account2 = new Account(AccountKey.valueOf(2), "Test3", "Test4", 3d);
        accountService.transfer(account, account2, 4);
        Assert.assertEquals(account.getBalance(), 6, 0);
        Assert.assertEquals(account2.getBalance(), 7, 0);
    }

    @Test(expected = DevExpertsNotEnoughMoneyException.class)
    public void transferBalanceNotEnough() {
        AccountService accountService = new AccountServiceImpl();
        Account account = new Account(AccountKey.valueOf(1), "Test", "Test2", 2d);
        Account account2 = new Account(AccountKey.valueOf(2), "Test3", "Test4", 3d);
        accountService.transfer(account2, account, 4);
    }

    @Test(expected = DevExpertsArgumentException.class)
    public void transferAmountLessThenZero() {
        AccountService accountService = new AccountServiceImpl();
        Account account = new Account(AccountKey.valueOf(1), "Test", "Test2", 2d);
        Account account2 = new Account(AccountKey.valueOf(2), "Test3", "Test4", 3d);
        accountService.transfer(account2, account, -4);
    }

    @Test(expected = DevExpertsNotFoundException.class)
    public void transferIfTargetNull() {
        AccountService accountService = new AccountServiceImpl();
        Account account = new Account(AccountKey.valueOf(1), "Test", "Test2", 0d);
        accountService.transfer(account, null, 4);
    }

    @Test(expected = DevExpertsNotFoundException.class)
    public void transferIfSourceNull() {
        AccountService accountService = new AccountServiceImpl();
        Account account = new Account(AccountKey.valueOf(1), "Test", "Test2", 0d);
        accountService.transfer(null, account, 4);
    }

    @Test(expected = DevExpertsArgumentException.class)
    public void transferIfSourceEqualTarget() {
        AccountService accountService = new AccountServiceImpl();
        Account account = new Account(AccountKey.valueOf(1), "Test", "Test2", 0d);
        accountService.transfer(account, account, 4);
    }

    @Test(expected = DevExpertsArgumentException.class)
    public void transferIfSourceEqualTargetById() {
        AccountService accountService = new AccountServiceImpl();
        Account account = new Account(AccountKey.valueOf(1), "Test", "Test2", 0d);
        Account account2 = new Account(AccountKey.valueOf(1), "Test", "Test2", 0d);
        accountService.transfer(account, account2, 4);
    }

    @Test(expected = DevExpertsArgumentException.class)
    public void createNullAccount() {
        AccountService accountService = new AccountServiceImpl();
        accountService.createAccount(null);
    }

    @Test(expected = DevExpertsArgumentException.class)
    public void createWithOutName() {
        AccountService accountService = new AccountServiceImpl();
        Account account = new Account(AccountKey.valueOf(1), null, "", 0d);
        accountService.createAccount(account);
    }

    @Test(expected = DevExpertsArgumentException.class)
    public void createWithMinusBalance() {
        AccountService accountService = new AccountServiceImpl();
        Account account = new Account(AccountKey.valueOf(1), "Test", "Test2", -5d);
        accountService.createAccount(account);
    }
}
