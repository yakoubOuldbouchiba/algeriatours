package com.yakoub.ea.services;


import com.yakoub.ea.filter.entities.Tour;
import com.yakoub.ea.filter.entities.TourPackage;
import com.yakoub.ea.enums.Difficulty;
import com.yakoub.ea.enums.Region;
import com.yakoub.ea.repositories.TourPackageRepository;
import com.yakoub.ea.repositories.TourRepository;
import com.yakoub.ea.filter.specification.TourSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import javax.persistence.EntityManager;
import java.util.List;

@Service
public class TourService {

    @Autowired
    private EntityManager em;
    public final TourRepository tourRepository;
    public final TourPackageRepository tourPackageRepository;

    @Autowired
    public TourService(TourRepository tourRepository, TourPackageRepository tourPackageRepository) {
        this.tourRepository = tourRepository;
        this.tourPackageRepository = tourPackageRepository;
    }


    public Tour createTour(String title, String description, String blurb , Integer price, String duration , String bullets , String keywords, String tourPackageName , Region region, Difficulty difficulty){
        TourPackage tourPackage = this.tourPackageRepository.findByName(tourPackageName).orElseThrow(()->{
            return new RuntimeException("Tour package doesn't existe");
        });
        return tourRepository.save(new Tour(title, description, blurb, price, duration, bullets, keywords, tourPackage, region, difficulty));
    }

    public long total(){
        return tourRepository.count();
    }

    public List<Tour> findByTours(String tourId){
        Specification<Tour> tourSpecification =  new TourSpecification(tourId , "250");
        return tourRepository.findAll(tourSpecification);
    };

}
