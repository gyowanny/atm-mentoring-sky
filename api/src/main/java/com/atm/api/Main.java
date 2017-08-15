package com.atm.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.atm.api.handler.BalanceHandler;
import com.atm.api.handler.StatusHandler;
import com.atm.api.module.AtmModule;
import ratpack.guice.Guice;
import ratpack.server.RatpackServer;
import ratpack.server.ServerConfig;

public class Main {

    public static void main(String[] args) throws Exception {
        RatpackServer.start(server -> server
                .serverConfig(config -> ServerConfig.embedded())
                .registryOf(registry -> registry
                        .add(ObjectMapper.class, new ObjectMapper().registerModule(new Jdk8Module()))
                )
                .registry(Guice.registry(b -> b.module(AtmModule.class)))
                .handlers(chain -> {
                    chain.get("private/status", StatusHandler.class);
                    chain.prefix("atm", atmChain -> atmChain
                            .put("balance", BalanceHandler.class)
                    );
                })
        );
    }

}
