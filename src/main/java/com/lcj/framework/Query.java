package com.lcj.framework;

import com.lcj.framework.domain.QueryWrapper;

import java.util.List;
import java.util.Map;

public interface Query<T> {
    List<T> query(List<T> sourceData ,QueryWrapper<T> queryWrapper);

    List<Map<String,Object>> queryGroupBy(List<T> sourceData , QueryWrapper<T> queryWrapper);
}
