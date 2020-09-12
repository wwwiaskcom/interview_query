package com.lcj.framework.domain;

import java.util.ArrayList;
import java.util.List;

public class OrderBy implements QueryHandleType {
    private List<OrderByRule> ruleChains = new ArrayList<OrderByRule>();

    public OrderBy orderAsc(String propertyName) {
        this.ruleChains.add(new OrderByRule(propertyName,true));
        return this;
    }

    public OrderBy orderDesc(String propertyName) {
        this.ruleChains.add(new OrderByRule(propertyName,false));
        return this;
    }

    public class OrderByRule{
        private boolean ascending;
        private String propertyName;

        protected OrderByRule(String propertyName, boolean ascending) {
            this.propertyName = propertyName;
            this.ascending = ascending;
        }

        public boolean isAscending() {
            return ascending;
        }

        public String getPropertyName() {
            return propertyName;
        }
    }

    public List<OrderByRule> getRuleChains() {
        return ruleChains;
    }
}
