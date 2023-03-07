package com.yakoub.ea.filter.factory;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ValueFactory {
    public static Object toValue(Root root, String attribute, String value) throws ClassNotFoundException, ParseException {
        Class<?> type = getClass(root, attribute);
        return getValue(type, value);
    }

    public static Object toValue(Join join , String attribute, String value) throws ClassNotFoundException, ParseException {
        Class<?> type = getClass(join , attribute);
        return getValue(type, value);
    }

    private static Object getValue(Class<?> type, String value) throws ParseException{
        if(type == java.util.Date.class){
            return toDate(value);
        }
        if(type.isEnum()){
            return toEnum(type,value);
        }
        return value;
    }

    private static Class<?> getClass(Root expression, String attribute) throws ClassNotFoundException {
        String className = expression.get(attribute).getJavaType().getName();
        Class<?> myClass = Class.forName(className);
        return  myClass ;
    }

    private static Class<?> getClass(Join expression, String attribute) throws ClassNotFoundException {
        String className = expression.get(attribute).getJavaType().getName();
        Class<?> myClass = Class.forName(className);
        return  myClass ;
    }

    private static Date toDate(String value) throws  ParseException {
        DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        System.out.println(df1.parse(value.substring(0,19)));
        return df1.parse(value.substring(0,19));
    }

    private static Enum toEnum(Class type, String value)  {
        return Enum.valueOf(type, value);
    }


}
