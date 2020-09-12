package com.lcj.framework.handle;

import com.lcj.framework.common.PropertyValue;
import com.lcj.framework.common.ReflectUtil;
import com.lcj.framework.domain.GroupBy;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 分组
 * 这里的分组类似数据库的查询，而不是把数据按照分组字段将数据分成多个list,因为那样order by 和 limit就没有意义了
 * @param <T>
 */
public class GroupByHandleExecutor<T>{
    //分组字段组合key分割符
    private static String GROUP_KEY_SEPARATOR = "#%#";

    private static String GROUP_SUM_PROPERTY_KEY_PRE = "sum_";

    private static String GROUP_COUNT_PROPERTY_KEY = "count";

    public List<Map<String,Object>> execute(List<T> dataSourceList, GroupBy groupBy) {
        if(groupBy == null || groupBy.getRuleChains().isEmpty()){
            throw new IllegalArgumentException("GroupByHandleExecutor parameter error");
        }

        List<Map<String,Object>> handleResultList = new ArrayList<>();
        if(dataSourceList == null || dataSourceList.isEmpty()){
            return null;
        }

        //1、没有聚合函数、直接返回分组字段值
        if(groupBy.getAggregationChains().isEmpty()){
            for (T oneSource : dataSourceList){
                Map<String,Object> handleResultEntry = new HashMap<>();
                for(GroupBy.GroupByRule groupByRule : groupBy.getRuleChains()){
                    PropertyValue propertyValue = ReflectUtil.getGetPropertyValue(oneSource,groupByRule.getPropertyName());
                    handleResultEntry.put(groupByRule.getPropertyName(),propertyValue.getValue());
                }
                handleResultList.add(handleResultEntry);
            }

            return handleResultList;
        }

        //2、分组聚合
        Map<String,Map<String,Object>> aggregationMap = aggregationToMap(dataSourceList,groupBy);

        //3、遍历groupDataMap放入返回结果中
        List<String> groupPropertyList= groupBy.getRuleChains().stream().map(GroupBy.GroupByRule::getPropertyName).collect(Collectors.toList());
        Set<Map.Entry<String,Map<String,Object>>> aggregationEntrys = aggregationMap.entrySet();
        for(Map.Entry<String,Map<String,Object>> aggregationEntry : aggregationEntrys){
            Map<String,Object> handleResultEntry = new HashMap<>();
            String groupKey = aggregationEntry.getKey();
            String[] groupKeyValueArray = groupKey.split(GROUP_KEY_SEPARATOR);
            //3.1处理返回分组字段的值
            for(int i = 0 ; i < groupPropertyList.size() ; i++){
                handleResultEntry.put(groupPropertyList.get(i),groupKeyValueArray[i]);
            }

            //3.2处理聚合函数的返回值
            Map<String,Object> groupValue = aggregationEntry.getValue();
            transAggregationMapToResultData(groupValue,handleResultEntry,groupBy.getAggregationChains());

            handleResultList.add(handleResultEntry);
        }

        return handleResultList;
    }

