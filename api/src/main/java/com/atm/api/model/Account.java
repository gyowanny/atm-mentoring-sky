package com.atm.api.model;

public class Account {
    private Long id;
    private String branchCode;
    private String account;
    private String cardNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccount() {
        return account;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", branchCode='" + branchCode + '\'' +
                ", account='" + account + '\'' +
                ", cardNumber='" + cardNumber + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account1 = (Account) o;

        if (id != null ? !id.equals(account1.id) : account1.id != null) return false;
        if (branchCode != null ? !branchCode.equals(account1.branchCode) : account1.branchCode != null) return false;
        if (account != null ? !account.equals(account1.account) : account1.account != null) return false;
        return cardNumber != null ? cardNumber.equals(account1.cardNumber) : account1.cardNumber == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (branchCode != null ? branchCode.hashCode() : 0);
        result = 31 * result + (account != null ? account.hashCode() : 0);
        result = 31 * result + (cardNumber != null ? cardNumber.hashCode() : 0);
        return result;
    }
}
