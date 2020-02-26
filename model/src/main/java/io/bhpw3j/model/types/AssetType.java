package io.bhpw3j.model.types;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AssetType {

    CREDIT_FLAG("CreditFlag", 0x40),
    DUTY_FLAG("DutyFlag", 0x80),
    GOVERNING_TOKEN("GoverningToken", 0x00),
    UTILITY_TOKEN("UtilityToken", 0x01),
    SHARE("Share", (DUTY_FLAG.byteValue | 0x10)),
    INVOICE("Invoice", (DUTY_FLAG.byteValue | 0x18)),
    TOKEN("Token", (DUTY_FLAG.byteValue | 0x20));

    private String jsonValue;
    private byte byteValue;

    AssetType(String jsonValue, int byteValue) {
        this.jsonValue = jsonValue;
        this.byteValue = (byte) byteValue;
    }

    @JsonValue
    public String jsonValue() {
        return this.jsonValue;
    }

    public byte byteValue() {
        return this.byteValue;
    }

    @JsonCreator
    public static AssetType fromJson(Object value) {
        if (value instanceof String) {
            return fromJsonValue((String) value);
        }
        if (value instanceof Integer) {
            return valueOf(((Integer) value).byteValue());
        }
        throw new IllegalArgumentException(String.format("%s value type not found.", AssetType.class.getName()));
    }

    public static AssetType valueOf(byte byteValue) {
        for (AssetType e : AssetType.values()) {
            if (e.byteValue == byteValue) {
                return e;
            }
        }
        throw new IllegalArgumentException(String.format("%s value type not found.", AssetType.class.getName()));
    }

    public static AssetType fromJsonValue(String jsonValue) {
        for (AssetType e : AssetType.values()) {
            if (e.jsonValue.equals(jsonValue)) {
                return e;
            }
        }
        throw new IllegalArgumentException(String.format("%s value type not found.", AssetType.class.getName()));
    }

}
