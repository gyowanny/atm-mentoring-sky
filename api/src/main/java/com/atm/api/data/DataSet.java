package com.atm.api.data;

import com.atm.api.model.Account;
import com.atm.api.model.Balance;
import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

public class DataSet {
    private static Map<String, Account> ACCOUNTS;

    private static Map<Account,Balance> BALANCES;

    static {
        Account account1 = new Account();
        account1.setAccount("123456");
        account1.setBranchCode("111");

        Account account2 = new Account();
        account2.setAccount("654321");
        account2.setBranchCode("222");

        ACCOUNTS = ImmutableMap.of("1111", account1, "2222", account2);

        Balance balanceForAccount1 = new Balance();
        balanceForAccount1.setAccount(account1);
        balanceForAccount1.setValue(20d);

        Balance balanceForAccount2 = new Balance();
        balanceForAccount2.setAccount(account2);
        balanceForAccount2.setValue(100d);

        BALANCES = ImmutableMap.of(account1, balanceForAccount1, account2, balanceForAccount2);
    }

    public Map<String,Account> getAccountDataSet() {
        return ACCOUNTS;
    }

    public Map<Account, Balance> getBalanceDataSet() {
        return BALANCES;
    }
}
