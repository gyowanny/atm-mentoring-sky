package com.atm.api.handler;

import com.atm.api.dao.AccountDao;
import com.atm.api.model.Balance;
import com.atm.api.model.request.WithdrawRequest;
import com.atm.api.service.BalanceService;
import com.atm.api.validator.CardValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import ratpack.handling.Context;
import ratpack.handling.Handler;
import ratpack.jackson.JsonRender;

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
        ctx.parse(WithdrawRequest.class)
                .map(withdrawRequest -> {
                    withdrawRequest.setCard(ctx.getPathTokens().get("cardNumber"));
                    return withdrawRequest;
                })
                .route(withdrawRequest -> !cardValidator.isValid(withdrawRequest.getCard()), invalidWithdrawRequest -> {
                    ctx.getResponse().status(403).send("Invalid card number");
                })
                .flatMap(withdrawRequest -> accountDao.findAccountByCard(withdrawRequest.getCard())
                        .map(account -> {
                            withdrawRequest.setAccount(account);
                            return withdrawRequest;
                        })
                )
                .then(withdrawRequest ->
                        balanceService.withdraw(withdrawRequest.getAccount(), Double.valueOf(withdrawRequest.getAmount()))
                                .route(balance -> balance.containsMessage(), balance -> {
                                    renderResponse(ctx, 403, balance);
                                })
                                .then(balance -> {
                                    renderResponse(ctx, 200, balance);
                                })
                );

    }

    private static void renderResponse(Context ctx, int code, Balance balance) {
        ctx.getResponse().status(code);
        ctx.render(json(balance));
    }

}
