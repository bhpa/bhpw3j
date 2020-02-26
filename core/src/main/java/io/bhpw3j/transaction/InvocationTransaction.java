package io.bhpw3j.transaction;

import io.bhpw3j.crypto.transaction.RawTransaction;
import io.bhpw3j.io.BinaryReader;
import io.bhpw3j.io.BinaryWriter;
import io.bhpw3j.model.types.TransactionType;
import io.bhpw3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;

public class InvocationTransaction extends RawTransaction {

    private byte[] contractScript;
    private BigDecimal systemFee;

    public InvocationTransaction() { }

    protected InvocationTransaction(Builder builder) {
        super(builder);
        this.contractScript = builder.contractScript;
        this.systemFee = builder.systemFee;
    }

    @Override
    public void serializeExclusive(BinaryWriter writer) throws IOException {
        writer.writeVarBytes(contractScript);
        writer.write(Numeric.fromDecimalToFixed8ByteArray(this.systemFee));
    }

    @Override
    public void deserializeExclusive(BinaryReader reader) throws IOException {
        this.contractScript = reader.readVarBytes();
        this.systemFee = Numeric.fromFixed8ToDecimal(reader.readBytes(8));
    }

    public byte[] getContractScript() {
        return this.contractScript;
    }

    public static class Builder extends RawTransaction.Builder<Builder> {

        private byte[] contractScript;
        private BigDecimal systemFee;

        public Builder() {
            super();
            transactionType(TransactionType.INVOCATION_TRANSACTION);
            systemFee = BigDecimal.ZERO;
            contractScript = new byte[]{};
        }

        public Builder contractScript(byte[] script) {
            this.contractScript = script; return this;
        }

        public Builder systemFee(BigDecimal gas) {
            this.systemFee = gas; return this;
        }

        @Override
        public InvocationTransaction build() {
            return new InvocationTransaction(this);
        }
    }
}

