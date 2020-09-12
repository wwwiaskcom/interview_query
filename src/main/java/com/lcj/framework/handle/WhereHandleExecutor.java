package com.lcj.framework.handle;

import com.lcj.framework.common.PropertyValue;
import com.lcj.framework.common.ReflectUtil;
import com.lcj.framework.domain.QueryHandleType;
import com.lcj.framework.domain.Where;

import java.util.ArrayList;
import java.util.List;

/**
 * 现在的逻辑是没有默认都是and的逻辑，除非单独加or
 * 先实现简单的方式，不带括号的方式
 * @param <T>
 */
public class WhereHandleExecutor<T> {

    public List<T> execute(List<T> dataSourceList, Where where) {
        if(where == null || where.getRuleChains().size() == 0){
            return dataSourceList;
        }

        List<T> handleResultList = new ArrayList<>();
        if(dataSourceList == null || dataSourceList.isEmpty()){
            return null;
        }

        for(T oneSource : dataSourceList){
            List<OneWhereJudgeResult> whereJudgeResultList = new ArrayList<>();
            int curAndOrFlag = Where.AND;
            for (int i = 0; i < where.getRuleChains().size(); i++) {
                Where.WhereRule rule = where.getRuleChains().get(i);
                //排除第一个条件是and 和 or
                if((i == 0 || i == where.getRuleChains().size() - 1) && (rule.getType() == Where.AND || rule.getType() == Where.OR)){
                    continue;
                }
                Boolean currJudge = false;
                switch (rule.getType()) {
                    case Where.OR:
                        curAndOrFlag = Where.OR;
                        break;
                    case Where.EQ:
                        currJudge = processEqual(oneSource,rule);
                        break;
                    case Where.LIKE:
                        currJudge = processLike(oneSource,rule);
                        break;
                    case Where.BETWEEN:
                        currJudge = processBetween(oneSource,rule);
                        break;
                    case Where.NOTEQ:
                        currJudge = processNotEqual(oneSource,rule);
                        break;
                    case Where.GT:
                        currJudge = processGreaterThen(oneSource,rule);
                        break;
                    case Where.GE:
                        currJudge = processGreaterEqual(oneSource,rule);
                        break;
                    case Where.LT:
                        currJudge = processLessThen(oneSource,rule);
                        break;
                    case Where.LE:
                        currJudge = processLessEqual(oneSource,rule);
                        break;
                    case Where.IN:
                        currJudge = processIN(oneSource,rule);
                        break;
                    case Where.NOTIN:
                        currJudge = processNotIN(oneSource,rule);
                        break;
                    case Where.ISNULL:
                        currJudge = processIsNull(oneSource,rule);
                        break;
                    case Where.ISNOTNULL:
                        currJudge = processIsNotNull(oneSource,rule);
                        break;
                    case Where.ISEMPTY:
                        currJudge = processIsEmpty(oneSource,rule);
                        break;
                    case Where.ISNOTEMPTY:
                        currJudge = processIsNotEmpty(oneSource,rule);
                        break;
                    default:
                        throw new IllegalArgumentException("type " + rule.getType() + " not supported.");
                }

                //如果rule是or 或者 and 就不判断
                if(rule.getType() != Where.OR && rule.getType() != Where.AND){
                    whereJudgeResultList.add(new OneWhereJudgeResult(curAndOrFlag,currJudge));
                    //or在判断一次后重置成AND
                    if(curAndOrFlag == Where.OR){
                        curAndOrFlag = Where.AND;
                    }
                }
            }

            if(oneDataJudgeWhereResult(whereJudgeResultList)){
                handleResultList.add(oneSource);
            }
        }
        return handleResultList;
    }

    /**
     * 一个where条件判断结果
     * 为了实现简单会把所有条件判断结果记录下来，全部判断
     */
    private class OneWhereJudgeResult{
        private int andOr;
        private Boolean judgeResult;

        public OneWhereJudgeResult(int andOr, Boolean judgeResult) {
            this.andOr = andOr;
            this.judgeResult = judgeResult;
        }

        public int getAndOr() {
            return andOr;
        }

        public Boolean getJudgeResult() {
            return judgeResult;
        }
    }

    /**
     * 处理相等的情况
     * @param oneSource
     * @param rule
     * @return
     */
    Boolean processEqual(T oneSource,Where.WhereRule rule){
        Object[] conditionValues = rule.getValues();
        PropertyValue propertyValue = ReflectUtil.getGetPropertyValue(oneSource,rule.getPropertyName());
        if(conditionValues == null || conditionValues.length != 1){
            throw new IllegalArgumentException("values length is error");
        }
        Object conditionValue = conditionValues[0];

        Class<?> type = propertyValue.getValueClass();
        //TODO 尝试将条件参数类型转换成bean中的类类型、现在如果传入的条件参数和bean类型不一致，不能用equals
        Boolean curStepPass = conditionValue.equals(propertyValue.getValue());

        return curStepPass;
    }

    Boolean processLike(T oneSource,Where.WhereRule rule){
        return true;
    }
    Boolean processBetween(T oneSource,Where.WhereRule rule){
        return true;
    }
    Boolean processNotEqual(T oneSource,Where.WhereRule rule){
        return true;
    }
    Boolean processGreaterThen(T oneSource,Where.WhereRule rule){
        return true;
    }
    Boolean processGreaterEqual(T oneSource,Where.WhereRule rule){
        return true;
    }
    Boolean processLessThen(T oneSource,Where.WhereRule rule){
        return true;}
    Boolean processLessEqual(T oneSource,Where.WhereRule rule){
        return true;
    }
    Boolean processIN(T oneSource,Where.WhereRule rule){
        return true;
    }
    Boolean processNotIN(T oneSource,Where.WhereRule rule){
        return true;
    }
    Boolean processIsNull(T oneSource,Where.WhereRule rule){
        return true;
    }
    Boolean processIsNotNull(T oneSource,Where.WhereRule rule){
        return true;
    }
    Boolean processIsEmpty(T oneSource,Where.WhereRule rule){
        return true;
    }
    Boolean processIsNotEmpty(T oneSource,Where.WhereRule rule){
        return true;
    }

    /**
     * 判断一条数据是否满足where条件
     * @param whereJudgeResultList
     * @return
     */
    private Boolean oneDataJudgeWhereResult(List<OneWhereJudgeResult> whereJudgeResultList){
        Boolean judgeResult = null;
        for(OneWhereJudgeResult oneWhereJudgeResult : whereJudgeResultList){
            if(judgeResult == null){
                judgeResult = oneWhereJudgeResult.getJudgeResult();
            }

            if(oneWhereJudgeResult.getAndOr() == Where.AND){
                judgeResult = judgeResult && oneWhereJudgeResult.getJudgeResult();
            }else{
                judgeResult = judgeResult || oneWhereJudgeResult.getJudgeResult();
            }
        }

        return judgeResult;
    }
}
