package io.bhpw3j.contract.abi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import io.bhpw3j.contract.ContractParameter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BhpContractEvent {

    @JsonProperty("name")
    private String name;

    @JsonProperty("parameters")
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<ContractParameter> parameters;

    public BhpContractEvent() {
    }

    public BhpContractEvent(String name, List<ContractParameter> parameters) {
        this.name = name;
        this.parameters = parameters != null ? parameters : new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<ContractParameter> getParameters() {
        return parameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BhpContractEvent)) return false;
        BhpContractEvent that = (BhpContractEvent) o;
        return Objects.equals(getName(), that.getName()) &&
                Objects.equals(getParameters(), that.getParameters());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getParameters());
    }

    @Override
    public String toString() {
        return "BhpContractEvent{" +
                "name='" + name + '\'' +
                ", parameters=" + parameters +
                '}';
    }
}
