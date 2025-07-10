package com.yakoub.ea.web;

import com.yakoub.ea.dto.TourDto;
import com.yakoub.ea.entities.Tour;
import com.yakoub.ea.filters.clause.Clause;
import com.yakoub.ea.filters.handlerMethodArgumentResolver.Critiria;
import com.yakoub.ea.filters.specification.GenericSpecification;
import com.yakoub.ea.repositories.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping(path = "/mytours")
public class TourController {
    private TourRepository tourRepository;

    @Autowired
    public TourController(TourRepository tourRepository) {
        this.tourRepository = tourRepository;
    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<TourDto> getTours(@Critiria List<Clause> filters  , Pageable pageable){
        Specification<Tour> specification = new GenericSpecification<>(filters);
        Page<Tour> tours = tourRepository.findAll(specification , pageable);
        return new PageImpl<>(
                    tours.get().map(TourDto::new).collect(Collectors.toList()),
                    pageable,
                    tours.getTotalElements()
        );

    }
}
