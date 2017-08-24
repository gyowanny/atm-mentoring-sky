package com.atm.api.service;

import com.atm.api.dao.AtmTransactionLogDao;
import com.atm.api.model.AtmTransactionLog;
import ratpack.exec.Blocking;

import javax.inject.Inject;
import java.util.Date;

public class AtmTransactionLogService {

    private final AtmTransactionLogDao transactionLogDao;

    @Inject
    public AtmTransactionLogService(AtmTransactionLogDao transactionLogDao) {
        this.transactionLogDao = transactionLogDao;
    }

    public void logTransaction(String cardNumber, String description) {
        Blocking.exec(() -> {
            AtmTransactionLog log = new AtmTransactionLog();
            log.setCardNumber(cardNumber);
            log.setDateTime(new Date());
            log.setDescription(description);

            transactionLogDao.saveTransactionLog(log);
        });
    }
}
