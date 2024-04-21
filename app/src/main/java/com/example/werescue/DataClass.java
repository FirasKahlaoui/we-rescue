package com.example.werescue;

import java.io.Serializable;

public class DataClass implements Serializable {
    private String imageURL, petName, description, gender, species, birthday, location, weight;

    public DataClass(){

    }

    public DataClass(String imageURL, String petName, String description, String gender, String species, String birthday, String location, String weight) {
        this.imageURL = imageURL;
        this.petName = petName;
        this.description = description;
        this.gender = gender;
        this.species = species;
        this.birthday = birthday;
        this.location = location;
        this.weight = weight;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}