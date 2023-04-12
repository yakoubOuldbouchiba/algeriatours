package com.yakoub.ea.filter.specification;

import com.yakoub.ea.filter.clause.Clause;
import com.yakoub.ea.filter.enums.Connecteur;
import com.yakoub.ea.filter.factory.ListClauseFactory;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class DisjunctionSpecification<T> implements Specification<T> {
    List<Clause> filters = new ArrayList<>();

    List<Clause> orFilters = new ArrayList<>();

    public DisjunctionSpecification(List<Clause> filters , List<Clause> orFilters) {
        this.filters = filters;
        this.orFilters = orFilters;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Predicate predicate1 =new GenericSpecification<T>(filters , Connecteur.And).toPredicate(root,query, criteriaBuilder);
        Predicate predicate2 =new GenericSpecification<T>(orFilters , Connecteur.Or).toPredicate(root,query, criteriaBuilder);
        return criteriaBuilder.or(predicate1 , predicate2);
    }
}
