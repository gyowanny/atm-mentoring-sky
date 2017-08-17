package com.atm.api.handler.util;

import com.atm.api.handler.WithdrawHandler;
import com.atm.api.model.request.WithdrawRequest;
import com.atm.api.validator.CardValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import ratpack.exec.Promise;
import ratpack.handling.Context;
import ratpack.http.TypedData;

public final class OperationHelper {
    private static final CardValidator cardValidator = new CardValidator();

    public static Promise<WithdrawRequest> parseWithdrawRequestAndValidateCardNumber(Context ctx) throws Exception {
        final String cardNumber = ctx.getPathTokens().get("cardNumber");
        return ctx.parse(WithdrawRequest.class)
                .route(withdrawRequest -> !cardValidator.isValid(cardNumber),
                        ignored -> ctx.getResponse().status(403).send("Invalid card number"))
                .map(withdrawRequest -> {
                    withdrawRequest.setCard(cardNumber);
                    return withdrawRequest;
                });

    }
}
