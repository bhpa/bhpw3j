package io.bhpw3j.protocol.core;

import java.math.BigInteger;

/**
 * Represents a block parameter. It takes either a block number or block name as input.
 *
 * BHP does not specifies the notion of "block name". This is an abstraction built by
 * bhpw3j library.
 */
public interface BlockParameter {

    static BlockParameter valueOf(BigInteger blockNumber) {
        return new BlockParameterIndex(blockNumber);
    }

    static BlockParameter valueOf(String blockName) {
        return BlockParameterName.fromString(blockName);
    }

    String getValue();
}
