package com.atm.api.dao.mybatis;

import com.atm.api.model.Account;
import org.apache.ibatis.session.SqlSession;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import ratpack.exec.ExecResult;
import ratpack.test.exec.ExecHarness;

import java.sql.Connection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalMatchers.eq;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * MyBatis Guice: http://www.mybatis.org/guice/getting-started.html
 * Unit testing MyBatis: https://github.com/mybatis/mybatis-3/wiki/Unit-Test
 */
public class MyBatisAccountDaoTest {
    private MyBatisAccountDao instance;
    @Mock
    private SqlSession session;
    @Mock
    private Connection connection;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        when(session.getConnection()).thenReturn(connection);

        instance = new MyBatisAccountDao(session);
    }

    @Test
    public void findAccountByCardMustReturnTheAccountAssociatedToTheGivenCard() throws Exception {
        // Given
        final String cardNumber = "cardNumber";

        final Account expectedAccount = new Account();
        expectedAccount.setBranchCode("1111");
        expectedAccount.setAccount("123456");

        when(session.selectOne(Account.class.getName()+".findByCard", cardNumber)).thenReturn(expectedAccount);

        // When
        try(ExecHarness harness = ExecHarness.harness()) {
            ExecResult<Account> result = harness.yield(exec -> instance.findAccountByCard(cardNumber));
            verify(session).selectOne(any(), any());
            assertThat(result.getValue()).isEqualTo(expectedAccount);
        }

    }

}