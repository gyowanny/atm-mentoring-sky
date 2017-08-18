package com.atm.api.validator;

public class CardValidator {
    private static final String REGEX_CARD_NUMBER = "\\d+";

    public boolean isValid(String cardNumber) {
        if (cardNumber == null)
            return false;

        return cardNumber.matches(REGEX_CARD_NUMBER);
    }
}
