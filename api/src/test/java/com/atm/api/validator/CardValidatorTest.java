package com.atm.api.validator;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class CardValidatorTest {
    private CardValidator instance;

    @Before
    public void setUp() throws Exception {
        instance = new CardValidator();
    }

    @Test
    public void returnsTrueForAStringWithNumbersOnly() throws Exception {
        assertThat(instance.isValid("123456")).isTrue();
    }

    @Test
    public void returnsFalseForAStringWithNumbersAndLetters() throws Exception {
        assertThat(instance.isValid("123456ABC")).isFalse();
    }

    @Test
    public void returnsFalseForAStringWithLettersOnly() throws Exception {
        assertThat(instance.isValid("ABCDEFG")).isFalse();
    }

    @Test
    public void returnsFalseForAStringWithNotAllowedChars() throws Exception {
        assertThat(instance.isValid("$#@!@$%^")).isFalse();
    }

    @Test
    public void returnsFalseWhenNullIsPassedIn() throws Exception {
        assertThat(instance.isValid(null)).isFalse();
    }

    @Test
    public void returnsFalseWhenAnEmptyStringIsPassedIn() throws Exception {
        assertThat(instance.isValid("")).isFalse();
    }
}