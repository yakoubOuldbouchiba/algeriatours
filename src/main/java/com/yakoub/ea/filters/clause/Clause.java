package com.yakoub.ea.filters.clause;






import com.yakoub.ea.filters.creator.AttributeCreator;
import com.yakoub.ea.filters.creator.JoinCreator;
import com.yakoub.ea.filters.enums.Operation;
import lombok.Getter;

import javax.persistence.criteria.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;
@Getter
public class Clause {
    @NotEmpty
    private final String field;
    @NotNull
    private final Operation operation;

    public static Predicate buildNullCheckPredicate(Root<?> root, CriteriaBuilder cb, Clause clause) {
        Join<?, ?> join = JoinCreator.createJoin(root, clause.getField());
        String attribute = AttributeCreator.resolveAttributeName(clause.getField());
        Path<?> path = (join != null ? join : root).get(attribute);

        return switch (clause.getOperation()) {
            case Is_Null -> cb.isNull(path);
            case Is_Not_Null -> cb.isNotNull(path);
            default -> null;
        };
    }

    public Clause(String filed, Operation operation) {
        this.field = filed;
        this.operation = operation;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Clause clause = (Clause) o;
        return Objects.equals(field, clause.field) && operation == clause.operation;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(field, operation);
    }
}
