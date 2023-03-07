package com.yakoub.ea.repositories;

import com.yakoub.ea.filter.entities.TourRating;
import com.yakoub.ea.filter.entities.TourRatingPk;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface TourRatingRepository  extends CrudRepository<TourRating , TourRatingPk>  ,JpaSpecificationExecutor<TourRating>  {
    List<TourRating> findByPkTourId(Long tourId);
    Optional<TourRating> findByPkTourIdAndPkCustomerId(Long tourId, Integer customerId);

    Page<TourRating> findByPkTourId(Long tourId , Pageable pageable);

    Page<TourRating> findAll(Specification specification , Pageable pageable);
}
