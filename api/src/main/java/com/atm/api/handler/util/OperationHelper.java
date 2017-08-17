package com.atm.api.handler.util;

import com.atm.api.handler.WithdrawHandler;
import com.atm.api.model.request.WithdrawRequest;
import com.atm.api.validator.CardValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import ratpack.exec.Promise;
import ratpack.handling.Context;
import ratpack.http.TypedData;

public final class OperationHelper {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final CardValidator cardValidator = new CardValidator();

    public static Promise<WithdrawRequest> parseWithdrawRequestAndValidateCardNumber(Promise<TypedData> body, Context ctx) throws Exception {
        return body.map(bodyParsed -> mapper.readValue(bodyParsed.getBytes(), WithdrawRequest.class))
                .route(withdrawRequest -> !cardValidator.isValid(withdrawRequest.getCard()),
                        ignored -> ctx.getResponse().status(403).send("Invalid card number"));
    }
}
