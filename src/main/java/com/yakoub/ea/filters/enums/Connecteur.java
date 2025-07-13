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
        throw new IllegalArgumentException("No Connecteur found for label: " + label);
    }

    @Override
    public String toString() {
        return label;
    }

}
