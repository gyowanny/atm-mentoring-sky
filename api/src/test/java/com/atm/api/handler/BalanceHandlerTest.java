package com.atm.api.handler;

import com.atm.api.model.Account;
import com.atm.api.model.Balance;
import com.atm.api.service.AccountService;
import com.atm.api.service.AtmTransactionLogService;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static ratpack.test.handling.RequestFixture.requestFixture;

public class BalanceHandlerTest {

    private BalanceHandler instance;

    @Mock
    private AccountService accountDao;

    @Mock
    private BalanceService balanceService;

    @Mock
    private CardValidator cardValidator;

    @Mock
    private AtmTransactionLogService atmTransactionLogService;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        instance = new BalanceHandler(accountDao, balanceService, cardValidator, atmTransactionLogService);
    }

    @Test
    public void returns200AndTheBalanceForTheGivenBranchAndAccountAssignedToTheCardNumber() throws Exception {
        // Given
        final String cardNumber = "aValidCardNumber";
        final String requestBody = createRequestBody(cardNumber);

        final Account expectedAccount = new Account();
        expectedAccount.setBranchCode("branchCode");
        expectedAccount.setAccount("account");

        final Balance expectedBalance = new Balance();
        expectedBalance.setAccount(expectedAccount);
        expectedBalance.setValue(Double.valueOf(100));

        when(accountDao.findAccountByCard(any())).thenReturn(Promise.value(expectedAccount));
        when(balanceService.getBalance(any())).thenReturn(Promise.value(expectedBalance));
        when(cardValidator.isValid(any())).thenReturn(true);

        // When
        HandlingResult handlingResult = requestFixture()
                .method("GET")
                .pathBinding(ImmutableMap.of("cardNumber", cardNumber))
                .handle(instance);

        // Then
        verify(cardValidator).isValid(eq(cardNumber));
        verify(accountDao).findAccountByCard(eq(cardNumber));
        verify(balanceService).getBalance(eq(expectedAccount));
        assertThat(handlingResult.getStatus().getCode()).isEqualTo(200);
        Balance actualBalance = (Balance) handlingResult.rendered(DefaultJsonRender.class).getObject();
        assertThat(actualBalance).isEqualTo(expectedBalance);
    }

    @Test
    public void returns403ForInvalidCardNumber() throws Exception {
        // Given
        final String cardNumber = "invalidCardNumber";
        final String requestBody = createRequestBody(cardNumber);

        when(cardValidator.isValid(any())).thenReturn(false);

        // When
        HandlingResult handlingResult = requestFixture()
                .method("GET")
                .pathBinding(ImmutableMap.of("cardNumber", cardNumber))
                .handle(instance);

        // Then
        verify(cardValidator).isValid(eq(cardNumber));
        verify(accountDao, times(0)).findAccountByCard(eq(cardNumber));
        assertThat(handlingResult.getStatus().getCode()).isEqualTo(403);
        assertThat(handlingResult.getBodyText()).isEqualTo("Invalid card number");
    }

    @Test
    public void returns404WhenCardNumberIsValidButWasNotFound() throws Exception {
        // Given
        final String cardNumber = "aValidCardNumber";
        final String requestBody = createRequestBody(cardNumber);

        when(accountDao.findAccountByCard(any())).thenReturn(Promise.value(null));
        when(cardValidator.isValid(any())).thenReturn(true);

        // When
        HandlingResult handlingResult = requestFixture()
                .method("GET")
                .pathBinding(ImmutableMap.of("cardNumber", cardNumber))
                .handle(instance);

        // Then
        verify(cardValidator).isValid(eq(cardNumber));
        verify(accountDao).findAccountByCard(eq(cardNumber));
        assertThat(handlingResult.getStatus().getCode()).isEqualTo(404);
        assertThat(handlingResult.getBodyText()).isEqualTo("Card not found");
    }

    private String createRequestBody(String cardNumber) {
        return "{\"card\":\""+cardNumber+"\"}";
    }
}