package com.example.zzyyff.flowerrecords;

public class class_Single {
    String pay_method = "";
    String property = "";

    public int getId() {
        return id;
    }

    int id = 0;
    double income = 0;
    double outcome = 0;
    String inorout="";

    String date = "";
    String remark = "";

    public String getDate() {
        return date;
    }

    public String getRemark() {
        return remark;
    }

    public String getPay_method() {
        return pay_method;
    }
    public String getInorout() {
        return inorout;
    }
    public class_Single(String property, double income, double outcome, String inorout, String pay_method, String remark, String date, int id){
        this.property = property;
        this.income = income;
        this.outcome = outcome;
        this.inorout = inorout;
        this.date = date;
        this.remark = remark;
        this.id = id;

        this.pay_method = pay_method;
    }
    public String getProperty() {
        return property;
    }
    public void setProperty(String property) {
        this.property = property;
    }
    public double getIncome() {
        return income;
    }
    public double getOutcome() {
        return outcome;
    }
}
