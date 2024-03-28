package com.example.werescue;

public class DataClass {
    private String imageURL, petName, description;
    private int age;

    public DataClass(){

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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DataClass(String imageURL, String petName, int age, String description) {
        this.imageURL = imageURL;
        this.petName = petName;
        this.age = age;
        this.description = description;
    }
}