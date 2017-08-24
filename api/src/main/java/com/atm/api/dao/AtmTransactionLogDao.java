package com.atm.api.dao;

import com.atm.api.model.AtmTransactionLog;

public interface AtmTransactionLogDao {

    void saveTransactionLog(AtmTransactionLog log);
}
