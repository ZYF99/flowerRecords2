package com.example.zzyyff.flowerrecords;

public class class_wantthing {
    String name;
    String value;
    String saveValue;
    int id ;

    public int getId() {
        return id;
    }

    public class_wantthing(int id,String name, String value, String saveValue) {
        this.name = name;
        this.value = value;
        this.saveValue = saveValue;
        this.id = id;
    }

    @Override
    public String toString() {
        return "class_wantthing{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", saveValue='" + saveValue + '\'' +
                ", id=" + id +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public String toShow() {
        return "价格："+value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSaveValue() {
        return saveValue;
    }

    public void setSaveValue(String saveValue) {
        this.saveValue = saveValue;
    }

}
