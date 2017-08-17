package com.atm.api.module;

import com.atm.api.dao.AccountDao;
import com.atm.api.handler.BalanceHandler;
import com.atm.api.handler.StatementHandler;
import com.atm.api.handler.StatusHandler;
import com.atm.api.service.BalanceService;
import com.atm.api.validator.CardValidator;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class AtmModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(AccountDao.class).in(Scopes.SINGLETON);

        bind(BalanceService.class).in(Scopes.SINGLETON);

        bind(CardValidator.class).in(Scopes.SINGLETON);

        bind(StatusHandler.class).in(Scopes.SINGLETON);
        bind(BalanceHandler.class).in(Scopes.SINGLETON);
        bind(StatementHandler.class).in(Scopes.SINGLETON);
    }
}
