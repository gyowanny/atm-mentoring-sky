package com.atm.api.module;

import com.atm.api.service.ratpack.DatabaseInitService;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.name.Names;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

public class AtmModule extends AbstractModule {
    private static final Logger LOGGER = LoggerFactory.getLogger(AtmModule.class);

    @Override
    protected void configure() {
        Names.bindProperties(binder(), System.getProperties());
        Names.bindProperties(binder(), loadAppConfig());

        install(new MyBatisInternalModule());

        install(new HandlersModule());

        bind(DatabaseInitService.class).in(Scopes.SINGLETON);
    }

    private Properties loadAppConfig() {
        Properties props = new Properties();
        String configFileName = String.format("app-config-%s.properties",
                System.getProperties().getProperty("env", "local"));
        try {
            props.load(AtmModule.class.getClassLoader().getResourceAsStream(configFileName));
        } catch (Exception e) {
            LOGGER.warn("App config file {} not found", configFileName);
        }
        return props;
    }
}
