package com.yakoub.ea.services;

import com.yakoub.ea.dto.RatingDto;
import com.yakoub.ea.entities.Tour;
import com.yakoub.ea.entities.TourRating;
import com.yakoub.ea.filter.clause.Clause;
import com.yakoub.ea.filter.enums.Connecteur;
import com.yakoub.ea.filter.factory.PageFactory;
import com.yakoub.ea.repositories.TourRatingRepository;
import com.yakoub.ea.repositories.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;



import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class TourRatingService {
    private  final TourRatingRepository tourRatingRepository;
    private  final TourRepository tourRepository;

    @Autowired
    public TourRatingService(TourRatingRepository tourRatingRepository, TourRepository tourRepository) {
        this.tourRatingRepository = tourRatingRepository;
        this.tourRepository = tourRepository;
    }

    public TourRating createTourRating(Long tourId, Integer customerId, Integer score, String comment){
        return tourRatingRepository.save(new TourRating(verfiyTour(tourId), customerId,
                score, comment));
    }


    @Transactional
    public void rateMany(Long tourId, Integer score, Integer customers[])  throws DataIntegrityViolationException {
        tourRepository.findById(tourId).ifPresent(tour -> {
            for (Integer c : customers) {
                tourRatingRepository.save(new TourRating(tour, c, score));
            }
        });
    }

    public Page<RatingDto> lookupRatings(Long tourId ,  List<Clause> filters1 , List<Clause> filters2, Pageable pageable){

        verfiyTour(tourId);
        Page<TourRating> ratings;
        if(filters1!=null && filters2 != null && !filters1.isEmpty() && !filters2.isEmpty()){
            ratings = (new PageFactory<>(tourRatingRepository,filters1, filters2, pageable)).getPage();
        }
        else if(filters2 != null){
            ratings = (new PageFactory<>(tourRatingRepository , filters2, Connecteur.Or , pageable)).getPage();
        }else {
            ratings = (new PageFactory<>(tourRatingRepository , filters1, Connecteur.And , pageable)).getPage();
        }
        return new PageImpl<>(
                ratings.get().map(RatingDto::new).collect(Collectors.toList()),
                pageable,
                ratings.getTotalElements()
        );
    }

    public Double getAverage(Long tourId){
        Tour tour = verfiyTour( tourId);
        return  tourRatingRepository.findByTourId((long) tourId).stream().mapToInt(TourRating::getScore).average().orElseThrow(()->new NoSuchElementException("Tour has no ratings"));
    }

    public TourRating update( Long tourId ,RatingDto ratingDto){
        TourRating tourRating = verfiyTourRating(tourId , ratingDto.getCustomerId());
        tourRating.setScore(ratingDto.getScore());
        tourRating.setComment(ratingDto.getComment());
        return tourRatingRepository.save(tourRating);
    }

    public TourRating updateSome(Long tourId , RatingDto ratingDto){
        TourRating tourRating = verfiyTourRating((long) tourId , ratingDto.getCustomerId());
        if(ratingDto.getScore()  != null){
            tourRating.setScore(ratingDto.getScore());
        }
        if(ratingDto.getComment() != null){
            tourRating.setComment(ratingDto.getComment());
        }
        return  tourRatingRepository.save(tourRating);
    }

    public void delete (Long tourId , Integer customerId) throws NoSuchElementException{
        TourRating tourRating = verfiyTourRating( tourId, customerId);
        tourRatingRepository.delete(tourRating);
    }

    private Tour verfiyTour (Long tourId) throws NoSuchElementException {
        return tourRepository.findById(tourId).orElseThrow(()->new NoSuchElementException("Tour does not exit"+tourId));
    }

    private TourRating verfiyTourRating (Long tourId , Integer customerId) throws NoSuchElementException{
        return tourRatingRepository.findByTourIdAndCustomerId(tourId, customerId).orElseThrow(()->new NoSuchElementException("Rating created by "+customerId+" for tour "+tourId+" does not exit"));
    }
}
