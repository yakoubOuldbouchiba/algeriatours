package com.yakoub.ea.controller;

import com.yakoub.ea.dto.RatingDto;
import com.yakoub.ea.entities.TourRating;
import com.yakoub.ea.repositories.TourRatingRepository;
import com.yakoub.ea.web.TourRatingController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment =  SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class ToutRatingControllerIntegrationTest {

    @Autowired
    private TourRatingController tourRatingController;


    @Autowired
    private TourRatingRepository tourRatingRepository;

    @Test
    @Rollback
    public void testCreateTourRating(){
        Long count = tourRatingRepository.count();
        RatingDto ratingDto = new RatingDto(3 , "Normal" , 80);
        tourRatingController.createTourRating(1L , ratingDto);
        assertEquals(count+1 ,tourRatingRepository.count() );

        assertThrows(NoSuchElementException.class, ()->{
            tourRatingController.createTourRating(50L , ratingDto);
        });
    }

    @Test
    @Rollback
    public void testCreateManyTourRatings(){
        Long count = tourRatingRepository.count();
        Integer customers[] = new Integer[]{60,70,80,90};
        tourRatingController.createManyTourRatings(1L, 4 , new Integer[]{60,70,80,90});
        assertEquals(count+customers.length , tourRatingRepository.count());

    }


    @Test
    @Rollback
    public void testGetAverage(){
        assertEquals(Map.of("average", 5.0) , tourRatingController.getAverage(1L));
    }

    @Test
    @Rollback
    public void testUpdateWithPut(){
        RatingDto ratingDto = new RatingDto(3 ,"Nice" , 4);
        RatingDto dto = tourRatingController.updateWithPut(1L , ratingDto);
        assertEquals(3 , dto.getScore().intValue());
        assertEquals("Nice", dto.getComment());
        assertEquals(4, dto.getCustomerId().intValue());
    }

    @Test
    @Rollback
    public void testUpdateWithPatch(){
        TourRating tourRating = tourRatingRepository.findByTourIdAndCustomerId(1L , 4).get();

        RatingDto ratingDto = new RatingDto(null ,"Very Nice" , 4);
        RatingDto dto = tourRatingController.updateWithPut(1L , ratingDto);
        assertEquals(tourRating.getScore() , dto.getScore());
        assertEquals("Very Nice", dto.getComment());
        assertEquals(4, dto.getCustomerId().intValue());
    }

    @Test
    @Rollback
    public void testDelete(){
        tourRatingController.delete(1L , 4);
        assertEquals(1 , tourRatingRepository.count());
    }
}
