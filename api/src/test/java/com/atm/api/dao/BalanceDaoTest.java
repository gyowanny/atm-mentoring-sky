package com.atm.api.dao;

import com.atm.api.model.Account;
import com.atm.api.sql.mybatis.AbstractMyBatisTest;
import org.junit.Test;

import static org.junit.Assert.*;

public class BalanceDaoTest extends AbstractMyBatisTest {

    @Test
    public void getBalance() throws Exception {
        // When
        session.getMapper(BalanceDao.class).getBalance(1l);
    }

}