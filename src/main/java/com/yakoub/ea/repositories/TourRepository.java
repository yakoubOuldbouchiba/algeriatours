package com.yakoub.ea.repositories;

import com.yakoub.ea.entities.Tour;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TourRepository  extends PagingAndSortingRepository<Tour , Long> ,JpaSpecificationExecutor<Tour> {


    Optional<Tour> findById(int tourId);

    Page<Tour> findByTourPackageCode(@Param("code") String code , Pageable pageable);

    Page<Tour> findAll(Specification specification, Pageable pageable);

}
