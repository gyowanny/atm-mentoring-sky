package com.atm.api.handler;

import com.atm.api.dao.AccountDao;
import com.atm.api.model.Balance;
import com.atm.api.model.Statement;
import com.atm.api.service.BalanceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import ratpack.jackson.internal.DefaultJsonRender;
import ratpack.test.handling.HandlingResult;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;
import static ratpack.test.handling.RequestFixture.requestFixture;

public class StatementHandlerTest {

    private StatementHandler instance;

    private final ObjectMapper mapper = new ObjectMapper();

    @Mock
    private AccountDao accountDao;

    @Mock
    private BalanceService balanceService;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        instance = new StatementHandler(accountDao, balanceService);
    }

    @Test
    public void shouldReturnAStatementObjectWithAllTheTransactions() throws Exception {
        // Given
        final String cardNumber = "cardNumber";
        final Statement expectedStatement = new Statement();
        expectedStatement.setTransactions(asList("transaction 1 : 10.00", "transaction 2 : -2.00"));

        // When
        HandlingResult handlingResult = requestFixture()
                .body(createBodyRequest(cardNumber), "application/json")
                .handle(instance);

        // Then
        assertThat(handlingResult.getStatus().getCode()).isEqualTo(200);
        Statement statement = (Statement) mapper.readValue(handlingResult.getBodyBytes(),
                DefaultJsonRender.class).getObject();
        assertThat(statement).isEqualTo(expectedStatement);
    }

    private String createBodyRequest(String cardNumber) {
        return "{\"card\":\""+cardNumber+"\"}";
    }
}