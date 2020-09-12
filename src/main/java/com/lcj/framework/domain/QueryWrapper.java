package com.lcj.framework.domain;

import com.lcj.framework.common.ReflectUtil;
import com.lcj.framework.common.SFunction;

import java.util.List;

public class QueryWrapper<T> {
    private Where where;
    private GroupBy groupBy;
    private OrderBy orderBy;
    private Limit limit;

    public QueryWrapper(){
        this.init();
    }

    public void init(){
        this.where = new Where();
    }

    public QueryWrapper<T> or() {
        this.where.or();
        return this;
    }

    public QueryWrapper<T> isNull(SFunction<T, ?> property) {
        String propertyName = ReflectUtil.getFieldName(property);
        this.where.isNull(propertyName);
        return this;
    }

    public QueryWrapper<T> isNotNull(SFunction<T, ?> property) {
        String propertyName = ReflectUtil.getFieldName(property);
        this.where.isNull(propertyName);
        return this;
    }

    public QueryWrapper<T> isEmpty(SFunction<T, ?> property) {
        String propertyName = ReflectUtil.getFieldName(property);
        this.where.isEmpty(propertyName);
        return this;
    }

    public QueryWrapper<T> isNotEmpty(SFunction<T, ?> property) {
        String propertyName = ReflectUtil.getFieldName(property);
        this.where.isNotEmpty(propertyName);
        return this;
    }

    public QueryWrapper<T> like(SFunction<T, ?> property, Object value) {
        String propertyName = ReflectUtil.getFieldName(property);
        this.where.like(propertyName,value);
        return this;
    }

    public QueryWrapper<T> equal(SFunction<T, ?> property, Object value) {
        String propertyName = ReflectUtil.getFieldName(property);
        this.where.equal(propertyName,value);
        return this;
    }

    public QueryWrapper<T> notEqual(SFunction<T, ?> property, Object value) {
        String propertyName = ReflectUtil.getFieldName(property);
        this.where.notEqual(propertyName,value);
        return this;
    }

    public QueryWrapper<T> between(SFunction<T, ?> property, Object... values) {
        String propertyName = ReflectUtil.getFieldName(property);
        this.where.between(propertyName,values);
        return this;
    }

    public QueryWrapper<T> in(SFunction<T, ?> property, List<Object> values) {
        String propertyName = ReflectUtil.getFieldName(property);
        this.where.in(propertyName,values);
        return this;
    }

    public QueryWrapper<T> notIn(SFunction<T, ?> property, List<Object> values) {
        String propertyName = ReflectUtil.getFieldName(property);
        this.where.notIn(propertyName,values);
        return this;
    }

    public QueryWrapper<T> greaterThan(SFunction<T, ?> property, Object value) {
        String propertyName = ReflectUtil.getFieldName(property);
        this.where.greaterThan(propertyName,value);
        return this;
    }

    public QueryWrapper<T> greaterEqual(SFunction<T, ?> property, Object value) {
        String propertyName = ReflectUtil.getFieldName(property);
        this.where.greaterEqual(propertyName,value);
        return this;
    }

    public QueryWrapper<T> lessThan(SFunction<T, ?> property, Object value) {
        String propertyName = ReflectUtil.getFieldName(property);
        this.where.lessThan(propertyName,value);
        return this;
    }

    public QueryWrapper<T> lessEqual(SFunction<T, ?> property, Object value) {
        String propertyName = ReflectUtil.getFieldName(property);
        this.where.lessEqual(propertyName,value);
        return this;
    }

    public QueryWrapper<T> groupBy(SFunction<T, ?> property){
        if(this.groupBy == null){
            this.groupBy = new GroupBy();
        }
        String propertyName = ReflectUtil.getFieldName(property);
        this.groupBy.addGroupByProperty(propertyName);
        return this;
    }

    /**
     * avg sum 目前只支持group by，不能像mysql那样可以用在不分组的情况
     * @param property
     * @param alias
     * @return
     */
    public QueryWrapper<T> avg(SFunction<T, ?> property,String alias){
        String propertyName = ReflectUtil.getFieldName(property);
        this.groupBy.avg(propertyName,alias);
        return this;
    }

    public QueryWrapper<T> sum(SFunction<T, ?> property,String alias){
        String propertyName = ReflectUtil.getFieldName(property);
        this.groupBy.sum(propertyName,alias);
        return this;
    }

    public QueryWrapper<T> asc(SFunction<T, ?> property){
        if(this.orderBy == null){
            this.orderBy = new OrderBy();
        }
        String propertyName = ReflectUtil.getFieldName(property);
        this.orderBy.orderAsc(propertyName);
        return this;
    }

    public QueryWrapper<T> asc(String propertyName){
        if(this.orderBy == null){
            this.orderBy = new OrderBy();
        }
        this.orderBy.orderAsc(propertyName);
        return this;
    }

    public QueryWrapper<T> desc(SFunction<T, ?> property){
        if(this.orderBy == null){
            this.orderBy = new OrderBy();
        }
        String propertyName = ReflectUtil.getFieldName(property);
        this.orderBy.orderDesc(propertyName);
        return this;
    }

    public QueryWrapper<T> desc(String propertyName){
        if(this.orderBy == null){
            this.orderBy = new OrderBy();
        }
        this.orderBy.orderAsc(propertyName);
        return this;
    }

    public QueryWrapper<T> limit(Integer limitNum){
        this.limit = new Limit(limitNum);
        return this;
    }

    public Where getWhere() {
        return where;
    }

    public void setWhere(Where where) {
        this.where = where;
    }

    public GroupBy getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(GroupBy groupBy) {
        this.groupBy = groupBy;
    }

    public OrderBy getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(OrderBy orderBy) {
        this.orderBy = orderBy;
    }

    public Limit getLimit() {
        return limit;
    }

    public void setLimit(Limit limit) {
        this.limit = limit;
    }
}
