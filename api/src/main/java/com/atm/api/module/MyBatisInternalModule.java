package com.atm.api.module;

import com.atm.api.service.AccountService;
import com.google.inject.*;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.mybatis.guice.MyBatisModule;
import org.mybatis.guice.datasource.builtin.PooledDataSourceProvider;
import org.mybatis.guice.datasource.helper.JdbcHelper;

import static org.apache.ibatis.io.Resources.getResourceAsReader;

public class MyBatisInternalModule extends MyBatisModule {

    @Override
    protected void initialize() {
        String env = System.getProperties().getProperty("env", "local");
        environmentId(env);

        install(JdbcHelper.MySQL);
        bindDataSourceProviderType(PooledDataSourceProvider.class);
        bindTransactionFactoryType(JdbcTransactionFactory.class);
        addMapperClasses("com.atm.api.dao");
    }

}
