package com.zzn.utils;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.Timestamp;
import java.util.*;

/**
 * @author zzn
 * @create 2022-12-24 1:34
 */
public class CompareFieldsUtils {
    /**
     * 对比俩个对象相同字段的值 --工具类
     * @param obj1 修改前的对象
     * @param obj2 修改后的对象
     * @param ignoreArr
     * @return
     */
    public static boolean compareFields(Object obj1, Object obj2, String[] ignoreArr) throws IntrospectionException, InvocationTargetException, IllegalAccessException {

        boolean flag = true;
        List<String> ignoreList = null;
        if (ignoreArr != null && ignoreArr.length > 0) {
            // array转化为list
            ignoreList = Arrays.asList(ignoreArr);
        }
        if (obj1.getClass() == obj2.getClass()) {// 只有两个对象都是同一类型的才有可比性
            Class clazz = obj1.getClass();
            // 获取object的属性描述
            PropertyDescriptor[] pds = Introspector.getBeanInfo(clazz,
                    Object.class).getPropertyDescriptors();
            for (PropertyDescriptor pd : pds) {// 这里就是所有的属性了
                String name = pd.getName();// 属性名
                if (ignoreList != null && ignoreList.contains(name)) {// 如果当前属性选择忽略比较，跳到下一次循环
                    continue;
                }
                Method readMethod = pd.getReadMethod();// get方法
                // 在obj1上调用get方法等同于获得obj1的属性值
                Object o1 = readMethod.invoke(obj1);
                // 在obj2上调用get方法等同于获得obj2的属性值
                Object o2 = readMethod.invoke(obj2);
                if (o1 instanceof Timestamp) {
                    o1 = new Date(String.valueOf(((Timestamp) o1).getTimestamp()));
                }
                if (o2 instanceof Timestamp) {
                    o2 = new Date(String.valueOf(((Timestamp) o2).getTimestamp()));
                }
                if (o1 == null && o2 == null) {
                    continue;
                } else if (o1 == null && o2 != null) {
                    flag = false;

                    continue;
                }
                if (!o1.equals(o2)) {
                    flag = false;

                }
            }
        }
        return flag;
    }
}
