package com.example.zzyyff.flowerrecords;

public class class_tablelist {
    double percent = 0.00;
    int count = 0;
    String property = "";
    double money = 0.00;

    public double getMoney() {
        return money;
    }

    public class_tablelist(double percent, String property, int count, double money){
    this.percent = percent;
    this.property = property;
    this.count = count;
    this.money = money;
}
    public double getPercent() {
        return percent;
    }

    public int getCount() {
        return count;
    }

    public String getProperty() {
        return property;
    }
}
