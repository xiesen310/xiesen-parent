package com.github.xiesen.common.model;

public enum AttributeRuleType {
    DATE("Date"),
    TIMESTAMP("Timestamp"),
    EMAIL("Email"),
    PHONE_NUMBER("Phone Number"),
    IP_ADDRESS("IP Address"),
    UUID("UUID");

    private final String description;

    AttributeRuleType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static AttributeRuleType fromString(String text) {
        for (AttributeRuleType b : AttributeRuleType.values()) {
            if (b.description.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }
}
