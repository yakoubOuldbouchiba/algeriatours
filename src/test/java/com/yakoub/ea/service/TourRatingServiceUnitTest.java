package com.yakoub.ea.service;


import com.yakoub.ea.dto.RatingDto;
import com.yakoub.ea.entities.Tour;
import com.yakoub.ea.entities.TourPackage;
import com.yakoub.ea.entities.TourRating;
import com.yakoub.ea.enums.Difficulty;
import com.yakoub.ea.enums.Region;
import com.yakoub.ea.repositories.TourRatingRepository;
import com.yakoub.ea.repositories.TourRepository;
import com.yakoub.ea.services.TourRatingService;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class TourRatingServiceUnitTest {


    @Mock
    private TourRepository tourRepository;
    @Mock
    private TourRatingRepository tourRatingRepository;

    @InjectMocks
    private TourRatingService underTest;

    @Test
    public void testCreateTourRating() {
        // Given
        Long tourId = 1L;
        Integer customerId = 1;
        Integer score = 5;
        String comment = "Great tour!";
        TourPackage tourPackage = new TourPackage("BC", "Backpack Cal");
        Tour tour = new Tour("BC", "Big Sur Retreat", "Description", 750,
                "What to expect", "What to bring", "3 days", tourPackage, Region.North, Difficulty.Medium);
        TourRating tourRating = new TourRating(tour, customerId, score, comment);

        when(tourRepository.findById(tourId)).thenReturn(Optional.of(tour));
        when(tourRatingRepository.save(any(TourRating.class))).thenReturn(tourRating);

        // When
        TourRating result = underTest.createTourRating(tourId, customerId, score, comment);

        // Then
        verify(tourRatingRepository, times(1)).save(any(TourRating.class));
        assertEquals(tourRating, result);
    }


    @Test
    public void testCreateWillReturnNoSuchElementException(){
        //given
        Long tourId = 1L;
        Integer customerId = 1;
        Integer score = 5;
        String comment = "Great tour!";
        when(tourRepository.findById(any())).thenReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(()->{
            underTest.createTourRating(tourId, customerId, score, comment);
        }).isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Tour does not exit"+tourId);
        verify(tourRatingRepository , never()).save(any(TourRating.class));
    }

    @Test
    public void testRateMany(){

        //given
        Long tourId = 1L;
        Integer score = 5;
        Integer customers[] = new Integer[]{50, 60, 70 ,80 ,90};
        Tour tour = new Tour();
        tour.setId(tourId);
        given(tourRepository.findById(tourId)).willReturn(Optional.of(tour));
        //when
        underTest.rateMany(tourId,score,customers);
        //then
        verify(tourRatingRepository , times(customers.length)).save(any(TourRating.class));
    }

    @Test
    public void testRateManyTourNotFound(){
        //given
        Long tourId = 1L;
        Integer score = 5;
        Integer customers[] = new Integer[]{50, 60, 70 ,80 ,90};
        Tour tour = new Tour();
        tour.setId(tourId);
        given(tourRepository.findById(tourId)).willReturn(Optional.empty());
        //when
        underTest.rateMany(tourId,score,customers);
        //then
        verify(tourRatingRepository , never()).save(any(TourRating.class));
    }

    @Test
    public void testRateManyReturnDataIntegrityViolationException(){
        //given
        Long tourId = 1L;
        Integer score = 5;
        Integer customers[] = new Integer[]{50, 70 ,80 ,90};
        Tour tour = new Tour();
        tour.setId(tourId);
        given(tourRepository.findById(tourId)).willReturn(Optional.of(tour));
        when(tourRatingRepository.save(new TourRating(tour, 50 , score))).thenThrow(DataIntegrityViolationException.class);
        //when
        //then
        assertThatThrownBy(()->{
            underTest.rateMany(1L , score , customers);
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @Disabled
    public void testLookupRatings(){

    }

    @Test
    public void testGetAverage(){
        //given
        Long tourId = 1L;
        Tour tour = new Tour();
        tour.setId(tourId);
        List<TourRating> ratings = new ArrayList<>();
        ratings.add(new TourRating(tour, 4 , 5 ));
        ratings.add(new TourRating(tour, 5 , 4 ));
        ratings.add(new TourRating(tour, 6 , 3 ));

        given(tourRepository.findById(tourId)).willReturn(Optional.of(tour));
        given(tourRatingRepository.findByTourId(tourId)).willReturn(ratings);
        //when
        Double expected =underTest.getAverage(tourId);
        //then
        assertThat(expected).isEqualTo(4.0);
        verify(tourRatingRepository, times(1)).findByTourId(tourId);
    }

    @Test
    public void testGetAverageReturnNoSuchElementException(){
        //given
        Long tourId = 1L;
        given(tourRepository.findById(tourId)).willReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(()->{
            underTest.getAverage(tourId);
        }).isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Tour does not exit"+tourId);
    }

    @Test
    public void testGetAverageReturnNoRatings(){
        //given
        Long tourId = 1L;
        Tour tour = new Tour();
        tour.setId(tourId);
        given(tourRepository.findById(tourId)).willReturn(Optional.of(tour));
        given(tourRatingRepository.findByTourId(tourId)).willReturn(new ArrayList<>());
        //when
        //then
        assertThatThrownBy(()->{
            underTest.getAverage(tourId);
        }).isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Tour has no ratings");
    }



    @Test
    public void testUpdate(){
        //given
        Long tourId = 1L;
        String comment = "Great tour!";
        Integer score =4;
        Integer customerId=1;
        Integer newScore =5;
        String newComment = "Great";
        TourPackage tourPackage = new TourPackage("BC", "Backpack Cal");
        Tour tour = new Tour("BC", "Big Sur Retreat", "Description", 750,
                "What to expect", "What to bring", "3 days", tourPackage, Region.North, Difficulty.Medium);
        TourRating tourRating = new TourRating(tour, customerId, score, comment);
        TourRating tourRatingExpected = new TourRating(tour, customerId, newScore , newComment);
        RatingDto ratingDto = new RatingDto(newScore, newComment  , 1);
        given(tourRatingRepository.findByTourIdAndCustomerId(tourId , customerId)).willReturn(Optional.of(tourRating));
        //when
        underTest.update(tourId,ratingDto);
        //then
        ArgumentCaptor<TourRating> argumentCaptor = ArgumentCaptor.forClass(TourRating.class);
        verify(tourRatingRepository).save(argumentCaptor.capture());
        TourRating captureRating = argumentCaptor.getValue();
        assertThat(captureRating).isEqualTo(tourRatingExpected);
    }



    @Test
    public void testUpdateNoSuchElementException(){

        //given
        Long tourId = 1L;
        RatingDto ratingDto = new RatingDto(5, "testing"  , 1);
        given(tourRatingRepository.findByTourIdAndCustomerId(anyLong(), any(Integer.class))).willReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(()->{
            underTest.update(tourId, ratingDto);
        }).isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Rating created by");


    }

    @Test
    public void testUpdateSome(){

        //given
        Long tourId = 1L;
        String comment = "Great tour!";
        Integer score =4;
        Integer customerId=1;
        Integer newScore =5;
        TourPackage tourPackage = new TourPackage("BC", "Backpack Cal");
        Tour tour = new Tour("BC", "Big Sur Retreat", "Description", 750,
                "What to expect", "What to bring", "3 days", tourPackage, Region.North, Difficulty.Medium);
        TourRating tourRating = new TourRating(tour, customerId, score, comment);
        TourRating tourRatingExpected = new TourRating(tour, customerId, newScore , comment);
        RatingDto ratingDto = new RatingDto(newScore, comment  , 1);
        given(tourRatingRepository.findByTourIdAndCustomerId(tourId , customerId)).willReturn(Optional.of(tourRating));
        //when
        underTest.updateSome(tourId,ratingDto);
        //then
        ArgumentCaptor<TourRating> argumentCaptor = ArgumentCaptor.forClass(TourRating.class);
        verify(tourRatingRepository).save(argumentCaptor.capture());
        TourRating captureRating = argumentCaptor.getValue();
        assertThat(captureRating).isEqualTo(tourRatingExpected);

    }


    @Test
    public void testUpdateSomeNoSuchElementException(){
        //given
        Long tourId = 1L;
        RatingDto ratingDto = new RatingDto(5, "testing"  , 1);
        given(tourRatingRepository.findByTourIdAndCustomerId(anyLong(), any(Integer.class))).willReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(()->{
            underTest.updateSome(tourId, ratingDto);
        }).isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Rating created by");
    }

    @Test
    public void testDelete(){

        //given
        Long tourId = 1L;
        Integer customerId= 1;
        String comment = "Great tour!";
        Integer score =4;
        TourPackage tourPackage = new TourPackage("BC", "Backpack Cal");
        Tour tour = new Tour("BC", "Big Sur Retreat", "Description", 750,
                "What to expect", "What to bring", "3 days", tourPackage, Region.North, Difficulty.Medium);
        TourRating tourRating = new TourRating(tour, customerId, score, comment);
        given(tourRatingRepository.findByTourIdAndCustomerId(tourId , customerId)).willReturn(Optional.of(tourRating));
        //when
        underTest.delete(tourId, customerId);
        //then
        ArgumentCaptor<TourRating> argumentCaptor = ArgumentCaptor.forClass(TourRating.class);
        verify(tourRatingRepository).delete(argumentCaptor.capture());
        TourRating expected = argumentCaptor.getValue();
        assertThat(expected).isEqualTo(tourRating);


    }

    @Test
    public void testDeleteNoSuchElementException(){
        //given
        Long tourId = 1L;
        Integer customerId= 1;

        given(tourRatingRepository.findByTourIdAndCustomerId(anyLong(), anyInt())).willReturn(Optional.empty());
        //when
        //then
        assertThatThrownBy(()->{
            underTest.delete(tourId, customerId);
        }).isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Rating created by");

    }


}