    /**
     * 将数据转化成分组字段组合为key、
     * 各个求聚合函数字段的sum_和key包含的记录条数count的Map
     * @param dataSourceList
     * @param groupBy
     * @return
     */
    private Map<String,Map<String,Object>> aggregationToMap(List<T> dataSourceList,GroupBy groupBy){
        //1、将所有数据统计分组放入Map中
        Map<String,Map<String,Object>> groupDataMap = new HashMap<>();
        for (T oneSource : dataSourceList){
            //分组数据key=GROUP_KEY_SEPARATOR+分组字段值+GROUP_KEY_SEPARATOR+分组字段值
            String groupKey = "";
            for(GroupBy.GroupByRule groupByRule : groupBy.getRuleChains()){
                PropertyValue propertyValue = ReflectUtil.getGetPropertyValue(oneSource,groupByRule.getPropertyName());
                //组合分组数据key包含：记录条数count、sum、max、min等的值(目前只保存sum、count)
                groupKey = groupKey + GROUP_KEY_SEPARATOR + propertyValue.getValue();
            }
            groupKey = groupKey.substring(GROUP_KEY_SEPARATOR.length());

            Map<String,Object> groupOneData;
            if (groupDataMap.containsKey(groupKey)) {
                groupOneData = groupDataMap.get(groupKey);
                Integer count = Integer.valueOf(groupOneData.get(GROUP_COUNT_PROPERTY_KEY).toString());
                groupOneData.put(GROUP_COUNT_PROPERTY_KEY,++count);
                for(GroupBy.AggregationRule aggregationRule : groupBy.getAggregationChains()){
                    PropertyValue aggregationPropertyValue = ReflectUtil.getGetPropertyValue(oneSource,aggregationRule.getAggregationProperty());
                    String sumKey = GROUP_SUM_PROPERTY_KEY_PRE + aggregationRule.getAggregationProperty();
                    Double sumAggregationProperty = (double)groupOneData.get(sumKey);
                    Object aggregationValue = aggregationPropertyValue.getValue();
                    if(aggregationValue != null){
                        if(aggregationValue instanceof BigDecimal){
                            sumAggregationProperty = sumAggregationProperty + ((BigDecimal) aggregationValue).doubleValue();
                        }else{
                            sumAggregationProperty  = sumAggregationProperty + Double.valueOf("" + aggregationValue);
                        }
                    }
                    groupOneData.put(sumKey,sumAggregationProperty);
                }
            }else{
                groupOneData = new HashMap<>();
                groupOneData.put(GROUP_COUNT_PROPERTY_KEY,1);
                for(GroupBy.AggregationRule aggregationRule : groupBy.getAggregationChains()){
                    PropertyValue aggregationPropertyValue = ReflectUtil.getGetPropertyValue(oneSource,aggregationRule.getAggregationProperty());
                    String sumKey = GROUP_SUM_PROPERTY_KEY_PRE + aggregationRule.getAggregationProperty();
                    Object aggregationValue = aggregationPropertyValue.getValue();
                    Double sumAggregationProperty = 0.0;
                    if(aggregationValue != null){
                        if(aggregationValue instanceof BigDecimal){
                            sumAggregationProperty = ((BigDecimal) aggregationValue).doubleValue();
                        }else{
                            sumAggregationProperty  = Double.valueOf(aggregationValue + "");
                        }
                    }
                    groupOneData.put(sumKey,sumAggregationProperty);
                }
            }
            groupDataMap.put(groupKey,groupOneData);
        }

        return groupDataMap;
    }

    /**
     * 将聚合的数据转化成结果输出
     * @param aggregationMapEntry
     * @param handleResultEntry
     * @param aggregationChains
     * @return
     */
    private void transAggregationMapToResultData( Map<String,Object> aggregationMapEntry,Map<String,Object> handleResultEntry,List<GroupBy.AggregationRule> aggregationChains){
        for(GroupBy.AggregationRule oneAggregationRule : aggregationChains){
            switch (oneAggregationRule.getType()){
                case GroupBy.SUM:
                    handleResultEntry.put(oneAggregationRule.getAlias(),aggregationMapEntry.get(GROUP_SUM_PROPERTY_KEY_PRE + oneAggregationRule.getAggregationProperty()));
                    break;
                case GroupBy.AVG:
                    Object sumValueObj = aggregationMapEntry.get(GROUP_SUM_PROPERTY_KEY_PRE + oneAggregationRule.getAggregationProperty());
                    if(sumValueObj == null){
                        break;
                    }
                    Double sumValue = Double.valueOf(sumValueObj.toString());
                    Integer countValue = Integer.valueOf(aggregationMapEntry.get(GROUP_COUNT_PROPERTY_KEY).toString());
                    Integer scale = oneAggregationRule.getScale();
                    if(scale == null){
                        scale = 2;
                    }

                    Double avgValue = (double) Math.round(sumValue/countValue * roundDivisor(scale)) / roundDivisor(scale);
                    handleResultEntry.put(oneAggregationRule.getAlias(),avgValue);
                    break;
                //TODO 处理其他聚合函数
                default:
                    throw new IllegalArgumentException("can not support type " +oneAggregationRule.getType());
            }
        }
    }

    private Double roundDivisor(Integer scale){
        String divisorStr = "1";
        if(scale == null){
            return Double.valueOf(divisorStr);
        }

        for(int i = 0 ; i < scale ; i++){
            divisorStr = divisorStr + "0";
        }

        return Double.valueOf(divisorStr);
    }
}
