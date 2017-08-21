package com.atm.api.dao;

import com.atm.api.model.Account;

public interface AccountDao {

    Account findAccountByCard(String cardNumber);
}
