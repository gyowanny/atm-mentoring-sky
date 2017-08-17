package com.atm.api.handler;

import com.atm.api.dao.AccountDao;
import com.atm.api.service.BalanceService;
import com.atm.api.validator.CardValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import ratpack.exec.Promise;
import ratpack.handling.Context;
import ratpack.handling.Handler;

import javax.inject.Inject;

import java.util.HashMap;

import static ratpack.jackson.Jackson.json;

public class BalanceHandler implements Handler {

    private final AccountDao accountDao;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final BalanceService balanceService;
    private final CardValidator cardValidator;

    @Inject
    public BalanceHandler(AccountDao accountDao, BalanceService balanceService, CardValidator cardValidator) {
        this.accountDao = accountDao;
        this.balanceService = balanceService;
        this.cardValidator = cardValidator;
    }

    @Override
    public void handle(Context ctx) throws Exception {
        Promise.value(ctx.getPathTokens().get("cardNumber"))
                .route(cardNumber -> !cardValidator.isValid(cardNumber), ignored -> {
                    ctx.getResponse().status(403).send("Invalid card number");
                })
                .flatMap(cardNumber -> accountDao.findAccountByCard(cardNumber))
                .onNull(() -> ctx.getResponse().status(404).send("Card not found"))
                .flatMap(account -> balanceService.getBalance(account))
                .then(balance -> {
                    ctx.getResponse().status(200);
                    ctx.render(json(balance));
                });

    }
}
