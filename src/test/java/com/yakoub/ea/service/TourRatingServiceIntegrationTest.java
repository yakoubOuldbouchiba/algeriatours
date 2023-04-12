package com.yakoub.ea.service;


import com.yakoub.ea.dto.RatingDto;
import com.yakoub.ea.entities.TourRating;
import com.yakoub.ea.repositories.TourRatingRepository;
import com.yakoub.ea.services.TourRatingService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class TourRatingServiceIntegrationTest {


    @Autowired
    private TourRatingService tourRatingService;


    @Autowired
    private TourRatingRepository tourRatingRepository;

    @Rollback
    @Test
    public void testCreateTourRating(){
        TourRating tourRating = tourRatingService.createTourRating(1L , 26 , 5 , "it is great");
        assertNotNull(tourRating.getId());
        assertEquals(1L , tourRating.getTour().getId());
        assertEquals(26, tourRating.getCustomerId());
        assertEquals(5, tourRating.getScore());
        assertEquals("it is great", tourRating.getComment());
    }

    @Rollback
    @Test
    public void testRateMany(){
        Long count = tourRatingRepository.count();
        tourRatingService.rateMany(1L , 26 , new Integer[]{30, 40, 50, 60});
        assertEquals(count+4 , tourRatingRepository.count());
    }

    @Rollback
    @Test
    public void testRateManyTourNotFound(){

        Long count = tourRatingRepository.count();
        tourRatingService.rateMany(100L , 26 , new Integer[]{30, 40, 50, 60});
        assertEquals(count , tourRatingRepository.count());

    }

    @Rollback
    @Test
    public void testRateManReturnDataIntegrityViolationExceptiony(){
        assertThatThrownBy(()->{
            tourRatingService.rateMany(1L , 26 , new Integer[]{40, 40, 50, 60});
        }).isInstanceOf(DataIntegrityViolationException.class);

    }

    @Test
    public void testGetAverage(){
        Double result = tourRatingService.getAverage(1L);
        assertEquals(5 , result);
    }

    @Rollback
    @Test
    public void testUpdate(){
        TourRating tourRating = tourRatingService.update(1L , new RatingDto(4, "Great",4 ));
        assertEquals(1 , tourRating.getId());
        assertEquals(4 , tourRating.getScore());
        assertEquals("Great" ,tourRating.getComment());
        assertEquals(4, tourRating.getCustomerId());
    }

    @Rollback
    @Test
    public void testUpdateSome(){
        TourRating oldTourRating =tourRatingRepository.findByTourIdAndCustomerId(1L , 4).get();
        TourRating newTourRating = tourRatingService.updateSome(1L , new RatingDto(4, "Nice",4 ));
        assertEquals(1 , newTourRating.getId());
        assertEquals("Nice" ,newTourRating.getComment());
        assertEquals(4 , oldTourRating.getScore());
        assertEquals(4, oldTourRating.getCustomerId());
    }

    @Test
    public void testDelete(){
        Long count = tourRatingRepository.count();
        assertThrows(NoSuchElementException.class  , ()->{
            tourRatingService.delete(1L , 154);
        });
        tourRatingService.delete(1L , 4);
        assertEquals(count-1, tourRatingRepository.count());
    }
}
