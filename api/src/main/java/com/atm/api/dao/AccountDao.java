package com.atm.api.dao;

import com.atm.api.model.Account;
import ratpack.exec.Promise;

public interface AccountDao {

    Promise<Account> findAccountByCard(String cardNumber);
}
