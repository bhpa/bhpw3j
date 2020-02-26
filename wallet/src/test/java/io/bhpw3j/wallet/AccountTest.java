package io.bhpw3j.wallet;

import io.bhpw3j.crypto.ECKeyPair;
import io.bhpw3j.crypto.BRC2;
import io.bhpw3j.crypto.exceptions.CipherException;
import io.bhpw3j.crypto.exceptions.BRC2InvalidFormat;
import io.bhpw3j.crypto.exceptions.BRC2InvalidPassphrase;
import io.bhpw3j.protocol.Bhpw3j;
import io.bhpw3j.protocol.exceptions.ErrorResponseException;
import io.bhpw3j.protocol.http.HttpService;
import io.bhpw3j.wallet.brc6.BRC6Account;
import okhttp3.OkHttpClient;
import org.junit.Test;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;


public class AccountTest {

    @Test
    public void testBuildAccountFromKeyPair() throws InvalidAlgorithmParameterException,
            NoSuchAlgorithmException, NoSuchProviderException {

        ECKeyPair ecKeyPair = ECKeyPair.createEcKeyPair();
        Account a = Account.fromECKeyPair(ecKeyPair).build();
        assertEquals(ecKeyPair, a.getECKeyPair());
        // TODO Claude 11.06.19 Implement
    }

    @Test
    public void testCreateVerificationScriptContract() {
        // TODO Claude 11.06.19: Implement
    }

    @Test
    public void testCreateStandardAccount1() throws CipherException {
        Account account = Account.fromWIF("L44B5gGEpqEDRS9vVPz7QT35jcBG2r3CZwSwQ4fCewXAhAhqGVpP").build();
        account.encryptPrivateKey("TestingOneTwoThree", BRC2.DEFAULT_SCRYPT_PARAMS);
        assertEquals("6PYVPVe1fQznphjbUxXP9KZJqPMVnVwCx5s5pr5axRJ8uHkMtZg97eT5kL", account.getEncryptedPrivateKey());
    }

    @Test
    public void testCreateStandardAccount2() throws CipherException {
        Account account = Account.fromWIF("KwYgW8gcxj1JWJXhPSu4Fqwzfhp5Yfi42mdYmMa4XqK7NJxXUSK7").build();
        account.encryptPrivateKey("Satoshi", BRC2.DEFAULT_SCRYPT_PARAMS);
        assertEquals("6PYN6mjwYfjPUuYT3Exajvx25UddFVLpCw4bMsmtLdnKwZ9t1Mi3CfKe8S", account.getEncryptedPrivateKey());
    }

    @Test
    public void testDecryptStandard1() throws BRC2InvalidFormat, CipherException,
            BRC2InvalidPassphrase {

        BRC6Account brc6Acct = new BRC6Account(
                "AStZHy8E6StCqYQbzMqi4poH7YNDHQKxvt", "", true, false,
                "6PYVPVe1fQznphjbUxXP9KZJqPMVnVwCx5s5pr5axRJ8uHkMtZg97eT5kL", null, null);

        Account a = Account.fromBRC6Account(brc6Acct).build();
        a.decryptPrivateKey("TestingOneTwoThree", BRC2.DEFAULT_SCRYPT_PARAMS);
        assertEquals("L44B5gGEpqEDRS9vVPz7QT35jcBG2r3CZwSwQ4fCewXAhAhqGVpP", a.getECKeyPair().exportAsWIF());
    }

    @Test
    public void testDecryptStandard2() throws CipherException, BRC2InvalidFormat, BRC2InvalidPassphrase {

        BRC6Account brc6Acct = new BRC6Account(
                "AXoxAX2eJfJ1shNpWqUxRh3RWNUJqvQvVa", "", true, false,
                "6PYN6mjwYfjPUuYT3Exajvx25UddFVLpCw4bMsmtLdnKwZ9t1Mi3CfKe8S", null, null);

        Account a = Account.fromBRC6Account(brc6Acct).build();
        a.decryptPrivateKey("Satoshi", BRC2.DEFAULT_SCRYPT_PARAMS);
        assertEquals("KwYgW8gcxj1JWJXhPSu4Fqwzfhp5Yfi42mdYmMa4XqK7NJxXUSK7", a.getECKeyPair().exportAsWIF());
    }

