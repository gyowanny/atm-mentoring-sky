package com.atm.api.model;

public class Balance {
    private Account account;
    private Double value;
    private String message;

    public void setAccount(Account account) {
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Double getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean containsMessage() {
        return message != null && !message.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Balance balance = (Balance) o;

        if (account != null ? !account.equals(balance.account) : balance.account != null) return false;
        if (value != null ? !value.equals(balance.value) : balance.value != null) return false;
        return message != null ? message.equals(balance.message) : balance.message == null;

    }

    @Override
    public int hashCode() {
        int result = account != null ? account.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Balance{" +
                "account=" + account +
                ", value=" + value +
                ", message='" + message + '\'' +
                '}';
    }
}
