package com.atm.api;

import com.atm.api.handler.BalanceHandler;
import com.atm.api.handler.StatementHandler;
import com.atm.api.handler.WithdrawHandler;
import com.atm.api.handler.StatusHandler;
import com.atm.api.module.AtmModule;
import ratpack.guice.Guice;
import ratpack.server.RatpackServer;
import ratpack.server.ServerConfig;

public class Main {

    public static void main(String[] args) throws Exception {
        RatpackServer.start(
            server -> server
                .serverConfig(config -> ServerConfig.embedded())
                .registry(Guice.registry(b -> b.module(AtmModule.class)))
                .handlers(chain -> {
                    chain.get("private/status", StatusHandler.class);
                    chain.prefix("atm/:cardNumber", atmChain -> atmChain
                            .get(BalanceHandler.class)
                            .put(WithdrawHandler.class)
                            .get("statement", StatementHandler.class)
                    );
                })
        );
    }

}
