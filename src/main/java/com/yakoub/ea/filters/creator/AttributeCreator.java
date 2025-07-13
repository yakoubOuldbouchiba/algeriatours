package com.yakoub.ea.filters.creator;

public class AttributeCreator {
    static public String resolveAttributeName(String field) {
        if (field == null || field.isBlank()) {
            throw new IllegalArgumentException("Field cannot be null or empty");
        }
        String[] parts = field.split("\\.");
        return parts[parts.length - 1];
    }
}
