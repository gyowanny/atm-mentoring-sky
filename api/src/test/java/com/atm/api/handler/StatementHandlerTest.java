package com.atm.api.handler;

import com.atm.api.dao.AccountDao;
import com.atm.api.model.Balance;
import com.atm.api.model.Statement;
import com.atm.api.service.BalanceService;
import com.atm.api.validator.CardValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import ratpack.exec.Promise;
import ratpack.jackson.internal.DefaultJsonRender;
import ratpack.test.handling.HandlingResult;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static ratpack.test.handling.RequestFixture.requestFixture;

public class StatementHandlerTest {

    private StatementHandler instance;

    private final ObjectMapper mapper = new ObjectMapper();

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

        when(cardValidator.isValid(any())).thenReturn(true);
        when(balanceService.getStatement(any())).thenReturn(Promise.value(expectedStatement));

        // When
        HandlingResult handlingResult = requestFixture()
                .pathBinding(ImmutableMap.of("cardNumber", cardNumber))
                .handle(instance);

        // Then
        assertThat(handlingResult.getStatus().getCode()).isEqualTo(200);
        Statement statement = (Statement) mapper.readValue(handlingResult.getBodyBytes(),
                DefaultJsonRender.class).getObject();
        assertThat(statement).isEqualTo(expectedStatement);
    }
}