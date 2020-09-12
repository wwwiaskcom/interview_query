package com.lcj.framework;

import com.lcj.framework.domain.QueryWrapper;
import com.lcj.framework.handle.GroupByHandleExecutor;
import com.lcj.framework.handle.OrderByLimitHandleExecutor;
import com.lcj.framework.handle.WhereHandleExecutor;

import java.util.List;
import java.util.Map;

/**
 * 查询执行顺序
 * where -> group by -> order by -> limit
 * @param <T>
 */
public class DefaultQuery<T> implements Query<T>{

    public List<T> query(List<T> sourceData ,QueryWrapper<T> queryWrapper) {
        if(queryWrapper.getGroupBy() != null){
            throw new IllegalArgumentException("query not support group by please use method queryGroupBy");
        }
        List<T> filterAfterWhereList = new WhereHandleExecutor<T>().execute(sourceData,queryWrapper.getWhere());
        List<T> sortLimitList = new OrderByLimitHandleExecutor<T>().execute(filterAfterWhereList,queryWrapper.getOrderBy(),queryWrapper.getLimit());
        return sortLimitList;
    }

    @Override
    public List<Map<String, Object>> queryGroupBy(List<T> sourceData, QueryWrapper<T> queryWrapper) {
        List<T> filterAfterWhereList = new WhereHandleExecutor<T>().execute(sourceData,queryWrapper.getWhere());
        List<Map<String,Object>> groupByList = new GroupByHandleExecutor().execute(filterAfterWhereList,queryWrapper.getGroupBy());
        List<Map<String,Object>> sortLimitList = new OrderByLimitHandleExecutor<Map<String,Object>>().execute(groupByList,queryWrapper.getOrderBy(),queryWrapper.getLimit());
        return sortLimitList;
    }
}
