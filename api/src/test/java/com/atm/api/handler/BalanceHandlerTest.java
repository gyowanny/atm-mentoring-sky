package com.atm.api.handler;

import com.atm.api.dao.AccountDao;
import com.atm.api.model.Account;
import com.atm.api.model.Balance;
import com.atm.api.service.BalanceService;
import com.atm.api.validator.CardValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
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

    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private AccountDao accountDao;

    @Mock
    private BalanceService balanceService;

    @Mock
    private CardValidator cardValidator;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        instance = new BalanceHandler(accountDao, balanceService, cardValidator);
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

        when(accountDao.findAccountByCard(any())).thenReturn(expectedAccount);
        when(balanceService.getBalance(any())).thenReturn(expectedBalance);
        when(cardValidator.isValid(any())).thenReturn(true);

        // When
        HandlingResult handlingResult = requestFixture()
                .method("GET")
                .body(requestBody, "application/json")
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
                .body(requestBody, "application/json")
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

        when(accountDao.findAccountByCard(any())).thenReturn(null);
        when(cardValidator.isValid(any())).thenReturn(true);

        // When
        HandlingResult handlingResult = requestFixture()
                .method("GET")
                .body(requestBody, "application/json")
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