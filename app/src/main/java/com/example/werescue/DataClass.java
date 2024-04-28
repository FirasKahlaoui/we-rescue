package com.example.werescue;


import java.io.Serializable;

public class DataClass implements Serializable {
    private String imageURL, petName, description, gender, species, birthday, location, weight, id, imagePath, ownerName, ownerEmail;
    private String name;
    private int age;
    public DataClass(){

    }

    public DataClass(String name, String gender, int age) {
        this.name = name;
        this.gender = gender;
        this.age = age;
    }

    public DataClass(String id, String petName, String description, String gender, String species, String birthday, String location, String weight, String imagePath) {
        this.id = id;
        this.petName = petName;
        this.description = description;
        this.gender = gender;
        this.species = species;
        this.birthday = birthday;
        this.location = location;
        this.weight = weight;
        this.imagePath = imagePath;
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

    public DataClass(String id, String name, String description, String gender, String species, String birthday, String location, String weight, byte[] image) {
    }

    public DataClass(String imageURL, String petName, String description, String gender, String species, String birthday, String location, String weight, String ownerName, String ownerEmail) {
        this.imageURL = imageURL;
        this.petName = petName;
        this.description = description;
        this.gender = gender;
        this.species = species;
        this.birthday = birthday;
        this.location = location;
        this.weight = weight;
        this.ownerName = ownerName;
        this.ownerEmail = ownerEmail;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
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

    public String getOwnerName() {
    return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }
    public String getImagePath() {
        return imageURL;
    }

    public void setImagePath(String imagePath) {
        this.imageURL = imagePath;
    }
}