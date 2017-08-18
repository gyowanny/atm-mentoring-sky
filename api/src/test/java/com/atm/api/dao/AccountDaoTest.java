package com.atm.api.dao;

import com.atm.api.data.DataSet;
import com.atm.api.model.Account;
import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import ratpack.exec.ExecResult;
import ratpack.test.exec.ExecHarness;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class AccountDaoTest {
    public static final String CARD_NUMBER = "cardNumber";

    private AccountDao instance;

    @Mock
    private DataSet dataSet;

    private Map<String,Account> accountMap;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        instance = new AccountDao(dataSet);
    }

    @Test
    public void shouldRetrieveAnExistingAccountByCardNumber() throws Exception {
        // Given
        Account account1 = new Account();
        account1.setAccount("123456");
        account1.setBranchCode("111");

        accountMap = ImmutableMap.of(CARD_NUMBER, account1);

        when(dataSet.getAccountDataSet()).thenReturn(accountMap);

        // When
        try (ExecHarness harness = ExecHarness.harness()) {
            ExecResult<Account> result = harness.yield(execution -> instance.findAccountByCard(CARD_NUMBER));
            verify(dataSet).getAccountDataSet();
            assertThat(result.getValue()).isEqualTo(account1);
        }
    }

    @Test
    public void shouldReturnNullWhenNoAccountIsFoundForTheGivenCardNumber() throws Exception {
        // Given
        accountMap = ImmutableMap.of();

        when(dataSet.getAccountDataSet()).thenReturn(accountMap);

        // When
        try (ExecHarness harness = ExecHarness.harness()) {
            ExecResult<Account> result = harness.yield(execution -> instance.findAccountByCard(CARD_NUMBER));
            verify(dataSet).getAccountDataSet();
            assertThat(result.getValue()).isNull();
        }
    }
}