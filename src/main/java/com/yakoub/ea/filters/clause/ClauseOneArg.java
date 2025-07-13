package com.yakoub.ea.filters.clause;





import com.yakoub.ea.filters.creator.AttributeCreator;
import com.yakoub.ea.filters.creator.JoinCreator;
import com.yakoub.ea.filters.enums.Operation;
import com.yakoub.ea.filters.factory.ValueFactory;
import lombok.Getter;

import javax.persistence.criteria.*;
import javax.validation.constraints.NotEmpty;
import java.text.ParseException;
import java.util.Objects;

@Getter
public class ClauseOneArg  extends Clause {
    @NotEmpty
    private final String arg;



    public ClauseOneArg(String filed, Operation operation, String arg) {
        super(filed, operation);
        this.arg = arg;
    }



     public static Predicate toPredicate(Root<?> root , CriteriaBuilder criteriaBuilder , Clause clause) throws ParseException, ClassNotFoundException {
        ClauseOneArg oneArg = (ClauseOneArg) clause;
        Join<?, ?> join = JoinCreator.createJoin(root, clause.getField());
         From<?, ?> from = (join != null)
                 ? join
                 : root;
        String attribute = AttributeCreator.resolveAttributeName(clause.getField());
        Path<?> path = from.get(attribute);
         Object value;
         try {
             value = ValueFactory.toValue(from, attribute, oneArg.getArg());
         } catch (Exception e) {
             throw new RuntimeException(e);
         }
         return switch (oneArg.getOperation()) {
             case NotEquals -> criteriaBuilder.notEqual(path, value);
             case Equals -> criteriaBuilder.equal(path, value);
             case Greater -> criteriaBuilder.greaterThan(asComparable(path), (Comparable<Object>) value);
             case GreaterOrEquals ->  criteriaBuilder.greaterThanOrEqualTo(asComparable(path), (Comparable<Object>) value);
             case Less -> criteriaBuilder.lessThan(asComparable(path), (Comparable<Object>) value);
             case LessOrEquals -> criteriaBuilder.lessThanOrEqualTo(asComparable(path), (Comparable<Object>) value);
             case Contains -> {
                 Expression<String> expr = criteriaBuilder.upper(path.as(String.class));
                 String pattern = "%" + value.toString().toUpperCase() + "%";
                 yield criteriaBuilder.like(expr, pattern);
             }
             case Notcontains -> {
                 Expression<String> expr = criteriaBuilder.upper(path.as(String.class));
                 String pattern = "%" + value.toString().toUpperCase() + "%";
                 yield criteriaBuilder.notLike(expr, pattern);
             }
             case Startswith -> {
                 Expression<String> expr = path.as(String.class);
                 yield criteriaBuilder.like(expr, value + "%");
             }
             case Endswith -> {
                 Expression<String> expr = path.as(String.class);
                 yield criteriaBuilder.like(expr, "%" + value);
             }
             default -> throw new UnsupportedOperationException("Unsupported operation: " + oneArg.getOperation());
         };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ClauseOneArg that = (ClauseOneArg) o;
        return Objects.equals(arg, that.arg);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), arg);
    }

    @SuppressWarnings("unchecked")
    private static Expression<Comparable<Object>> asComparable(Path<?> path) {
        return (Expression<Comparable<Object>>) path;
    }

}
