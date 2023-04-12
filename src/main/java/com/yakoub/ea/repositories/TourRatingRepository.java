package com.yakoub.ea.repositories;

import com.yakoub.ea.entities.TourRating;
import com.yakoub.ea.entities.TourRatingPk;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface TourRatingRepository  extends CrudRepository<TourRating , Integer>  ,JpaSpecificationExecutor<TourRating>  {
    List<TourRating> findByTourId(Long tourId);
    Optional<TourRating> findByTourIdAndCustomerId(Long tourId, Integer customerId);

    Page<TourRating> findByTourId(Long tourId , Pageable pageable);

    Page<TourRating> findAll(Specification specification , Pageable pageable);
}
