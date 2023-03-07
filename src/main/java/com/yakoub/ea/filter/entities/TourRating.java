package com.yakoub.ea.filter.entities;



import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.util.Date;
import java.util.Objects;


@Entity
public class TourRating {

    @EmbeddedId
    private TourRatingPk pk;


    @Column(nullable = false)
    private Integer score;

    @Column(length = 255)
    private  String comment;

    @Column(columnDefinition = "date default '2020-01-01'")
    private Date date;

    public TourRating() {
    }

    public TourRating(TourRatingPk pk, Integer score, String comment) {
        this.pk = pk;
        this.score = score;
        this.comment = comment;
    }

    public TourRatingPk getPk() {
        return pk;
    }

    public void setPk(TourRatingPk pk) {
        this.pk = pk;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TourRating that = (TourRating) o;
        return Objects.equals(pk, that.pk) && Objects.equals(score, that.score) && Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pk, score, comment);
    }
}
