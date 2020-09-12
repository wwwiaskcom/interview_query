package com.lcj.framework.domian;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class User implements Serializable {
    private static final long serialVersionUID = 3814738576963931746L;
    private String userName;
    private Integer age;
    private String userId;
    private int gender;
    private Long mobilePhoneA;
    private long mobilePhoneB;
    private Date birthday;
    private BigDecimal salary;
    private double scoreA;
    private Double scoreB;
    private float scoreC;
    private Float scoreD;
    private short scoreE;
    private Short scoreF;

    public User() {
    }

    public User(String userName, Integer age, String userId, int gender) {
        this.userName = userName;
        this.age = age;
        this.userId = userId;
        this.gender = gender;
    }

    public User(String userName, Integer age, String userId, int gender, Long mobilePhoneA, long mobilePhoneB, Date birthday, BigDecimal salary, double scoreA, Double scoreB, float scoreC, Float scoreD, short scoreE, Short scoreF) {
        this.userName = userName;
        this.age = age;
        this.userId = userId;
        this.gender = gender;
        this.mobilePhoneA = mobilePhoneA;
        this.mobilePhoneB = mobilePhoneB;
        this.birthday = birthday;
        this.salary = salary;
        this.scoreA = scoreA;
        this.scoreB = scoreB;
        this.scoreC = scoreC;
        this.scoreD = scoreD;
        this.scoreE = scoreE;
        this.scoreF = scoreF;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public Long getMobilePhoneA() {
        return mobilePhoneA;
    }

    public void setMobilePhoneA(Long mobilePhoneA) {
        this.mobilePhoneA = mobilePhoneA;
    }

    public long getMobilePhoneB() {
        return mobilePhoneB;
    }

    public void setMobilePhoneB(long mobilePhoneB) {
        this.mobilePhoneB = mobilePhoneB;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public double getScoreA() {
        return scoreA;
    }

    public void setScoreA(double scoreA) {
        this.scoreA = scoreA;
    }

    public Double getScoreB() {
        return scoreB;
    }

    public void setScoreB(Double scoreB) {
        this.scoreB = scoreB;
    }

    public float getScoreC() {
        return scoreC;
    }

    public void setScoreC(float scoreC) {
        this.scoreC = scoreC;
    }

    public Float getScoreD() {
        return scoreD;
    }

    public void setScoreD(Float scoreD) {
        this.scoreD = scoreD;
    }

    public short getScoreE() {
        return scoreE;
    }

    public void setScoreE(short scoreE) {
        this.scoreE = scoreE;
    }

    public Short getScoreF() {
        return scoreF;
    }

    public void setScoreF(Short scoreF) {
        this.scoreF = scoreF;
    }
}
