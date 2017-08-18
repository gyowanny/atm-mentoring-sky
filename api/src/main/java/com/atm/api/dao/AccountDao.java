package com.atm.api.dao;

import com.atm.api.data.DataSet;
import com.atm.api.model.Account;
import ratpack.exec.Promise;

import javax.inject.Inject;

public class AccountDao {

    private final DataSet dataSet;

    @Inject
    public AccountDao(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    public Promise<Account> findAccountByCard(String cardNumber) {
        return Promise.value(dataSet.getAccountDataSet().get(cardNumber));
    }
}
