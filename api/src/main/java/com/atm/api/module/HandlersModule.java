package com.atm.api.module;

import com.atm.api.handler.BalanceHandler;
import com.atm.api.handler.StatementHandler;
import com.atm.api.handler.StatusHandler;
import com.atm.api.handler.WithdrawHandler;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class HandlersModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(StatusHandler.class).in(Scopes.SINGLETON);
        bind(StatementHandler.class).in(Scopes.SINGLETON);
        bind(BalanceHandler.class).in(Scopes.SINGLETON);
        bind(WithdrawHandler.class).in(Scopes.SINGLETON);
    }
}
