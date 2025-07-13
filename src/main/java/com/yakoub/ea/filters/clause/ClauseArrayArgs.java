package com.yakoub.ea.filters.clause;





import com.yakoub.ea.filters.creator.AttributeCreator;
import com.yakoub.ea.filters.creator.JoinCreator;
import com.yakoub.ea.filters.enums.Operation;
import com.yakoub.ea.filters.factory.ValueFactory;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.criteria.*;
import java.util.Arrays;

@Getter
@EqualsAndHashCode(callSuper = true)
public class ClauseArrayArgs extends Clause {
    private final String[] args;


    public ClauseArrayArgs(String filed, Operation operation, String[] args) {
        super(filed, operation);
        this.args = args;
    }



    static public Predicate toPredicate(Root<?> root, CriteriaBuilder criteriaBuilder, Clause clause) {
        ClauseArrayArgs arrayClause = (ClauseArrayArgs) clause;
        String attribute = AttributeCreator.resolveAttributeName(clause.getField());
        if (arrayClause.getArgs() == null || arrayClause.getArgs().length == 0) {
            return  criteriaBuilder.conjunction(); // or throw exception
        }
        Path<?>  parentExpression = createParentExpression(root, clause);
        Predicate inPredicate = parentExpression.in(Arrays.stream(arrayClause.getArgs())
                .map(arg -> {
                    try {
                        return ValueFactory.toValue(root, attribute, arg);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }).toArray());

        if (arrayClause.getOperation() == Operation.NotIn) {
            return criteriaBuilder.not(inPredicate);
        }
        return inPredicate;
    }

    private static Path<?> createParentExpression(Root<?> root, Clause clause) {
        Join<?, ?> join = JoinCreator.createJoin(root, clause.getField());
        String attribute = AttributeCreator.resolveAttributeName(clause.getField());
        return (join != null ? join : root).get(attribute);
    }
}
