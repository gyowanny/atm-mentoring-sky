package com.atm.api.service.ratpack;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ratpack.service.Service;
import ratpack.service.StartEvent;
import ratpack.service.StopEvent;

import javax.inject.Inject;

import static org.apache.ibatis.io.Resources.getResourceAsReader;

public class DatabaseInitService implements Service {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseInitService.class);

    @Inject
    private SqlSessionFactory sessionFactory;

    @Override
    public void onStart(StartEvent event) throws Exception {
        if ("prod".equalsIgnoreCase(sessionFactory.getConfiguration().getEnvironment().getId())) {
            return;
        }

        LOGGER.info("### INITIALIZING DATABASE...");
        try(SqlSession sqlSession = sessionFactory.openSession()) {
            ScriptRunner scriptRunner = new ScriptRunner(sqlSession.getConnection());
            scriptRunner.setAutoCommit(true);
            scriptRunner.setStopOnError(true);
            scriptRunner.runScript(getResourceAsReader("db.sql"));
            sqlSession.close();
            LOGGER.info("### DATABASE INITIALIZED");
        }
    }

    @Override
    public void onStop(StopEvent event) throws Exception {

    }
}
