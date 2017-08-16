package com.atm.api.handler;

import com.atm.api.dao.AccountDao;
import com.atm.api.model.Account;
import com.atm.api.service.BalanceService;
import com.atm.api.validator.CardValidator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import ratpack.exec.Promise;
import ratpack.func.Pair;
import ratpack.handling.Context;
import ratpack.handling.Handler;
import ratpack.http.TypedData;

import java.io.IOException;

import static ratpack.jackson.Jackson.json;

public class WithdrawHandler implements Handler{

    private final AccountDao accountDao;
    private final BalanceService balanceService;
    private final ObjectMapper mapper = new ObjectMapper();
    private final CardValidator cardValidator;

    public WithdrawHandler(AccountDao accountDao, BalanceService balanceService, CardValidator cardValidator) {
        this.accountDao = accountDao;
        this.balanceService = balanceService;
        this.cardValidator = cardValidator;
    }

    @Override
    public void handle(Context ctx) throws Exception {
        parseAndValidate(ctx.getRequest().getBody(), ctx)
                .flatMap(withdrawRequest ->
                    accountDao.findAccountByCard(withdrawRequest.card)
                            .map(account -> {
                                withdrawRequest.account = account;
                                return withdrawRequest;
                            })
                )
                .flatMap(withdrawRequest ->
                        balanceService.withdraw(withdrawRequest.account, Double.valueOf(withdrawRequest.amount)))
                .route(balance -> balance.containsMessage(), balance -> {
                    ctx.getResponse().status(403);
                    ctx.render(json(balance));
                })
                .then(balance -> {
                    System.out.println(balance.toString());
                    ctx.getResponse().status(200);
                    ctx.render(json(balance));
                });
    }

    private Promise<WithdrawRequest> parseAndValidate(Promise<TypedData> body, Context ctx) throws Exception {
        return body.map(bodyParsed -> mapper.readValue(bodyParsed.getBytes(), WithdrawRequest.class))
                .route(withdrawRequest -> !cardValidator.isValid(withdrawRequest.card),
                        ignored -> ctx.getResponse().status(403).send("Invalid card number"));
    }

    private static class WithdrawRequest {
        @JsonProperty("card") private String card;
        @JsonProperty("pin") private String pin;
        @JsonProperty("amount") private String amount;
        @JsonIgnore private Account account;
    }
}
