package com.yakoub.ea.dto;

import com.yakoub.ea.filter.entities.Tour;

public class TourDto {

    private Long id;
    private String title;
    private String description;

    private String blurb;

    private Integer price;

    private String duration;

    private String bullets;

    private String keywords;

    public TourDto() {
    }

    public TourDto(Tour tour) {
         id = tour.getId();
         title = tour.getTitle();
         description = tour.getDescription();
         blurb = tour.getBlurb();
         price = tour.getPrice();
         duration = tour.getDuration();
         bullets = tour.getBullets();
         keywords = tour.getKeywords();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBlurb() {
        return blurb;
    }

    public void setBlurb(String blurb) {
        this.blurb = blurb;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getBullets() {
        return bullets;
    }

    public void setBullets(String bullets) {
        this.bullets = bullets;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
}
