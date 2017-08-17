package com.atm.api.handler;

import com.atm.api.dao.AccountDao;
import com.atm.api.service.BalanceService;
import ratpack.handling.Context;
import ratpack.handling.Handler;

import javax.inject.Inject;

public class StatementHandler implements Handler {

    private final AccountDao accountDao;
    private final BalanceService balanceService;

    @Inject
    public StatementHandler(AccountDao accountDao, BalanceService balanceService) {
        this.accountDao = accountDao;
        this.balanceService = balanceService;
    }

    @Override
    public void handle(Context ctx) throws Exception {

    }
}