    @Test
    public void testDecryptStandard3() throws CipherException, BRC2InvalidFormat, BRC2InvalidPassphrase {

        BRC6Account brc6Acct = new BRC6Account(
                "AdGPiWRqqoFMauM6anTNFB7MyBwQhEANyZ", "", true, false,
                "6PYUNvLELtv66vFYgmHuu11je7h4hTZiLTVbRk4RNvJZo75PurR6z7JnoX", null, null);

        Account a = Account.fromBRC6Account(brc6Acct).build();
        Wallet w = new Wallet.Builder().account(a).build();
        a.decryptPrivateKey("q1w2e3!@#", BRC2.DEFAULT_SCRYPT_PARAMS);
        assertEquals("L5fE7aDEiBLJwcf3Zr9NrUUuT9Rd8nc4kPkuJWqNhftdmx3xcyAd", a.getECKeyPair().exportAsWIF());
    }

    @Test
    public void testUpdateAccountBalances() throws IOException, ErrorResponseException {
        String address = "AK2nJJpJr6o664CWJKi1QRXjqeic2zRp8y";
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new ResponseInterceptor(address)).build();
        HttpService httpService = new HttpService(httpClient);
        Bhpw3j bhpw3J = Bhpw3j.build(httpService);

        Account a = Account.fromAddress("AK2nJJpJr6o664CWJKi1QRXjqeic2zRp8y").build();
        a.updateAssetBalances(bhpw3J);
    }

    @Test
    public void testCreateGenericAccount() {
        Account a = Account.createAccount();
        assertThat(a, notNullValue());
        assertThat(a.getAddress(), notNullValue());
        assertThat(a.getBalances(), notNullValue());
        assertThat(a.getContract(), notNullValue());
        assertThat(a.getECKeyPair(), notNullValue());
        assertThat(a.getEncryptedPrivateKey(), is(nullValue()));
        assertThat(a.getLabel(), notNullValue());
        assertThat(a.getPrivateKey(), notNullValue());
        assertThat(a.getPublicKey(), notNullValue());
        assertThat(a.isDefault(), is(false));
        assertThat(a.isLocked(), is(false));
    }

    @Test
    public void testFromNewECKeyPair() {
        Account a = Account.fromNewECKeyPair()
                .isDefault(true)
                .isLocked(false)
                .build();

        assertThat(a, notNullValue());
        assertThat(a.getAddress(), notNullValue());
        assertThat(a.getBalances(), notNullValue());
        assertThat(a.getContract(), notNullValue());
        assertThat(a.getECKeyPair(), notNullValue());
        assertThat(a.getEncryptedPrivateKey(), is(nullValue()));
        assertThat(a.getLabel(), notNullValue());
        assertThat(a.getPrivateKey(), notNullValue());
        assertThat(a.getPublicKey(), notNullValue());
        assertThat(a.isDefault(), is(true));
        assertThat(a.isLocked(), is(false));
    }

    @Test
    public void testFromMultiSigKeys() {
        Account a = Account.fromMultiSigKeys(
                Arrays.asList(
                        SampleKeys.KEY_PAIR_1.getPublicKey(),
                        SampleKeys.KEY_PAIR_2.getPublicKey()
                ),
                2
        ).build();

        assertThat(a.isMultiSig(), is(true));
        assertThat(a.getAddress(), is("ATcWffQV1A7NMEsqQ1RmKfS7AbSqcAp2hd"));
        assertThat(a.getPublicKey(), is(nullValue()));
        assertThat(a.getPrivateKey(), is(nullValue()));
        assertThat(a.getLabel(), is(a.getAddress()));
    }

}
