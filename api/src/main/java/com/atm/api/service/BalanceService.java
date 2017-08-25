package com.atm.api.service;

import com.atm.api.dao.BalanceDao;
import com.atm.api.model.Account;
import com.atm.api.model.Balance;
import com.atm.api.model.Statement;
import ratpack.exec.Blocking;
import ratpack.exec.Promise;

import javax.inject.Inject;

public class BalanceService {
    private final BalanceDao balanceDao;

    @Inject
    public BalanceService(BalanceDao balanceDao) {
        this.balanceDao = balanceDao;
    }

    /**
     * Retrieves the balance of the given account.
     * If no balance is found a balance with ZERO of value is returned.
     *
     * @param account
     * @return A balance object
     */
    public Promise<Balance> getBalance(Account account) {
        return Blocking.get(() -> balanceDao.getBalance(account.getId()))
                .mapIf(balance -> balance == null, ignored -> {
                    Balance balance = new Balance();
                    balance.setAccount(account);
                    balance.setValue(0d);
                    return balance;
                });
    }

    /**
     * Takes the given amount out of the balance and return the updated balance.
     * <p>
     * Pre-conditions:
     * <p>
     * 1. Look at the current balance in order to check if there's enough funds. If not return a balance
     * object with the current balance value and the message "Not enough funds"
     * <p>
     * After pre-conditions are satisfied:
     * <p>
     * 1. Update the current balance by taking out the given amount
     * 2. Register a transaction in the transactions DataSet
     * 3. Return the updated balance
     *
     * @param account
     * @param amount
     * @return
     */
    public Promise<Balance> withdraw(Account account, Double amount) {
        return Promise.value(null);
    }

    /**
     * The statement should return a list of the given account's transactions in the following format:
     * <p>
     * date time description amount
     *
     * @param account
     * @return A statement object
     */
    public Promise<Statement> getStatement(Account account) {
        return Promise.value(null);
    }
}
