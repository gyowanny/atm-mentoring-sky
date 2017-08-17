package com.atm.api.model.request;

import com.atm.api.model.Account;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class WithdrawRequest {
    @JsonProperty("pin") private String pin;
    @JsonProperty("amount") private String amount;
    @JsonIgnore private String card;
    @JsonIgnore private Account account;

    public WithdrawRequest() {
    }

    public String getCard() {
        return card;
    }

    public String getPin() {
        return pin;
    }

    public String getAmount() {
        return amount;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setCard(String card) {
        this.card = card;
    }
}
