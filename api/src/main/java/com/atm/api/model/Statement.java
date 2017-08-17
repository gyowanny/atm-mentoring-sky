package com.atm.api.model;

import java.util.List;

public class Statement {
    private List<String> transactions;

    public void setTransactions(List<String> transactions) {
        this.transactions = transactions;
    }

    public List<String> getTransactions() {
        return transactions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Statement statement = (Statement) o;

        return transactions != null ? transactions.equals(statement.transactions) : statement.transactions == null;

    }

    @Override
    public int hashCode() {
        return transactions != null ? transactions.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Statement{" +
                "transactions=" + transactions +
                '}';
    }
}
