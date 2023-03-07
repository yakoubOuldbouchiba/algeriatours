package com.yakoub.ea.filter.specification;


import com.yakoub.ea.filter.clause.Clause;
import com.yakoub.ea.filter.clause.ClauseArrayArgs;
import com.yakoub.ea.filter.clause.ClauseOneArg;
import com.yakoub.ea.filter.clause.ClauseTwoArgs;
import com.yakoub.ea.filter.enums.Connecteur;
import org.springframework.data.jpa.domain.Specification;


import javax.persistence.criteria.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


public class GenericSpecification <T> implements Specification<T> {
    private final List<Clause > clauses;
    private final Connecteur connecteur;

    public GenericSpecification(List<Clause> clauses , Connecteur connecteur) {
        this.clauses = clauses;
        this.connecteur = connecteur;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if(clauses !=null &&!clauses.isEmpty()){

            //create the predicates
            clauses.forEach(clause -> {
                if(clause instanceof ClauseOneArg){
                    try {
                        predicates.add(ClauseOneArg.toPredicate(root, criteriaBuilder, clause));
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
                if(clause instanceof ClauseTwoArgs){
                    predicates.add(ClauseTwoArgs.toPredicate(root, criteriaBuilder, clause));
                }
                if(clause instanceof ClauseArrayArgs){
                    predicates.add(ClauseArrayArgs.toPredicate(root, criteriaBuilder, clause));
                }
            });
        }
        if(connecteur.equals(Connecteur.Or)){
            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
