package com.yakoub.ea.filters.specification;



import com.yakoub.ea.filters.clause.Clause;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;

import java.util.List;
import java.util.stream.Collectors;




public class DxFilterSpecification<T> implements Specification<T> {

    private List<Clause> filters;
    
    private  List<List<Clause>> clauses;

    public DxFilterSpecification(List<Clause> filters, List<List<Clause>> clauses) {
        this.filters = filters;
        this.clauses = clauses;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Predicate predicate1 =new GenericSpecification<T>(filters).toPredicate(root,query, criteriaBuilder);
        List<Predicate>  predicates = clauses.stream().map(clauses1 -> new GenericSpecification<T>(clauses1).toPredicate(root,query, criteriaBuilder)).collect(Collectors.toList());
        query.distinct(true);
        return criteriaBuilder.and(predicate1 , criteriaBuilder.or(predicates.toArray(new Predicate[0])));
    }
}
