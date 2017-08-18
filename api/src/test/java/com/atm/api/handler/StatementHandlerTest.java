package com.atm.api.handler;

import com.atm.api.dao.AccountDao;
import com.atm.api.model.Account;
import com.atm.api.model.Balance;
import com.atm.api.model.Statement;
import com.atm.api.service.BalanceService;
import com.atm.api.validator.CardValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.internal.verification.Times;
import ratpack.exec.Promise;
import ratpack.jackson.internal.DefaultJsonRender;
import ratpack.test.handling.HandlingResult;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static ratpack.test.handling.RequestFixture.requestFixture;

public class StatementHandlerTest {

    private StatementHandler instance;

    @Mock
    private AccountDao accountDao;

    @Mock
    private BalanceService balanceService;

    @Mock
    private CardValidator cardValidator;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        instance = new StatementHandler(accountDao, balanceService, cardValidator);
    }

    @Test
    public void shouldReturnAStatementObjectWithAllTheTransactions() throws Exception {
        // Given
        final String cardNumber = "cardNumber";
        final Statement expectedStatement = new Statement();
        expectedStatement.setTransactions(asList("10/08/2017 transaction 1 : 10.00", "11/08/2017 transaction 2 : -2.00"));

        final Account expectedAccount = new Account();
        expectedAccount.setBranchCode("branchCode");
        expectedAccount.setAccount("account");

        when(cardValidator.isValid(any())).thenReturn(true);
        when(accountDao.findAccountByCard(any())).thenReturn(Promise.value(expectedAccount));
        when(balanceService.getStatement(any())).thenReturn(Promise.value(expectedStatement));

        // When
        HandlingResult handlingResult = requestFixture()
                .pathBinding(ImmutableMap.of("cardNumber", cardNumber))
                .handle(instance);

        // Then
        verify(accountDao).findAccountByCard(eq(cardNumber));
        verify(balanceService).getStatement(eq(expectedAccount));
        assertThat(handlingResult.getStatus().getCode()).isEqualTo(200);
        Statement statement = (Statement) handlingResult.rendered(DefaultJsonRender.class).getObject();
        assertThat(statement).isEqualTo(expectedStatement);
    }

    @Test
    public void shouldReturn403ForInvalidCardNumber() throws Exception {
        // Given
        final String cardNumber = "invalidCardNumber";

        when(cardValidator.isValid(any())).thenReturn(false);

        // When
        HandlingResult handlingResult = requestFixture()
                .pathBinding(ImmutableMap.of("cardNumber", cardNumber))
                .handle(instance);

        // Then
        assertThat(handlingResult.getStatus().getCode()).isEqualTo(403);
        assertThat(handlingResult.getBodyText()).isEqualTo("Invalid card number");

    }

    @Test
    public void returns404WhenCardNumberIsValidButWasNotFound() throws Exception {
        // Given
        final String cardNumber = "cardNumber";

        final Account expectedAccount = new Account();
        expectedAccount.setBranchCode("branchCode");
        expectedAccount.setAccount("account");

        when(cardValidator.isValid(any())).thenReturn(true);
        when(accountDao.findAccountByCard(any())).thenReturn(Promise.value(null));
        when(balanceService.getStatement(any())).thenReturn(Promise.value(null));

        // When
        HandlingResult handlingResult = requestFixture()
                .pathBinding(ImmutableMap.of("cardNumber", cardNumber))
                .handle(instance);

        // Then
        verify(accountDao).findAccountByCard(eq(cardNumber));
        verify(balanceService, new Times(0)).getStatement(eq(expectedAccount));
        assertThat(handlingResult.getStatus().getCode()).isEqualTo(404);
        assertThat(handlingResult.getBodyText()).isEqualTo("Card not found");
    }
}