package com.atm.api.dao;

import com.atm.api.model.Account;
import com.atm.api.model.Balance;

public interface BalanceDao {

    Balance getBalance(Long accountId);
}
