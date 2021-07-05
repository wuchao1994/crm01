package com.xxx.crm.query;

import com.xxx.crm.base.BaseQuery;

public class SaleChanceQuery extends BaseQuery {
//    条件查询的字段
    private String customerName;
    private String createMan;
    private String state;
    private Integer assignMan;
    private Integer devResult;


    public SaleChanceQuery() {
    }

    public SaleChanceQuery(String customerName, String createMan, String state) {
        this.customerName = customerName;
        this.createMan = createMan;
        this.state = state;
    }

    public SaleChanceQuery(String customerName, String createMan, String state, Integer assignMan, Integer devResult) {
        this.customerName = customerName;
        this.createMan = createMan;
        this.state = state;
        this.assignMan = assignMan;
        this.devResult = devResult;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCreateMan() {
        return createMan;
    }

    public void setCreateMan(String createMan) {
        this.createMan = createMan;
    }

    public Integer getAssignMan() {
        return assignMan;
    }

    public void setAssignMan(Integer assignMan) {
        this.assignMan = assignMan;
    }

    public Integer getDevResult() {
        return devResult;
    }

    public void setDevResult(Integer devResult) {
        this.devResult = devResult;
    }

    @Override
    public String toString() {
        return "SaleChanceQuery{" +
                "customerName='" + customerName + '\'' +
                ", createMan='" + createMan + '\'' +
                ", state='" + state + '\'' +
                ", assignMan=" + assignMan +
                ", devResult=" + devResult +
                '}';
    }
}
