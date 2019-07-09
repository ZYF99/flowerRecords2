package com.example.zzyyff.flowerrecords;

public class class_Diary {
    int id;
    String name;
    String text;
    String year;
    String month;
    String day;
    String property;
    String image_path;
    String  wheather;
    String city ;

    public String getImage_path() {
        return image_path;
    }

    public String getProperty() {
        return property;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWheather() {
        return wheather;
    }

    public String getCity() {
        return city;
    }

    public class_Diary(int input_id, String property, String input_name,
                       String input_year, String input_month, String input_day,
                       String input_text, String image_path, String wheather, String city){

        this.id = input_id;
        this.name = input_name;
        this.text = input_text;
        this.year = input_year;
        this.month = input_month;
        this.day = input_day;
        this.image_path = image_path;
        this.property = property;
        this.wheather = wheather;
        this.city = city;


    }
    public String getName() {
        return name;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public void setName(String name) {
        this.name = name;
    }




}
