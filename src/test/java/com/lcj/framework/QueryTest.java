package com.lcj.framework;

import com.lcj.framework.domain.*;
import com.lcj.framework.domian.User;
import com.lcj.framework.handle.GroupByHandleExecutor;
import com.lcj.framework.handle.OrderByLimitHandleExecutor;
import com.lcj.framework.handle.WhereHandleExecutor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;

public class QueryTest {
    List<User> userList;

    @Before
    public void initDataSource(){
        userList = new ArrayList<>();
        short scoreEone = 12;
        Short scoreFone = 14;
        User one = new User("李长江",10,"lcj",1,123456789L,987654321L,new Date(1595820594000L),BigDecimal.ONE,1.234,1.234,1.234f,1.234f,scoreEone,scoreFone);
        User two = new User("李长江2",101,"lcj2",0);
        User three = new User("李长江3",10,"lcj3",1,123456789L,987654321L,new Date(1599820594000L),BigDecimal.ONE,1.234,1.234,1.234f,1.234f,scoreEone,scoreFone);
        User four = new User("李长江4",10,"lcj4",1,123456789L,987654321L,new Date(1599820584000L),BigDecimal.ONE,1.234,1.234,1.234f,1.234f,scoreEone,scoreFone);
        User five = new User("李长江5",10,"lcj5",1,123456789L,987654321L,new Date(1599820994000L),BigDecimal.ONE,1.234,1.234,1.234f,1.234f,scoreEone,scoreFone);
        User six = new User("李长江6",10,"lcj6",0,123456789L,987654321L,new Date(1599820594000L),BigDecimal.ONE,1.234,1.234,1.234f,1.234f,scoreEone,scoreFone);
        User seven = new User("李长江7",10,"lcj7",1,123456789L,987654321L,new Date(1591821594000L),BigDecimal.ONE,1.234,1.234,1.234f,1.234f,scoreEone,scoreFone);
        User eight = new User("李长江8",10,"lcj8",0,123456789L,987654321L,new Date(1599820594000L),BigDecimal.ONE,1.234,1.234,1.234f,1.234f,scoreEone,scoreFone);
        User nine = new User("李长江9",11,"lcj9",1,123456789L,987654321L,new Date(1599810594000L),BigDecimal.ONE,1.234,1.234,1.234f,1.234f,scoreEone,scoreFone);
        User ten = new User("李长江10",10,"lcj10",1,123456789L,987654321L,new Date(1591820594000L),BigDecimal.ONE,1.234,1.234,1.234f,1.234f,scoreEone,scoreFone);
        User eleven = new User("李长江11",111,"lcj11",0);
        userList.add(one);
        userList.add(two);
        userList.add(three);
        userList.add(four);
        userList.add(five);
        userList.add(six);
        userList.add(seven);
        userList.add(eight);
        userList.add(nine);
        userList.add(ten);
        userList.add(eleven);
    }

    @Test
    public void whereNullTest(){
        WhereHandleExecutor<User> whereExecutor = new WhereHandleExecutor<User>();
        Where where = new Where();
        where.equal("userId","lcj");
        where.or();
        where.equal("userName","李长江2");

        where.greaterEqual("age",10);
        where.between("userId","lcj","lcj2");
        where.isEmpty("scoreD");

        List<User> queryResultListNull = whereExecutor.execute(null,where);
        Assert.assertEquals("源数据为null时where条件查询返回数量错误",null,queryResultListNull);
        List<User> queryResultListEmpty = whereExecutor.execute(new ArrayList<>(),where);
        Assert.assertEquals("源数据size=0时where条件查询返回数量错误",null,queryResultListEmpty);

        //测试where条件为空的情况
        List<User> queryResultListWhereEmpty = whereExecutor.execute(userList,null);
        Assert.assertEquals("where为null时where条件查询返回数量错误",userList,queryResultListWhereEmpty);
    }

