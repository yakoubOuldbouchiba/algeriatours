package com.yakoub.ea.services;

import com.yakoub.ea.filter.entities.TourPackage;
import com.yakoub.ea.repositories.TourPackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Service
public class TourPackageService {

    @PersistenceContext
    private EntityManager entityManager;
    private final TourPackageRepository tourPackageRepository;

    @Autowired
    public TourPackageService(TourPackageRepository tourPackageRepository) {
        this.tourPackageRepository = tourPackageRepository;
    }

    public TourPackage createTourPackage(String code , String name){
        return this.tourPackageRepository.findById(code).orElse(this.tourPackageRepository.save(new TourPackage(code , name)));
    }

    public Iterable<TourPackage> lookup(){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<TourPackage> cq = cb.createQuery(TourPackage.class);
        Root<TourPackage> tourPackages = cq.from(TourPackage.class);
        cq = cq.select(tourPackages);
        //cq.where(cb.equal(tourPackages.get("code"), code));
        //cq.where(cb.equal(tourPackages.get("name"), code));
        TypedQuery<TourPackage> query = entityManager.createQuery(cq);
        return query.getResultList();
        //return this.tourPackageRepository.findAll();
    }

    public long total(){
        return this.tourPackageRepository.count();
    }
}
