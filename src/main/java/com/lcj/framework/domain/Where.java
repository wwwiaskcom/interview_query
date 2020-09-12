package com.lcj.framework.domain;

import java.util.ArrayList;
import java.util.List;

public class Where implements QueryHandleType {

    public static final int LIKE = 1;
    public static final int IN = 2;
    public static final int NOTIN = 3;
    public static final int BETWEEN = 4;
    public static final int EQ = 5;
    public static final int NOTEQ = 6;
    public static final int GT = 7;
    public static final int GE = 8;
    public static final int LT = 9;
    public static final int LE = 10;
    public static final int ISNULL = 11;
    public static final int ISNOTNULL = 12;
    public static final int ISEMPTY = 13;
    public static final int ISNOTEMPTY = 14;
    public static final int AND = 201;
    public static final int OR = 202;

    private List<WhereRule> ruleChains = new ArrayList<WhereRule>();
//默认条件都是and
//    public Where and() {
//        this.ruleChains.add(new WhereRule(AND,""));
//        return this;
//    }

    public Where or() {
        this.ruleChains.add(new WhereRule(OR,""));
        return this;
    }

    public Where isNull(String propertyName) {
        this.ruleChains.add(new WhereRule(ISNULL, propertyName));
        return this;
    }

    public Where isNotNull(String propertyName) {
        this.ruleChains.add(new WhereRule(ISNOTNULL, propertyName));
        return this;
    }

    public Where isEmpty(String propertyName) {
        this.ruleChains.add(new WhereRule(ISEMPTY, propertyName));
        return this;
    }

    public Where isNotEmpty(String propertyName) {
        this.ruleChains.add(new WhereRule(ISNOTEMPTY, propertyName));
        return this;
    }

    public Where like(String propertyName, Object value) {
        this.ruleChains.add(new WhereRule(LIKE, propertyName, new Object[] { value }));
        return this;
    }

    public Where equal(String propertyName, Object value) {
        this.ruleChains.add(new WhereRule(EQ, propertyName, new Object[] { value }));
        return this;
    }

    public Where notEqual(String propertyName, Object value) {
        this.ruleChains.add(new WhereRule(NOTEQ, propertyName, new Object[] { value }));
        return this;
    }

    public Where between(String propertyName, Object... values) {
        this.ruleChains.add(new WhereRule(BETWEEN, propertyName, values));
        return this;
    }

    public Where in(String propertyName, List<Object> values) {
        this.ruleChains.add(new WhereRule(IN, propertyName, new Object[] { values }));
        return this;
    }

    public Where notIn(String propertyName, List<Object> values) {
        this.ruleChains.add(new WhereRule(NOTIN, propertyName, new Object[] { values }));
        return this;
    }

    public Where greaterThan(String propertyName, Object value) {
        this.ruleChains.add(new WhereRule(GT, propertyName, new Object[] { value }));
        return this;
    }

    public Where greaterEqual(String propertyName, Object value) {
        this.ruleChains.add(new WhereRule(GE, propertyName, new Object[] { value }));
        return this;
    }

    public Where lessThan(String propertyName, Object value) {
        this.ruleChains.add(new WhereRule(LT, propertyName, new Object[] { value }));
        return this;
    }

    public Where lessEqual(String propertyName, Object value) {
        this.ruleChains.add(new WhereRule(LE, propertyName, new Object[] { value }));
        return this;
    }

    public class WhereRule{
        private int type;	//规则的类型
        private String propertyName;
        private Object[] values;

        public WhereRule(int type, String propertyName) {
            this.propertyName = propertyName;
            this.type = type;
        }

        public WhereRule(int type, String propertyName,
                    Object[] values) {
            this.propertyName = propertyName;
            this.values = values;
            this.type = type;
        }

        public Object[] getValues() {
            return this.values;
        }

        public int getType() {
            return this.type;
        }

        public String getPropertyName() {
            return this.propertyName;
        }
    }

    public List<WhereRule> getRuleChains() {
        return ruleChains;
    }
}
