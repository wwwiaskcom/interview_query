package com.lcj.framework.common;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectUtil {
    /**
     * 根据函数获取字段名称
     * eg：User::getUserId --> userId
     * @param fn
     * @param <T>
     * @return
     */
    public static <T> String getFieldName(SFunction<T, ?> fn) {
        // 从function取出序列化方法
        Method writeReplaceMethod;
        try {
            writeReplaceMethod = fn.getClass().getDeclaredMethod("writeReplace");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        // 从序列化方法取出序列化的lambda信息
        boolean isAccessible = writeReplaceMethod.isAccessible();
        writeReplaceMethod.setAccessible(true);
        SerializedLambda serializedLambda;
        try {
            serializedLambda = (SerializedLambda) writeReplaceMethod.invoke(fn);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        writeReplaceMethod.setAccessible(isAccessible);

        // 从lambda信息取出method、field、class等
        //fieldName形式例如UserId
        String fieldName = serializedLambda.getImplMethodName().substring("get".length());

        //fieldName形式例如userId
        fieldName = fieldName.replaceFirst(fieldName.charAt(0) + "", (fieldName.charAt(0) + "").toLowerCase());
        //fieldName变成_的形式例如user_id
        //fieldName = fieldName.replaceAll("[A-Z]", "_$0").toLowerCase();
        return fieldName;
    }

    /**
     * 获取对象中指定字段的值和类型
     * @param ob
     * @param name
     * @return
     * @throws Exception
     */
    public static PropertyValue getGetPropertyValue(Object ob , String name){
        Method[] m = ob.getClass().getMethods();
        for(int i = 0;i < m.length;i++){
            if(("get"+name).toLowerCase().equals(m[i].getName().toLowerCase())){
                PropertyValue propertyValue = new PropertyValue();
                Object value = null;
                try {
                    value = m[i].invoke(ob);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                propertyValue.setValue(value);
                propertyValue.setValueClass(m[i].getReturnType());
                return propertyValue;
            }
        }
        return null;
    }

}
