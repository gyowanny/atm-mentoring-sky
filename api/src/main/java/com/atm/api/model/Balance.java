package com.atm.api.model;

public class Balance {
    private Account account;
    private Double value;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Balance balance = (Balance) o;

        if (!account.equals(balance.account)) return false;
        return value.equals(balance.value);

    }

    @Override
    public int hashCode() {
        int result = account.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Balance{" +
                "account=" + account +
                ", value=" + value +
                '}';
    }
}
