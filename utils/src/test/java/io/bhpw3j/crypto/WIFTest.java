package io.bhpw3j.crypto;

import io.bhpw3j.utils.Numeric;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class WIFTest {

    @Test
    public void testWIF() {
        final String privateKey = "9117f4bf9be717c9a90994326897f4243503accd06712162267e77f18b49c3a3";

        byte[] privateKeyFromWIF = WIF.getPrivateKeyFromWIF("L25kgAQJXNHnhc7Sx9bomxxwVSMsZdkaNQ3m2VfHrnLzKWMLP13A");

        assertThat(Numeric.toHexStringNoPrefix(privateKeyFromWIF), is(privateKey));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWIFLargerThan38() {
        WIF.getPrivateKeyFromWIF("L25kgAQJXNHnhc7Sx9bomxxwVSMsZdkaNQ3m2VfHrnLzKWMLP13A000");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWIFFirstByteDifferentThan0x80() {
        byte[] wifBytes = Base58.decode("L25kgAQJXNHnhc7Sx9bomxxwVSMsZdkaNQ3m2VfHrnLzKWMLP13A");
        wifBytes[0] = (byte) 0x81;
        String wifString = Base58.encode(wifBytes);
        WIF.getPrivateKeyFromWIF(wifString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWIF33ByteDifferentThan0x01() {
        byte[] wifBytes = Base58.decode("L25kgAQJXNHnhc7Sx9bomxxwVSMsZdkaNQ3m2VfHrnLzKWMLP13A");
        wifBytes[33] = (byte) 0x00;
        String wifString = Base58.encode(wifBytes);
        WIF.getPrivateKeyFromWIF(wifString);
    }

    @Test(expected = NullPointerException.class)
    public void testWIFNull() {
        WIF.getPrivateKeyFromWIF(null);
    }

}
