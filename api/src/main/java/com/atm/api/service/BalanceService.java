package com.atm.api.service;

import com.atm.api.model.Account;
import com.atm.api.model.Balance;
import ratpack.exec.Promise;

public class BalanceService {
    public Promise<Balance> getBalance(Account account) {
        return Promise.value(null);
    }

    public Promise<Balance> withdraw(Account account, Double amount) {
        return Promise.value(null);
    }
}
