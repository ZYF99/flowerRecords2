package com.example.zzyyff.flowerrecords;

public class class_tagitem {
    String name;
    boolean isclick;

    @Override
    public String toString() {
        return "class_tagitem{" +
                "name='" + name + '\'' +
                ", isclick=" + isclick +
                '}';
    }

    public class_tagitem(String name, boolean isclick) {
        this.name = name;
        this.isclick = isclick;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isIsclick() {
        return isclick;
    }

    public void setIsclick(boolean isclick) {
        this.isclick = isclick;
    }
}
