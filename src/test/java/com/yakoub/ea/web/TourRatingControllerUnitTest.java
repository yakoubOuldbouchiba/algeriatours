package com.yakoub.ea.web;

import com.yakoub.ea.dto.RatingDto;
import com.yakoub.ea.entities.Tour;
import com.yakoub.ea.entities.TourRating;
import com.yakoub.ea.services.TourRatingService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;


import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitConfig
@WebMvcTest(TourRatingController.class)
class TourRatingControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TourRatingService tourRatingService;


    // @Before
//    public void setUp() throws Exception {
//        //underTest  = new TourRatingController( tourRatingService);
//        //MockitoAnnotations.initMocks(this);
//    }

    @Test
    void testCreateTourRating() throws Exception {

        // given
        Long tourId = 1L;
        RatingDto ratingDto = new RatingDto();
        ratingDto.setCustomerId(1);
        ratingDto.setScore(5);
        ratingDto.setComment("Great tour!");


        // when
        mockMvc.perform(post("/tours/{tourId}/ratings", tourId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerId\":1,\"score\":5,\"comment\":\"Great tour!\"}"))
                .andExpect(status().isCreated());

        // then
        // verify that tourRatingService.createTourRating() is called with the correct arguments
        verify(tourRatingService, times(1)).createTourRating(eq(tourId), eq(ratingDto.getCustomerId()), eq(ratingDto.getScore()), eq(ratingDto.getComment()));
    }

    @Test
    void testCreateTourRatingThrowsExecption() throws Exception {

        // given
        Long tourId = 1L;
        RatingDto ratingDto = new RatingDto();
        ratingDto.setCustomerId(1);
        ratingDto.setScore(5);
        ratingDto.setComment("Great tour!");
        given(tourRatingService.createTourRating(anyLong(), anyInt(), anyInt(), anyString())).willThrow(NoSuchElementException.class);

        // when
        mockMvc.perform(post("/tours/{tourId}/ratings", tourId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerId\":1,\"score\":5,\"comment\":\"Great tour!\"}"))
                .andExpect(status().isNotFound());

        // then
        // verify that tourRatingService.createTourRating() is called with the correct arguments
        verify(tourRatingService, times(1)).createTourRating(eq(tourId), eq(ratingDto.getCustomerId()), eq(ratingDto.getScore()), eq(ratingDto.getComment()));
    }

    @Test
    void testCreateManyTourRatings() throws Exception {
        Long tourId = 1L;
        Integer score = 5;
        Integer customers[] = new Integer[]{1, 2, 3, 5};


        mockMvc.perform(post("/tours/{tourId}/ratings/{score}", tourId, score).param("customers", "1,2,3,5"))
                .andExpect(status().isCreated());

        verify(tourRatingService, times(1)).rateMany(tourId, score, customers);
    }


    @Test
    @Disabled
    void findRatings() {
    }

    @Test
    void testGetAverage() throws Exception {
        //given
        //when
        mockMvc.perform(get("/tours/{tourId}/ratings/average", anyLong())).andExpect(status().isOk());
        //then
        verify(tourRatingService, times(1)).getAverage(anyLong());
    }

    @Test
    void testGetAverageNoSuchElementException() throws Exception {
        //given
        given(tourRatingService.getAverage(anyLong())).willThrow(NoSuchElementException.class);
        //when
        mockMvc.perform(get("/tours/{tourId}/ratings/average", anyLong())).andExpect(status().isNotFound());
        //then
        verify(tourRatingService, times(1)).getAverage(anyLong());
    }

    @Test
    void testUpdateWithPut() throws Exception {
        //given
        Long tourId = 1L;
        RatingDto dto = new RatingDto(5, "Great tour!", 3);
        TourRating rating= new TourRating(new Tour() , 5, 3 , "Grear tour!");
        given(tourRatingService.update( anyLong(), any(RatingDto.class))).willReturn(rating);
        //when
        mockMvc.perform(put("/tours/{tourId}/ratings", tourId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerId\":3,\"score\":5,\"comment\":\"Great tour!\"}"))
                .andExpect(status().isOk());
        //then
        verify(tourRatingService, times(1)).update(eq(tourId),eq(dto) );
    }

    @Test
    void testUpdateWithPutNoSuchElementException() throws Exception {
        //given
        Long tourId = 1L;
        RatingDto dto = new RatingDto(5, "Great tour!", 3);
        TourRating rating= new TourRating(new Tour() , 5, 3 , "Grear tour!");
        given(tourRatingService.update( anyLong(), any(RatingDto.class))).willThrow(NoSuchElementException.class);
        //when
        mockMvc.perform(put("/tours/{tourId}/ratings", tourId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerId\":3,\"score\":5,\"comment\":\"Great tour!\"}"))
                .andExpect(status().isNotFound());
        //then
        verify(tourRatingService, times(1)).update(eq(tourId),eq(dto) );
    }

    @Test
    void testUpdateWithPatch() throws Exception {
        //given
        Long tourId = 1L;
        RatingDto dto = new RatingDto( null,"Great tour!", null);
        TourRating rating= new TourRating(new Tour() , 5, 3 , "Grear tour!");
        given(tourRatingService.updateSome( anyLong(), any(RatingDto.class))).willReturn(rating);
        //when
        mockMvc.perform(patch("/tours/{tourId}/ratings", tourId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"comment\":\"Great tour!\"}"))
                .andExpect(status().isOk());
        //then
        verify(tourRatingService, times(1)).updateSome(eq(tourId),eq(dto) );
    }

    @Test
    void testUpdateWithPatchNoSuchElementException() throws Exception {
        //given
        Long tourId = 1L;
        RatingDto dto = new RatingDto( null,"Great tour!", null);
        TourRating rating= new TourRating(new Tour() , 5, 3 , "Grear tour!");
        given(tourRatingService.updateSome( anyLong(), any(RatingDto.class))).willThrow(NoSuchElementException.class);
        //when
        mockMvc.perform(patch("/tours/{tourId}/ratings", tourId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"comment\":\"Great tour!\"}"))
                .andExpect(status().isNotFound());
        //then
        verify(tourRatingService, times(1)).updateSome(eq(tourId),eq(dto) );
    }

    @Test
    void testDelete() throws Exception{
        //test
        Long tourId = 1L;
        Integer customerId =1;
        //when
        mockMvc.perform(delete("/tours/{tourId}/ratings/{customerId}", tourId ,customerId))
                .andExpect(status().isNonAuthoritativeInformation());
        //then
        verify(tourRatingService , times(1)).delete(tourId, customerId);
    }

    @Test
    void testDeleteNoSuchElementException() throws Exception{
        //test
        Long tourId = 1L;
        Integer customerId =1;
        doThrow(new NoSuchElementException()).when(tourRatingService).delete(tourId, customerId);
        //when
        mockMvc.perform(delete("/tours/{tourId}/ratings/{customerId}", tourId ,customerId))
                .andExpect(status().isNotFound());
        //then
        verify(tourRatingService , times(1)).delete(tourId, customerId);
    }
}