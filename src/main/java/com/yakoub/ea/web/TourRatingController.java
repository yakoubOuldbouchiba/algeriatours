package com.yakoub.ea.web;


import com.yakoub.ea.dto.RatingDto;

import com.yakoub.ea.filters.clause.Clause;
import com.yakoub.ea.filters.handlerMethodArgumentResolver.Critiria;
import com.yakoub.ea.services.TourRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping(path = "/tours/{tourId}/ratings")
public class TourRatingController {

    private final TourRatingService tourRatingService;

    @Autowired
    public TourRatingController(TourRatingService tourRatingService) {
        this.tourRatingService = tourRatingService;
    }



    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createTourRating(@PathVariable(value = "tourId") Long tourId,
                                 @RequestBody @Validated RatingDto ratingDto){
        this.tourRatingService.createTourRating(tourId , ratingDto.getCustomerId() , ratingDto.getScore() , ratingDto.getComment());

    }

    @PostMapping("/{score}")
    @ResponseStatus(HttpStatus.CREATED)
    public void createManyTourRatings(@PathVariable(value = "tourId") Long tourId,
                                      @PathVariable(value = "score") Integer score,
                                      @RequestParam("customers") Integer customers[]) {
        tourRatingService.rateMany(tourId, score, customers);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<RatingDto> findRatings(@PathVariable(value = "tourId") Long tourId , @Critiria List<Clause> filters1 , Pageable pageable){

        return tourRatingService.lookupRatings(tourId , filters1  , pageable);
    }

    @GetMapping(path = "/average")
    @ResponseStatus(HttpStatus.OK)
    public Map<String , Double> getAverage(@PathVariable(value = "tourId") Long tourId){

        return Map.of("average", tourRatingService.getAverage(tourId));
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public RatingDto updateWithPut(@PathVariable(value = "tourId") Long tourId ,@RequestBody @Validated RatingDto ratingDto){

        return  new RatingDto(tourRatingService.update(tourId , ratingDto));
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    public RatingDto updateWithPatch(@PathVariable(value = "tourId") Long tourId ,@RequestBody RatingDto ratingDto){
        return  new RatingDto(tourRatingService.updateSome(tourId , ratingDto));
    }



    @DeleteMapping(path = "/{customerId}")
    @ResponseStatus(HttpStatus.NON_AUTHORITATIVE_INFORMATION)
    public void delete (@PathVariable(value = "tourId")Long tourId ,
                        @PathVariable(value = "customerId")Integer customerId){
        tourRatingService.delete(tourId, customerId);
    }



    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public String return404(NoSuchElementException ex){
        return ex.getMessage();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public String return500(DataIntegrityViolationException ex){
        return ex.getMessage();
    }
}
