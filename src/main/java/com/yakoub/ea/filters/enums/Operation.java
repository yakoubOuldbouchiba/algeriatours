package com.yakoub.ea.filters.enums;



import com.yakoub.ea.execptions.BusinessException;

public enum Operation {
    Less("<"),
    LessOrEquals("<="),
    NotEquals("<>"),
    Equals("="),
    Greater(">"),
    GreaterOrEquals(">="),
    Between("between"),
    Contains("contains"),
    Endswith("endswith"),
    Notcontains("notcontains"),
    Startswith("startswith"),
    In("in"),
    NotIn("notin"),
    Is_Not_Null("is_not_null"),
    Is_Null("is_null");
    public final String label;

    Operation(String label) {
        this.label = label;
    }

    public static Operation valueOfLabel(String label) throws BusinessException {
        for (Operation e : values()) {
            if (e.label.equals(label)) {
                return e;
            }
        }
        throw new BusinessException("the operation "+label+" is not  valid ");
    }

    public static boolean isOperation(String label) throws BusinessException {
        return valueOfLabel(label) != null;
    }
}
