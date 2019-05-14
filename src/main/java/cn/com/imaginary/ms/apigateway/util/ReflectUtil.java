package cn.com.imaginary.ms.apigateway.util;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.util.StringUtils;

/**
 * 反射工具类
 *
 * @author : Imaginary
 * @version : V1.0
 * @date : 2018/9/16 10:59
 */
public class ReflectUtil {
    public static String getStrValue(Object bean, String name) {

        Object obj = getValue(bean, name);
        if (obj == null) {
            return null;
        }
        return obj.toString();
    }

    public static String getStrValue(Object bean, String name, String def) {

        Object obj = getValue(bean, name);
        if (obj == null) {
            return def;
        }
        return obj.toString();
    }

    public static Integer getIntValue(Object bean, String name) {

        Object obj = getValue(bean, name);
        if (obj == null) {
            return null;
        }
        try {
            return Integer.parseInt(obj.toString());
        } catch (Exception e) {
            return null;
        }
    }

    public static Integer getIntValue(Object bean, String name, Integer def) {

        Integer temp = getIntValue(bean, name);
        if (temp == null) {
            return def;
        } else {
            return temp;
        }
    }

    public static Long getLongValue(Object bean, String name) {

        Object obj = getValue(bean, name);
        if (obj == null) {
            return null;
        }
        try {
            return Long.parseLong(obj.toString());
        } catch (Exception e) {
            return null;
        }
    }

    public static Float getFloatValue(Object bean, String name) {

        Object obj = getValue(bean, name);
        if (obj == null) {
            return null;
        }
        try {
            return Float.parseFloat(obj.toString());
        } catch (Exception e) {
            return null;
        }
    }

    public static Long getLongValue(Object bean, String name, Long def) {

        Long l = getLongValue(bean, name);
        if (l == null) {
            return def;
        } else {
            return l;
        }
    }

    public static Object getValue(Object bean, String name) {
        if (bean == null) {
            return null;
        }
        try {
            return BeanUtils.getProperty(bean, name);
        } catch (Exception e) {
            return null;
        }
    }

    public static void setValue(Object bean, String name, Object value) {
        if (bean == null || StringUtils.isEmpty(name)) {
            return;
        }
        try {
            BeanUtils.setProperty(bean, name, value);
        } catch (Exception e) {

        }
    }

    public static void main(String[] args){
        System.out.println("CRITICAL!!!  The server has no beats!!!".length());
    }

}
