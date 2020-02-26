package io.bhpw3j.protocol.core.methods.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.bhpw3j.protocol.core.Response;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class BhpValidateAddress extends Response<BhpValidateAddress.Result> {

    public BhpValidateAddress.Result getValidation() {
        return getResult();
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Result {

        @JsonProperty("address")
        private String address;

        @JsonProperty("isvalid")
        private Boolean isValid;

        public Result() {
        }

        public Result(String address, Boolean isValid) {
            this.address = address;
            this.isValid = isValid;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public Boolean getValid() {
            return isValid;
        }

        public void setValid(Boolean valid) {
            isValid = valid;
        }

        public Boolean isValid() {
            return isValid;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Result)) return false;
            Result result = (Result) o;
            return Objects.equals(getAddress(), result.getAddress()) &&
                    Objects.equals(isValid, result.isValid);
        }

        @Override
        public int hashCode() {
            return Objects.hash(getAddress(), isValid);
        }

        @Override
        public String toString() {
            return "Result{" +
                    "address='" + address + '\'' +
                    ", isValid=" + isValid +
                    '}';
        }
    }

}
