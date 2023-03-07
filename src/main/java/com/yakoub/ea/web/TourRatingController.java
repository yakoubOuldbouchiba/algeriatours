package com.yakoub.ea.web;


import com.yakoub.ea.dto.RatingDto;
import com.yakoub.ea.dto.TourDto;
import com.yakoub.ea.filter.clause.Clause;
import com.yakoub.ea.filter.entities.Tour;
import com.yakoub.ea.filter.entities.TourRating;
import com.yakoub.ea.filter.entities.TourRatingPk;
import com.yakoub.ea.filter.enums.Connecteur;
import com.yakoub.ea.filter.factory.ListClauseFactory;
import com.yakoub.ea.filter.factory.PageFactory;
import com.yakoub.ea.filter.specification.ConjunctionSpecification;
import com.yakoub.ea.handlerMethodArgumentResolver.FilterParams;
import com.yakoub.ea.handlerMethodArgumentResolver.OrFilterParams;
import com.yakoub.ea.repositories.TourRatingRepository;
import com.yakoub.ea.repositories.TourRepository;
import com.yakoub.ea.services.TourService;
import com.yakoub.ea.filter.specification.GenericSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping(path = "/tours/{tourId}/ratings")
public class TourRatingController {

    @Autowired
    private TourService tourService;
    TourRatingRepository tourRatingRepository;
    TourRepository tourRepository;

    @Autowired
    public TourRatingController(TourRatingRepository tourRatingRepository, TourRepository tourRepository) {
        this.tourRatingRepository = tourRatingRepository;
        this.tourRepository = tourRepository;
    }

    public TourRatingController() {
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createTourRating(@PathVariable(value = "tourId") int tourId,
                                 @RequestBody @Validated RatingDto ratingDto){
        System.out.println("tourId : "+tourId);
        Tour tour = verfiyTour((long) tourId);
        tourRatingRepository.save(new TourRating(new TourRatingPk(tour,ratingDto.getCustomerId()), ratingDto.getScore(), ratingDto.getComment()));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<RatingDto> getAllRatingsForTour(@PathVariable(value = "tourId") int tourId , @FilterParams List<Clause> filters1 , @OrFilterParams List<Clause> filters2, Pageable pageable){

        verfiyTour((long) tourId);
        Page<TourRating> ratings;
        if(filters1!=null && filters2 != null && !filters1.isEmpty() && !filters2.isEmpty()){
            System.out.println("filter1 size : "+filters1.size());
            System.out.println("filter2 size : "+filters2.size());
            ratings = (new PageFactory<TourRating>(tourRatingRepository,filters1, filters2, pageable)).getPage();
        }
        else if(filters2 != null){
            ratings = (new PageFactory<TourRating>(tourRatingRepository , filters2, Connecteur.Or , pageable)).getPage();
        }else {
            ratings = (new PageFactory<TourRating>(tourRatingRepository , filters1, Connecteur.And , pageable)).getPage();
        }
        return new PageImpl<>(
                ratings.get().map(RatingDto::new).collect(Collectors.toList()),
                pageable,
                ratings.getTotalElements()
        );
    }

    @GetMapping(path = "/tours")
    @ResponseStatus(HttpStatus.OK)
    public List<TourDto> findByTourPackageCode (@PathVariable(value = "tourId") String tourId){
        return  tourService.findByTours(tourId).stream().map(tour -> new TourDto(tour)).collect(Collectors.toList());
    }


    @GetMapping(path = "/average")
    @ResponseStatus(HttpStatus.OK)
    public Map<String , Double> getAverage(@PathVariable(value = "tourId") int tourId){
        Tour tour = verfiyTour((long) tourId);
        return Map.of("average", tourRatingRepository.findByPkTourId((long) tourId).stream().mapToInt(TourRating::getScore).average().orElseThrow(()->new NoSuchElementException("Tour has no ratings")));
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public RatingDto updateWithPut(@PathVariable(value = "tourId") int tourId ,@RequestBody @Validated RatingDto ratingDto){
        TourRating tourRating = verfiyTour((long) tourId , ratingDto.getCustomerId());
        tourRating.setScore(ratingDto.getScore());
        tourRating.setComment(ratingDto.getComment());
        return  new RatingDto(tourRatingRepository.save(tourRating));
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public RatingDto updateWithPatch(@PathVariable(value = "tourId") int tourId ,@RequestBody @Validated RatingDto ratingDto){
        TourRating tourRating = verfiyTour((long) tourId , ratingDto.getCustomerId());
        if(ratingDto.getScore()  != null){
            tourRating.setScore(ratingDto.getScore());
        }
        if(ratingDto.getComment() != null){
            tourRating.setComment(ratingDto.getComment());
        }
        return  new RatingDto(tourRatingRepository.save(tourRating));
    }

    @DeleteMapping(path = "/{customerId}")
    @ResponseStatus(HttpStatus.NON_AUTHORITATIVE_INFORMATION)
    public void delete (@PathVariable(value = "tourId")int tourId ,
                        @PathVariable(value = "customerId")int customerId){
        TourRating tourRating = verfiyTour((long) tourId, customerId);
        tourRatingRepository.delete(tourRating);
    }

    private Tour verfiyTour (Long tourId) throws NoSuchElementException{
        return tourRepository.findById(tourId).orElseThrow(()->new NoSuchElementException("Tour does not exit"+tourId));
    }
    private TourRating verfiyTour (Long tourId , Integer customerId) throws NoSuchElementException{
        return tourRatingRepository.findByPkTourIdAndPkCustomerId(tourId, customerId).orElseThrow(()->new NoSuchElementException("Rating created by "+customerId+" for tour "+tourId+" does not exit"));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public String return404(NoSuchElementException ex){
        return ex.getMessage();
    }
}
