package com.hamstechonline.editprofile.datamodel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MentorResponse {

    @SerializedName("occupations")
    private List<Occupation> occupations = null;
    @SerializedName("cities")
    private List<City> cities = null;
    @SerializedName("whyhamstech")
    private List<Whyhamstech> whyhamstech = null;
    @SerializedName("languages")
    private List<Language> languages = null;

    @SerializedName("lang")
    private String lang;

    public MentorResponse(String lang) {
        this.lang = lang;
    }

    public List<Occupation> getOccupations() {
        return occupations;
    }

    public void setOccupations(List<Occupation> occupations) {
        this.occupations = occupations;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    public List<Whyhamstech> getWhyhamstech() {
        return whyhamstech;
    }

    public void setWhyhamstech(List<Whyhamstech> whyhamstech) {
        this.whyhamstech = whyhamstech;
    }

    public List<Language> getLanguages() {
        return languages;
    }

    public void setLanguages(List<Language> languages) {
        this.languages = languages;
    }
}
