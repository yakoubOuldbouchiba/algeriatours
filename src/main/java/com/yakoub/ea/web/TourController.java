package com.yakoub.ea.web;

import com.yakoub.ea.dto.TourDto;
import com.yakoub.ea.filter.entities.Tour;
import com.yakoub.ea.filter.enums.Connecteur;
import com.yakoub.ea.filter.factory.ListClauseFactory;
import com.yakoub.ea.filter.factory.PageFactory;
import com.yakoub.ea.services.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping(path = "/mytours")
public class TourController {
    private TourService tourService;

    @Autowired
    public TourController(TourService tourService) {
        this.tourService = tourService;
    }


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<TourDto> getTours(@RequestParam(value="filter" , required = false) List<String> filters1 , @RequestParam(value="orFilter" , required = false) List<String> filters2 , Pageable pageable){
        Page<Tour> tours;
        if(filters1!=null && filters2 != null && !filters1.isEmpty() && !filters2.isEmpty()){
          tours = (new PageFactory<Tour>(tourService.tourRepository, ListClauseFactory.getClauses(filters1) , ListClauseFactory.getClauses(filters2) , pageable)).getPage();
        }
        else if(filters2 != null){
                tours = (new PageFactory<Tour>(tourService.tourRepository , ListClauseFactory.getClauses(filters2), Connecteur.Or , pageable)).getPage();
        }else {
            tours = (new PageFactory<Tour>(tourService.tourRepository , ListClauseFactory.getClauses(filters1), Connecteur.And , pageable)).getPage();
        }
        return new PageImpl<>(
                    tours.get().map(TourDto::new).collect(Collectors.toList()),
                    pageable,
                    tours.getTotalElements()
        );

    }
}
