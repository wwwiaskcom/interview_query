package com.lcj.framework.domain;

public class Limit implements QueryHandleType {
    private Integer limitNum;

    public Limit(Integer limitNum){
        this.limitNum = limitNum;
    }

    public Integer getLimitNum() {
        return limitNum;
    }

    public void setLimitNum(Integer limitNum) {
        this.limitNum = limitNum;
    }
}
