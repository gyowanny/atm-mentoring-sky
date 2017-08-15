package com.atm.api.model;

public class Account {

    private String branchCode;
    private String account;

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

    @Override
    public String toString() {
        return "Account{" +
                "branchCode='" + branchCode + '\'' +
                ", account='" + account + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account1 = (Account) o;

        if (branchCode != null ? !branchCode.equals(account1.branchCode) : account1.branchCode != null) return false;
        return account != null ? account.equals(account1.account) : account1.account == null;

    }

    @Override
    public int hashCode() {
        int result = branchCode != null ? branchCode.hashCode() : 0;
        result = 31 * result + (account != null ? account.hashCode() : 0);
        return result;
    }
}
