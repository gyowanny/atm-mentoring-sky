package com.atm.api.handler;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import ratpack.test.handling.HandlingResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static ratpack.test.handling.RequestFixture.requestFixture;

public class StatusHandlerTest {

    private StatusHandler instance;

    @Before
    public void setUp() {
        instance = new StatusHandler();
    }

    @Test
    public void requestShouldBeSent() {
        HandlingResult handlingResult = requestFixture()
                .method("GET")
                .handle(instance);

        assertThat(handlingResult.getStatus().getCode()).isEqualTo(200);
    }

}
