package io.bhpw3j.protocol.core;

import com.fasterxml.jackson.annotation.JsonValue;
import io.bhpw3j.utils.Numeric;

import java.math.BigInteger;

/**
 * Represents a parameter as a number.
 * This class converts to string hexadecimal.
 */
public class HexParameterNumber implements HexParameter {

    private BigInteger param;

    public HexParameterNumber(BigInteger paramAsBigInteger) {
        this.param = paramAsBigInteger;
    }

    @Override
    @JsonValue
    public String getHexValue() {
        return Numeric.toHexStringNoPrefix(this.param);
    }
}
