package com.atm.api.handler;

import com.atm.api.dao.AccountDao;
import com.atm.api.service.BalanceService;
import com.atm.api.validator.CardValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import ratpack.handling.Context;
import ratpack.handling.Handler;

import javax.inject.Inject;

import static com.atm.api.handler.util.OperationHelper.parseWithdrawRequestAndValidateCardNumber;
import static ratpack.jackson.Jackson.json;

public class WithdrawHandler implements Handler{

    private final AccountDao accountDao;
    private final BalanceService balanceService;
    private final ObjectMapper mapper = new ObjectMapper();
    private final CardValidator cardValidator;

    @Inject
    public WithdrawHandler(AccountDao accountDao, BalanceService balanceService, CardValidator cardValidator) {
        this.accountDao = accountDao;
        this.balanceService = balanceService;
        this.cardValidator = cardValidator;
    }

    @Override
    public void handle(Context ctx) throws Exception {
        parseWithdrawRequestAndValidateCardNumber(ctx.getRequest().getBody(), ctx)
                .flatMap(withdrawRequest ->
                    accountDao.findAccountByCard(withdrawRequest.getCard())
                            .map(account -> {
                                withdrawRequest.setAccount(account);
                                return withdrawRequest;
                            })
                )
                .flatMap(withdrawRequest ->
                        balanceService.withdraw(withdrawRequest.getAccount(), Double.valueOf(withdrawRequest.getAmount())))
                .route(balance -> balance.containsMessage(), balance -> {
                    ctx.getResponse().status(403);
                    ctx.render(json(balance));
                })
                .then(balance -> {
                    ctx.getResponse().status(200);
                    ctx.render(json(balance));
                });
    }

}
