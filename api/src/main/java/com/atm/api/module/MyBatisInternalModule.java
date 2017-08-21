package com.atm.api.module;

import com.atm.api.dao.AccountDao;
import com.atm.api.dao.mybatis.MyBatisAccountDao;
import com.google.inject.*;
import org.mybatis.guice.XMLMyBatisModule;

import static org.apache.ibatis.io.Resources.getResourceAsReader;

public class MyBatisInternalModule extends XMLMyBatisModule {

    @Override
    protected void initialize() {
        String env = System.getProperties().getProperty("env", "local");
        setEnvironmentId(env);
        setClassPathResource(String.format("mybatis/mybatis-config-%s.xml", env));
        bindTransactionInterceptors();
        bind(AccountDao.class).to(MyBatisAccountDao.class).in(Scopes.SINGLETON);
    }

}
