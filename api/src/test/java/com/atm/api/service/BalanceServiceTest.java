package com.atm.api.service;

import com.atm.api.data.DataSet;
import com.atm.api.model.Account;
import com.atm.api.model.Balance;
import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import ratpack.exec.ExecResult;
import ratpack.test.exec.ExecHarness;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class BalanceServiceTest {
    private BalanceService instance;

    @Mock
    private DataSet dataSet;

    private Map<Account, Balance> balanceMap;
    private Account account;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        instance = new BalanceService(dataSet);

        account = new Account();
        account.setAccount("123456");
        account.setBranchCode("111");
    }

    @Test
    public void retrieveBalanceForAnExistingAccount() throws Exception {
        // Given
        Balance expectedBalance = new Balance();
        expectedBalance.setAccount(account);
        expectedBalance.setValue(20d);

        balanceMap = ImmutableMap.of(account, expectedBalance);

        when(dataSet.getBalanceDataSet()).thenReturn(balanceMap);

        // When
        executeAndAssertBalance(expectedBalance);
    }

    @Test
    public void retrieveBalanceAsZeroForAnExistingAccountWithNoBalance() throws Exception {
        // Given
        Balance expectedBalance = new Balance();
        expectedBalance.setAccount(account);
        expectedBalance.setValue(0d);

        balanceMap = ImmutableMap.of();

        when(dataSet.getBalanceDataSet()).thenReturn(balanceMap);

        // When
        executeAndAssertBalance(expectedBalance);
    }

    private void executeAndAssertBalance(Balance expectedBalance) throws Exception {
        try (ExecHarness harness = ExecHarness.harness()) {
            ExecResult<Balance> result = harness.yield(execution -> instance.getBalance(account));
            verify(dataSet).getBalanceDataSet();
            assertThat(result.getValue()).isEqualTo(expectedBalance);
        }
    }
}