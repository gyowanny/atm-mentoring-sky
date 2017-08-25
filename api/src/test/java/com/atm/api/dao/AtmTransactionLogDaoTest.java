package com.atm.api.dao;

import com.atm.api.model.AtmTransactionLog;
import com.atm.api.sql.mybatis.AbstractMyBatisTest;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class AtmTransactionLogDaoTest extends AbstractMyBatisTest {

    @Test
    public void saveTransactionLog() throws Exception {
        // Given
        AtmTransactionLog log = new AtmTransactionLog();
        log.setCardNumber("cardNumber");
        log.setDescription("description");
        log.setDateTime(new Date());

        // When
        session.getMapper(AtmTransactionLogDao.class).saveTransactionLog(log);
    }

}