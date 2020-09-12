package com.lcj.framework.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class GroupBy implements QueryHandleType {
    public static final int SUM = 1;
    public static final int AVG = 2;

    private List<GroupByRule> ruleChains = new ArrayList<>();

    private List<AggregationRule> aggregationChains = new ArrayList<>();

    public GroupBy addGroupByProperty(String propertyName) {
        this.ruleChains.add(new GroupByRule(propertyName));
        return this;
    }

    public GroupBy avg(String propertyName,String alias) {
        this.aggregationChains.add(new AggregationRule(propertyName,AVG,alias));
        return this;
    }

    public GroupBy avg(String propertyName,String alias,Integer scale) {
        this.aggregationChains.add(new AggregationRule(propertyName,AVG,alias,scale));
        return this;
    }

    public GroupBy sum(String propertyName,String alias,Integer scale) {
        this.aggregationChains.add(new AggregationRule(propertyName,SUM,alias,scale));
        return this;
    }

    public GroupBy sum(String propertyName,String alias) {
        this.aggregationChains.add(new AggregationRule(propertyName,SUM,alias));
        return this;
    }

    public class GroupByRule{
        private String propertyName;

        protected GroupByRule(String propertyName) {
            this.propertyName = propertyName;
        }

        public String getPropertyName() {
            return propertyName;
        }
    }

    public class AggregationRule{

        //聚合操作的字段
        private String aggregationProperty;

        private int type;

        //别名、排序时可能用这个字段
        private String alias;

        //保留小数位
        private Integer scale;

        public AggregationRule(String aggregationProperty, int type, String alias) {
            this.aggregationProperty = aggregationProperty;
            this.type = type;
            this.alias = alias;
        }

        public AggregationRule(String aggregationProperty, int type, String alias,Integer scale) {
            this.aggregationProperty = aggregationProperty;
            this.type = type;
            this.alias = alias;
            this.scale = scale;
        }

        public String getAggregationProperty() {
            return aggregationProperty;
        }

        public int getType() {
            return type;
        }

        public String getAlias() {
            return alias;
        }

        public Integer getScale() {
            return scale;
        }
    }

    public List<GroupByRule> getRuleChains() {
        return ruleChains;
    }

    public List<AggregationRule> getAggregationChains() {
        return aggregationChains;
    }
}
