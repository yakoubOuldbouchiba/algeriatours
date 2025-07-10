package com.yakoub.ea.filters.enums;

public enum Connecteur {
    Or("or"),
    And("and");
    public final String label;

    Connecteur(String label) {
        this.label = label;
    }

    public static Connecteur valueOfLabel(String label) {
        for (Connecteur e : values()) {
            if (e.label.equalsIgnoreCase(label)) {
                return e;
            }
        }
        return null;
    }

    public static boolean isOperation(String label) {
        return valueOfLabel(label) != null;
    }
}
