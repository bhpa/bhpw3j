package io.bhpw3j.contract;

import io.bhpw3j.constants.BHPConstants;
import io.bhpw3j.io.BinaryReader;
import io.bhpw3j.io.BinaryWriter;
import io.bhpw3j.io.BhpSerializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;

public class ContractDeploymentScript extends BhpSerializable {

    private static final Logger LOG = LoggerFactory.getLogger(ContractDeploymentScript.class);

    private byte[] scriptBinary;

    private ContractDescriptionProperties descriptionProperties;

    private ContractFunctionProperties functionProperties;

    private ScriptHash contractScriptHash;

    public ContractDeploymentScript() {
    }

    public ContractDeploymentScript(byte[] scriptBinary, ContractFunctionProperties functionProperties, ContractDescriptionProperties descriptionProperties) {
        this.scriptBinary = scriptBinary;
        this.functionProperties = functionProperties;
        this.descriptionProperties = descriptionProperties;
        this.contractScriptHash = ScriptHash.fromScript(scriptBinary);
    }

    public byte[] getScriptBinary() {
        return scriptBinary;
    }

    public ContractDescriptionProperties getDescriptionProperties() {
        return descriptionProperties;
    }

    public ContractFunctionProperties getFunctionProperties() {
        return functionProperties;
    }

    public ScriptHash getContractScriptHash() {
        return contractScriptHash;
    }

    public BigDecimal getDeploymentSystemFee() {
        int fee = BHPConstants.CONTRACT_DEPLOY_BASIC_FEE;
        if (functionProperties.getNeedsStorage()) {
            fee += BHPConstants.CONTRACT_DEPLOY_STORAGE_FEE;
        }
        if (functionProperties.getNeedsDynamicInvoke()) {
            fee += BHPConstants.CONTRACT_DEPLOY_DYNAMIC_INVOKE_FEE;
        }
        fee -= BHPConstants.FREE_OF_CHARGE_EXECUTION_COST;
        return new BigDecimal(Math.max(fee, 0));
    }

    @Override
    public void deserialize(BinaryReader reader) throws IOException {
        try {
            this.descriptionProperties = reader.readSerializable(ContractDescriptionProperties.class);
            this.functionProperties = reader.readSerializable(ContractFunctionProperties.class);
            this.scriptBinary = reader.readPushData();
            this.contractScriptHash = ScriptHash.fromScript(this.scriptBinary);
        } catch (IllegalAccessException e) {
            LOG.error("Can't access the specified object.", e);
        } catch (InstantiationException e) {
            LOG.error("Can't instantiate the specified object type.", e);
        }
    }

    @Override
    public void serialize(BinaryWriter writer) throws IOException {
        // description properties (i.e., description,
        // email, author, version, name)
        writer.writeSerializableFixed(this.descriptionProperties);
        // function properties (i.e., parameter types, return type,
        // needs storage, needs dynamic invoke, is payable)
        writer.writeSerializableFixed(this.functionProperties);
        // script binary (.avm)
        writer.write(new ScriptBuilder()
                .pushData(this.scriptBinary)
                .toArray());
        // syscall "Bhp.Contract.Create"
        writer.write(new ScriptBuilder()
                .sysCall("Bhp.Contract.Create")
                .toArray());
    }

}
