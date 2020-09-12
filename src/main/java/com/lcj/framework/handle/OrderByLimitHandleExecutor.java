package com.lcj.framework.handle;

import com.lcj.framework.common.PropertyValue;
import com.lcj.framework.common.ReflectUtil;
import com.lcj.framework.domain.Limit;
import com.lcj.framework.domain.OrderBy;

import java.util.*;
import java.util.stream.Collectors;

public class OrderByLimitHandleExecutor<T> {

    public List<T> execute(List<T> dataSourceList, OrderBy orderBy, Limit limit) {
        if(orderBy == null || orderBy.getRuleChains().isEmpty()){
            return dataSourceList;
        }
        List<OrderBy.OrderByRule> orderByRuleList = orderBy.getRuleChains();
        List<Comparator> comparatorList = new ArrayList<>();

        for(OrderBy.OrderByRule orderByRule : orderByRuleList){
            Comparator<T> comparator = (o1,o2) -> {
                if(o1 == null){
                    return -1;
                }
                if(o2 == null){
                    return 1;
                }

                Object orderByPropertyValue1 = null;
                Object orderByPropertyValue2 = null;
                if(o1 instanceof Map){
                    orderByPropertyValue1 = ((Map) o1).get(orderByRule.getPropertyName());
                    orderByPropertyValue2 = ((Map) o2).get(orderByRule.getPropertyName());
                }else{
                    PropertyValue propertyValue1 = ReflectUtil.getGetPropertyValue(o1,orderByRule.getPropertyName());
                    PropertyValue propertyValue2 = ReflectUtil.getGetPropertyValue(o2,orderByRule.getPropertyName());
                    orderByPropertyValue1 = propertyValue1.getValue();
                    orderByPropertyValue2 = propertyValue2.getValue();
                }

                if(orderByPropertyValue1 == null){
                    return -1;
                }
                if(orderByPropertyValue2 == null){
                    return 1;
                }

                Class valueClass = orderByPropertyValue1.getClass();
                if(valueClass.equals(Integer.class) || valueClass.equals(int.class)){
                    return Integer.valueOf(orderByPropertyValue1 + "").compareTo(Integer.valueOf(orderByPropertyValue2 + ""));
                }else if(valueClass.equals(String.class)){
                    return String.valueOf(orderByPropertyValue1).compareTo(String.valueOf(orderByPropertyValue2));
                }else if(valueClass.equals(Long.class) || valueClass.equals(long.class)){
                    return Long.valueOf(orderByPropertyValue1 + "").compareTo(Long.valueOf(orderByPropertyValue2 + ""));
                }else if(valueClass.equals(Short.class) || valueClass.equals(short.class)){
                    return Short.valueOf(orderByPropertyValue1 + "").compareTo(Short.valueOf(orderByPropertyValue2 + ""));
                }else if(valueClass.equals(Boolean.class) || valueClass.equals(boolean.class)){
                    return Boolean.valueOf(orderByPropertyValue1 + "").compareTo(Boolean.valueOf(orderByPropertyValue2 + ""));
                }else if(valueClass.equals(Float.class) || valueClass.equals(float.class)){
                    return Float.valueOf(orderByPropertyValue1 + "").compareTo(Float.valueOf(orderByPropertyValue2 + ""));
                }else if(valueClass.equals(Double.class) || valueClass.equals(double.class)){
                    return Double.valueOf(orderByPropertyValue1 + "").compareTo(Double.valueOf(orderByPropertyValue2 + ""));
                }else if(valueClass.equals(Date.class)){
                    return ((Date)orderByPropertyValue1).compareTo((Date)orderByPropertyValue2);
                }else{
                    throw new IllegalArgumentException("order by data type not support");
                }
            };

            if(!orderByRule.isAscending()){
                comparator = comparator.reversed();
            }

            comparatorList.add(comparator);
        }
        Comparator comparatorSort = comparatorList.get(0);
        if(comparatorList.size() > 1){
            for(int i = 1 ; i < comparatorList.size() ; i++){
                comparatorSort = comparatorSort.thenComparing(comparatorSort);
            }
        }

        List<T> sortList = (List<T>)dataSourceList.stream().sorted(comparatorSort).collect(Collectors.toList());
        if(limit != null){
            List<T> limitList = sortList.stream().limit(limit.getLimitNum()).collect(Collectors.toList());
            return limitList;
        }
        return sortList;
    }
}
