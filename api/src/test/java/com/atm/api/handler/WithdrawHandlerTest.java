package com.atm.api.handler;

import com.atm.api.dao.AccountDao;
import com.atm.api.model.Account;
import com.atm.api.model.Balance;
import com.atm.api.service.BalanceService;
import com.atm.api.validator.CardValidator;
import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import ratpack.exec.Promise;
import ratpack.jackson.internal.DefaultJsonRender;
import ratpack.test.handling.HandlingResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static ratpack.test.handling.RequestFixture.requestFixture;

public class WithdrawHandlerTest {

    private WithdrawHandler instance;

    @Mock
    private AccountDao accountDao;

    @Mock
    private BalanceService balanceService;

    @Mock
    private CardValidator cardValidator;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        instance = new WithdrawHandler(accountDao, balanceService, cardValidator);
    }

    @Test
    public void shouldWithdrawAndReturnTheUpdatedBalance() throws Exception {
        // Given
        final String cardNumber = "cardNumber";

        final Account expectedAccount = new Account();
        expectedAccount.setBranchCode("branchCode");
        expectedAccount.setAccount("account");

        final Balance expectedBalance = new Balance();
        expectedBalance.setAccount(expectedAccount);
        expectedBalance.setValue(Double.valueOf(80));

        when(cardValidator.isValid(any())).thenReturn(true);
        when(accountDao.findAccountByCard(any())).thenReturn(Promise.value(expectedAccount));
        when(balanceService.withdraw(any(), any())).thenReturn(Promise.value(expectedBalance));

        // When
        HandlingResult handlingResult = requestFixture()
                .pathBinding(ImmutableMap.of("cardNumber", cardNumber))
                .body(createRequestBody(), "application/json")
                .timeout(60)
                .handle(instance);

        // Then
        verify(accountDao).findAccountByCard(eq(cardNumber));
        verify(balanceService).withdraw(eq(expectedAccount), eq(Double.valueOf("20.00")));
        assertThat(handlingResult.getStatus().getCode()).isEqualTo(200);
        Balance balance = (Balance) handlingResult.rendered(DefaultJsonRender.class).getObject();
        assertThat(balance).isEqualTo(expectedBalance);
    }

    @Test
    public void shouldReturn403ForInvalidCardNumber() throws Exception {
        // Given
        final String cardNumber = "invalidCardNumber";

        when(cardValidator.isValid(any())).thenReturn(false);

        // When
        HandlingResult handlingResult = requestFixture()
                .pathBinding(ImmutableMap.of("cardNumber", cardNumber))
                .body(createRequestBody(), "application/json")
                .handle(instance);

        // Then
        assertThat(handlingResult.getStatus().getCode()).isEqualTo(403);
        assertThat(handlingResult.getBodyText()).isEqualTo("Invalid card number");
    }

    @Test
    public void shouldReturn403WhenTheAvailableBalanceIsLessThanTheRequestedAmount() throws Exception {
        // Given
        final String cardNumber = "cardNumber";

        final Account expectedAccount = new Account();
        expectedAccount.setBranchCode("branchCode");
        expectedAccount.setAccount("account");

        final Balance expectedBalance = new Balance();
        expectedBalance.setAccount(expectedAccount);
        expectedBalance.setValue(Double.valueOf(-80));
        expectedBalance.setMessage("Not enough funds");

        when(cardValidator.isValid(any())).thenReturn(true);
        when(accountDao.findAccountByCard(any())).thenReturn(Promise.value(expectedAccount));
        when(balanceService.withdraw(any(), any())).thenReturn(Promise.value(expectedBalance));

        // When
        HandlingResult handlingResult = requestFixture()
                .pathBinding(ImmutableMap.of("cardNumber", cardNumber))
                .body(createRequestBody(), "application/json")
                .handle(instance);

        // Then
        verify(accountDao).findAccountByCard(eq(cardNumber));
        verify(balanceService).withdraw(eq(expectedAccount), eq(Double.valueOf("20.00")));
        assertThat(handlingResult.getStatus().getCode()).isEqualTo(403);
        Balance balance = (Balance) handlingResult.rendered(DefaultJsonRender.class).getObject();
        assertThat(balance).isEqualTo(expectedBalance);
    }

    private String createRequestBody() {
        return "{\"pin\":\"1234\",\"amount\":\"20.00\"}";
    }
}