package com.example.citytour;

public class Informations {
    private String name;
    private String profession;
    private String date;
    private String phone;


    public Informations() {
    }

    public Informations(String name, String profession, String date, String phone) {
        this.name = name;
        this.profession = profession;
        this.date = date;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getProfession() {
        return profession;
    }

    public String getDate() {
        return date;
    }

    public String getPhone() {
        return phone;
    }
}
