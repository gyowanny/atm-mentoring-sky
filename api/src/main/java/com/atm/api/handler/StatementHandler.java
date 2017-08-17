package com.atm.api.handler;

import com.atm.api.dao.AccountDao;
import com.atm.api.service.BalanceService;
import com.atm.api.validator.CardValidator;
import ratpack.handling.Context;
import ratpack.handling.Handler;

import javax.inject.Inject;

public class StatementHandler implements Handler {
    private final AccountDao accountDao;
    private final BalanceService balanceService;
    private final CardValidator cardValidator;

    @Inject
    public StatementHandler(AccountDao accountDao, BalanceService balanceService, CardValidator cardValidator) {
        this.accountDao = accountDao;
        this.balanceService = balanceService;
        this.cardValidator = cardValidator;
    }

    @Override
    public void handle(Context ctx) throws Exception {
        final String cardNumber = ctx.getPathTokens().get("cardNumber");

    }
}
