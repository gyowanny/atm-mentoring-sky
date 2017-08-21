package com.atm.api.handler;

import com.atm.api.service.AccountService;
import com.atm.api.service.BalanceService;
import com.atm.api.validator.CardValidator;
import ratpack.exec.Promise;
import ratpack.handling.Context;
import ratpack.handling.Handler;

import javax.inject.Inject;

import static ratpack.jackson.Jackson.json;

public class BalanceHandler implements Handler {
    private final AccountService accountService;
    private final BalanceService balanceService;
    private final CardValidator cardValidator;

    @Inject
    public BalanceHandler(AccountService accountService, BalanceService balanceService, CardValidator cardValidator) {
        this.accountService = accountService;
        this.balanceService = balanceService;
        this.cardValidator = cardValidator;
    }

    @Override
    public void handle(Context ctx) throws Exception {
        Promise.value(ctx.getAllPathTokens().get("cardNumber"))
                .route(cardNumber -> !cardValidator.isValid(cardNumber), ignored -> {
                    ctx.getResponse().status(403).send("Invalid card number");
                })
                .flatMap(accountService::findAccountByCard)
                .onNull(() -> ctx.getResponse().status(404).send("Card not found"))
                .flatMap(balanceService::getBalance)
                .then(balance -> {
                    ctx.getResponse().status(200);
                    ctx.render(json(balance));
                });

    }
}
