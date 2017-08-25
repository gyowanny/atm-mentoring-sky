package com.atm.api.sql.mybatis;

import com.atm.api.dao.AccountDao;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.mybatis.guice.MyBatisModule;
import org.mybatis.guice.datasource.helper.JdbcHelper;

import javax.sql.DataSource;

public class MyBatisSessionFactoryProvider {
    private static SqlSessionFactory sqlSessionFactory;

    private MyBatisSessionFactoryProvider instance = new MyBatisSessionFactoryProvider();

    public static SqlSessionFactory provide() {
        if (sqlSessionFactory == null) {
            createFactory();
        }
        return sqlSessionFactory;
    }

    private MyBatisSessionFactoryProvider() {

    }

    private static void createFactory() {
        UnpooledDataSource dataSource = new UnpooledDataSource("com.mysql.jdbc.Driver",
                "jdbc:mysql://localhost:3306/atm", "atm", "atm");
        dataSource.setAutoCommit(false);

        TransactionFactory transactionFactory = new JdbcTransactionFactory();

        Environment environment = new Environment("test", transactionFactory, dataSource);

        Configuration configuration = new Configuration(environment);
        configuration.addMappers("com.atm.api.dao");

        sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
    }
}
