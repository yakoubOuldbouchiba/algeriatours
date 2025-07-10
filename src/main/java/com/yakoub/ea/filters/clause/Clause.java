package com.yakoub.ea.filters.clause;






import com.yakoub.ea.filters.creator.AttribueCreator;
import com.yakoub.ea.filters.creator.JoinCreator;
import com.yakoub.ea.filters.enums.Operation;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Objects;

public class Clause {
    private String filed;
    private Operation operation;
    
    
    public static Predicate toPredicate2(Root root, CriteriaBuilder criteriaBuilder, Clause clause) {
        Join joinMap = JoinCreator.createJoin(root, clause.getFiled());
        String attribute = AttribueCreator.createAttribute(clause.getFiled());
        try {
            switch (clause.getOperation()) {
                case Is_Null:
                    if (joinMap == null) {
                        return criteriaBuilder.isNull(root.get(attribute));
                    } else {
                        return criteriaBuilder.isNull(joinMap.get(attribute));
                    }
                case Is_Not_Null:
                    if (joinMap == null) {
                        return criteriaBuilder.isNotNull(root.get(attribute));
                    } else {
                        return criteriaBuilder.isNotNull(joinMap.get(attribute));
                    }
                default:
                    return null;
            }
        } catch (IllegalArgumentException illegalArgumentException) {
            throw illegalArgumentException;
        }
    }
    
    
    public Clause(String id, String s, int i) {
    }
    
    public Clause(String filed, Operation operation) {
        this.filed = filed;
        this.operation = operation;
    }
    
    public String getFiled() {
        return filed;
    }
    
    public void setFiled(String filed) {
        this.filed = filed;
    }
    
    public Operation getOperation() {
        return operation;
    }
    
    public void setOperation(Operation operation) {
        this.operation = operation;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Clause clause = (Clause) o;
        return Objects.equals(filed, clause.filed) && operation == clause.operation;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(filed, operation);
    }
}
