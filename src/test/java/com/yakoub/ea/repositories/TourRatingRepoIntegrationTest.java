package com.yakoub.ea.repositories;

import com.yakoub.ea.entities.TourRating;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TourRatingRepoIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TourRatingRepository tourRatingRepository;

    @Test
    public void testFindByTourId(){
        assertEquals(1 , tourRatingRepository.findByTourId(1L).size());
    }

    @Test
    public void testFindByTourIdAndCustomerId(){
        TourRating rating = tourRatingRepository.findByTourIdAndCustomerId(1L , 4).get();
        assertNotNull(rating.getId());
        assertEquals(1 , rating.getTour().getId().longValue());
        assertEquals(4 , rating.getCustomerId().intValue());
        assertEquals(5 , rating.getScore().intValue());
        assertEquals("I loved it", rating.getComment());
    }

    @Test
    public void findByTourId(){
        Page<TourRating> page = tourRatingRepository.findByTourId(1L , PageRequest.of(1, 1));
        assertEquals(1 , page.getTotalElements());
        assertEquals(1 , page.getTotalPages());
        assertEquals(1 , page.getSize());
        assertEquals(1, page.getNumber());
    }


}
