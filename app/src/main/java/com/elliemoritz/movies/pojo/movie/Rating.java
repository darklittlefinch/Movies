package com.elliemoritz.movies.pojo.movie;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Rating implements Serializable {
    @SerializedName("kp")
    private String kinopoiskRating;

    public Rating(String kinopoiskRating) {
        this.kinopoiskRating = kinopoiskRating;
    }

    public String getKinopoiskRating() {
        return kinopoiskRating;
    }

    public String getFormattedKinopoiskRating() {
        return formatKinopoiskRating();
    }

    public double getKinopoiskRatingDouble() {
        return Double.parseDouble(formatKinopoiskRating());
    }

    private String formatKinopoiskRating() {
        return String.format("%.1f", Double.parseDouble(kinopoiskRating));
    }

    @Override
    public String toString() {
        return "Rating{" +
                "kp='" + kinopoiskRating + '\'' +
                '}';
    }
}
