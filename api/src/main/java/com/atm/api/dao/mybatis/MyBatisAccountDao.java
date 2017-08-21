package com.atm.api.dao.mybatis;

import com.atm.api.dao.AccountDao;
import com.atm.api.model.Account;
import org.apache.ibatis.session.SqlSession;
import ratpack.exec.Promise;

import javax.inject.Inject;

public class MyBatisAccountDao implements AccountDao {
    private final SqlSession session;

    @Inject
    public MyBatisAccountDao(SqlSession session) {
        this.session = session;
    }

    @Override
    public Promise<Account> findAccountByCard(String cardNumber) {
        return Promise.value(session.selectOne(Account.class.getName()+".findByCard",
                cardNumber));
    }
}
