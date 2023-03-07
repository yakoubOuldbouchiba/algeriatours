package com.yakoub.ea.dto;

import com.yakoub.ea.filter.entities.TourRating;
import com.yakoub.ea.enums.Difficulty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Objects;

public class RatingDto {
    @Min(value = 0, message = "Age should not be less than 0")
    @Max(value = 5, message = "Age should not be greater than 5")
    private Integer score;

    @Size(max = 255)
    private String comment;

    @NotNull
    private Integer customerId;

    private Date date;

    private Difficulty difficulty;

    public RatingDto() {
    }

    public RatingDto(Integer score, String comment, Integer customerId, Difficulty difficulty) {
        this.score = score;
        this.comment = comment;
        this.customerId = customerId;
        this.difficulty = difficulty;
    }

    public RatingDto(TourRating tourRating) {
        this.score = tourRating.getScore();
        this.comment = tourRating.getComment();
        this.customerId = tourRating.getPk().getCustomerId();
        this.date = tourRating.getDate();
        this.difficulty = tourRating.getPk().getTour().getDifficulty();
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RatingDto ratingDto = (RatingDto) o;
        return Objects.equals(score, ratingDto.score) && Objects.equals(comment, ratingDto.comment) && Objects.equals(customerId, ratingDto.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(score, comment, customerId);
    }
}
