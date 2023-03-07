package com.yakoub.ea.filter.specification;

import com.yakoub.ea.filter.entities.Tour;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class TourSpecification implements Specification<Tour> {

    private String id;

    private String price ;

    public TourSpecification(String id, String price) {
        this.id = id;
        this.price = price;
    }


    @Override
    public Predicate toPredicate(Root<Tour> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicateList = new ArrayList<>();
        predicateList.add(criteriaBuilder.equal(root.get("id"), id));
        predicateList.add(criteriaBuilder.lessThan(root.get("price"), price));
        return  criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
    }
}