    @Test
    public void whereTest(){
        WhereHandleExecutor<User> whereExecutor = new WhereHandleExecutor<User>();
        Where where = new Where();
        where.equal("userId","lcj");
        where.or();
        where.equal("userName","李长江2");

        //目前只实现了equal、其他条件都是mock直接返回true
        where.greaterEqual("age",10);
        where.between("userId","lcj","lcj2");
        where.isNull("age");
        where.isEmpty("scoreD");
        where.in("age",Arrays.asList(10,11));
        where.isNotEmpty("userId");
        where.notIn("age",Arrays.asList(110,121));
        where.isNotNull("scoreD");
        where.greaterThan("scoreD",1.1);
        where.lessEqual("scoreD",1.2);
        where.lessThan("scoreD",1.2);
        where.notEqual("scoreD",1.2);
        where.like("userName","李长江");
        List<User> queryResultList = whereExecutor.execute(userList,where);
        Assert.assertEquals("where条件查询返回数量错误",2,queryResultList.size());
        Assert.assertEquals("where条件查询数据错误","lcj",queryResultList.get(0).getUserId());
    }

    @Test
    public void groupByTest(){
        GroupByHandleExecutor<User> groupByHandleExecutor = new GroupByHandleExecutor<>();
        GroupBy groupby = new GroupBy();
        groupby.addGroupByProperty("age");
        groupby.addGroupByProperty("gender");

        groupby.avg("scoreA","scoreA",3);
        groupby.avg("scoreC","scoreC");
        groupby.sum("scoreB","scoreB");
        groupby.sum("scoreD","scoreD",3);

        List<Map<String,Object>> groupByResultList = groupByHandleExecutor.execute(userList,groupby);
        Assert.assertEquals("groupBy条件查询返回数量错误",5,groupByResultList.size());
        Assert.assertEquals("groupBy条件查询数据错误",1.234,groupByResultList.get(0).get("scoreA"));
    }
    @Test
    public void orderByLimitTest(){
        OrderByLimitHandleExecutor<User> orderByLimitHandleExecutor = new OrderByLimitHandleExecutor<>();
        OrderBy orderBy = new OrderBy();
        orderBy.orderAsc("age");
        orderBy.orderDesc("scoreC");
        orderBy.orderAsc("birthday");
        orderBy.orderDesc("userId");
        orderBy.orderDesc("scoreA");
        orderBy.orderDesc("salary");

        Limit limit = new Limit(3);
        List<User> orderByResultListLimit = orderByLimitHandleExecutor.execute(userList,orderBy,limit);
        Assert.assertEquals("orderByResultListLimit条件查询返回数量错误",3,orderByResultListLimit.size());
        Assert.assertEquals("orderByResultListLimit条件查询数据错误","lcj",orderByResultListLimit.get(0).getUserId());

        List<User> orderByResultListNoLimit = orderByLimitHandleExecutor.execute(userList,orderBy,null);
        Assert.assertEquals("orderByResultListNoLimit条件查询返回数量错误",11,orderByResultListNoLimit.size());

    }

    @Test
    public void queryTest(){
        Query<User> queryService = new DefaultQuery<User>();
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.equal(User::getAge,10)
                .or()
                .equal(User::getGender,1)
                .in(User::getGender,Arrays.asList(1,0))
                .isEmpty(User::getScoreE)
                .like(User::getUserId,"lcj")
                .isNotEmpty(User::getUserName)
                .greaterThan(User::getMobilePhoneA,12334455)
                .asc(User::getAge)
                .desc(User::getScoreA)
                .limit(10);

        List<User> queryList = queryService.query(userList,queryWrapper);
        Assert.assertEquals("queryList条件查询返回数量错误",9,queryList.size());
        Assert.assertEquals("queryList条件查询数据错误","lcj",queryList.get(0).getUserId());
    }

    @Test
    public void queryGroupByTest(){
        Query<User> queryService = new DefaultQuery<User>();
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.equal(User::getAge,10)
                .or()
                .equal(User::getGender,1)
                .in(User::getGender,Arrays.asList(1,0))
                .isEmpty(User::getScoreE)
                .like(User::getUserId,"lcj")
                .isNotEmpty(User::getUserName)
                .greaterThan(User::getMobilePhoneA,12334455)
                .groupBy(User::getGender)
                .groupBy(User::getAge)
                .avg(User::getScoreA,"scoreA")
                .sum(User::getScoreB,"scoreB")
                .asc("scoreA")
                .desc("scoreB")
                .limit(10);

        List<Map<String,Object>> queryList = queryService.queryGroupBy(userList,queryWrapper);
        Assert.assertEquals("queryGroupByTest条件查询返回数量错误",3,queryList.size());
        Assert.assertEquals("queryGroupByTest条件查询数据错误",7.404,queryList.get(0).get("scoreB"));
    }
}
