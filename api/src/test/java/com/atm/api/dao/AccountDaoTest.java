package com.atm.api.dao;

import com.atm.api.dao.AccountDao;
import com.atm.api.model.Account;
import com.atm.api.sql.mybatis.AbstractMyBatisTest;
import com.atm.api.sql.mybatis.MyBatisSessionFactoryProvider;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AccountDaoTest extends AbstractMyBatisTest {

    @Test
    public void findAccountByCardReturnsExistingAccount() throws Exception {
        // When
        session.getMapper(AccountDao.class).findAccountByCard("1234");
    }
}
