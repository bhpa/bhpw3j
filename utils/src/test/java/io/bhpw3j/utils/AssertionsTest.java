package io.bhpw3j.utils;

import org.junit.Test;

public class AssertionsTest {

    @Test
    public void testVerifyPrecondition() {
        Assertions.verifyPrecondition(true, "");
    }

    @Test(expected = RuntimeException.class)
    public void testVerifyPreconditionFailure() {
        Assertions.verifyPrecondition(false, "");
    }
}
