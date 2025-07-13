package com.yakoub.ea.filters.enums;



import com.yakoub.ea.execptions.BusinessException;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private static final Map<String, Operation> LABEL_MAP =
            Stream.of(values()).collect(Collectors.toMap(op -> op.label, op -> op));

    public static Operation valueOfLabel(String label) throws BusinessException {
        Operation op = LABEL_MAP.get(label);
        if (op == null) {
            throw new BusinessException("The operation '" + label + "' is not valid.");
        }
        return op;
    }

    public static boolean isValidOperation(String label) {
        return LABEL_MAP.containsKey(label);
    }

    @Override
    public String toString() {
        return label;
    }
}
