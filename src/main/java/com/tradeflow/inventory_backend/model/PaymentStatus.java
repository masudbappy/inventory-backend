package com.tradeflow.inventory_backend.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PaymentStatus {
    NONE,
    PARTIAL,
    PAID,
    UNPAID;

    @JsonCreator
    public static PaymentStatus fromString(String value) {
        if (value == null) {
            return NONE;
        }

        try {
            return PaymentStatus.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            // Handle legacy values or different cases
            switch (value.toLowerCase()) {
                case "partial":
                    return PARTIAL;
                case "paid":
                    return PAID;
                case "unpaid":
                case "pending":
                    return UNPAID;
                case "none":
                default:
                    return NONE;
            }
        }
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}