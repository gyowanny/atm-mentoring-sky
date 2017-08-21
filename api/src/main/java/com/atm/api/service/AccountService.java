package com.atm.api.service;

import com.atm.api.dao.AccountDao;
import com.atm.api.model.Account;
import ratpack.exec.Promise;

import javax.inject.Inject;

public class AccountService {
    private final AccountDao accountDao;

    @Inject
    public AccountService(AccountDao accountMapper) {
        this.accountDao = accountMapper;
    }

    public Promise<Account> findAccountByCard(String cardNumber) {
        return Promise.value(accountDao.findAccountByCard(cardNumber));
    }
}
