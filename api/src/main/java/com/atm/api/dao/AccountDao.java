package com.atm.api.dao;

import com.atm.api.model.Account;
import ratpack.exec.Promise;

public class AccountDao {

    public Promise<Account> findAccountByCard(String cardNumber) {
        return Promise.value(null);
    }
}
