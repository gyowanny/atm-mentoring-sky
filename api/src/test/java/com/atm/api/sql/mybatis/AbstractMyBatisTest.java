package com.atm.api.sql.mybatis;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.After;
import org.junit.Before;

public abstract class AbstractMyBatisTest {
    protected SqlSessionFactory sqlSessionFactory = MyBatisSessionFactoryProvider.provide();
    protected SqlSession session;

    @Before
    public void setUp() throws Exception {
        session = sqlSessionFactory.openSession();
    }

    @After
    public void tearDown() throws Exception {
        if (session != null) {
            session.rollback(true);
            session.close();
        }
    }
}
