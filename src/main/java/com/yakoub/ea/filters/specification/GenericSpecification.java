package com.yakoub.ea.filters.specification;




import com.yakoub.ea.filters.clause.Clause;
import com.yakoub.ea.filters.clause.ClauseArrayArgs;
import com.yakoub.ea.filters.clause.ClauseOneArg;
import com.yakoub.ea.filters.clause.ClauseTwoArgs;
import com.yakoub.ea.filters.enums.Connecteur;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


public class GenericSpecification<T> implements Specification<T> {
    private final List<Clause> filter;

    private Connecteur connecteur = Connecteur.And;

    public GenericSpecification(List<Clause> filter) {
        this.filter = filter;
    }

    public GenericSpecification(List<Clause> filter, Connecteur connecteur) {
        this.filter = filter;
        this.connecteur = connecteur;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        //query.distinct(true);
        List<Predicate> predicates = new ArrayList<>();

        if (filter != null && !filter.isEmpty()) {

            //create the predicates
            filter.forEach(clause -> {
                if (clause instanceof ClauseOneArg) {
                    try {
                        predicates.add(ClauseOneArg.toPredicate(root, criteriaBuilder, clause));
                    } catch (ParseException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    return;
                }
                if (clause instanceof ClauseTwoArgs) {
                    predicates.add(ClauseTwoArgs.toPredicate(root, criteriaBuilder, clause));
                    return;
                }
                if (clause instanceof ClauseArrayArgs) {
                    predicates.add(ClauseArrayArgs.toPredicate(root, criteriaBuilder, clause));
                    return;
                }
                try {
                    predicates.add(Clause.toPredicate2(root,criteriaBuilder,clause));
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        if (connecteur.equals(Connecteur.Or) && predicates.size() > 0) {
            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
