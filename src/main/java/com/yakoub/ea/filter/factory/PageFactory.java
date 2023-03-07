package com.yakoub.ea.filter.factory;

import com.yakoub.ea.filter.clause.Clause;
import com.yakoub.ea.filter.enums.Connecteur;
import com.yakoub.ea.filter.specification.ConjunctionSpecification;
import com.yakoub.ea.filter.specification.GenericSpecification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


import java.util.List;

public class PageFactory<T> {

    private final JpaSpecificationExecutor<T> repository;
    private final Page<T> page;


    public PageFactory(JpaSpecificationExecutor<T> repository, List<Clause> clauses1 , Pageable pageable) {
        this.repository = repository;
        page = this.repository.findAll(new GenericSpecification<T>(clauses1, Connecteur.And) , pageable);
    }

    public PageFactory(JpaSpecificationExecutor<T> repository, List<Clause> clauses1, Connecteur connecteur , Pageable pageable) {
        this.repository = repository;
        page = this.repository.findAll(new GenericSpecification<T>(clauses1, connecteur) , pageable);
    }


    public PageFactory(JpaSpecificationExecutor<T> repository, List<Clause> clauses1, List<Clause> clauses2 , Pageable pageable) {
        this.repository = repository;
        page = this.repository.findAll(new ConjunctionSpecification<>(clauses1, clauses2) , pageable);
    }

    public Page<T> getPage() {
        return page;
    }
}
