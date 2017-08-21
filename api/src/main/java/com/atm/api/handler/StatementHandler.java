package com.atm.api.handler;

import com.atm.api.service.AccountService;
import com.atm.api.service.BalanceService;
import com.atm.api.validator.CardValidator;
import ratpack.exec.Promise;
import ratpack.handling.Context;
import ratpack.handling.Handler;

import javax.inject.Inject;

import static ratpack.jackson.Jackson.json;

public class StatementHandler implements Handler {
    private final AccountService accountDao;
    private final BalanceService balanceService;
    private final CardValidator cardValidator;

    @Inject
    public StatementHandler(AccountService accountDao, BalanceService balanceService, CardValidator cardValidator) {
        this.accountDao = accountDao;
        this.balanceService = balanceService;
        this.cardValidator = cardValidator;
    }

    @Override
    public void handle(Context ctx) throws Exception {
        Promise.value(ctx.getPathTokens().get("cardNumber"))
            .route(cardNumber -> !cardValidator.isValid(cardNumber),
                    cardNumber -> ctx.getResponse().status(403).send("Invalid card number"))
            .flatMap(accountDao::findAccountByCard)
            .onNull(() -> ctx.getResponse().status(404).send("Card not found"))
            .flatMap(balanceService::getStatement)
            .then(statement -> {
                ctx.getResponse().status(200);
                ctx.render(json(statement));
            });

    }
}
