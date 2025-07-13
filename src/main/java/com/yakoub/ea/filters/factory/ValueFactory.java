package com.yakoub.ea.filters.factory;



import com.yakoub.ea.config.converter.DateConvertor;


import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.text.ParseException;
import java.time.LocalDate;

public class ValueFactory {

    public static Object toValue(Root<?> root, String attribute, String value) throws Exception {
        Path<?> path = root.get(attribute);
        Class<?> type = path.getJavaType();
        return getValue(type, value);
    }

    public static Object toValue(Join<?, ?> join, String attribute, String value) throws Exception {
        Path<?> path = join.get(attribute);
        Class<?> type = path.getJavaType();
        return getValue(type, value);
    }

    public static Object toValue(From<?, ?> join, String attribute, String value) throws Exception {
        Path<?> path = join.get(attribute);
        Class<?> type = path.getJavaType();
        return getValue(type, value);
    }

    private static Object getValue(Class<?> type, String value) throws ParseException {
        if (value == null || value.isBlank()) return null;

        System.out.printf("Resolving type: %s for value: %s%n", type.getSimpleName(), value);

        if (type == LocalDate.class) return toDate(value);
        if (type.isEnum()) return toEnum(type, value);
        if (type == Boolean.class || type == boolean.class) return Boolean.parseBoolean(value);
        if (type == Integer.class || type == int.class) return Integer.valueOf(value);
        if (type == Long.class || type == long.class) return Long.valueOf(value);
        if (type == Double.class || type == double.class) return Double.valueOf(value);
        if (type == Float.class || type == float.class) return Float.valueOf(value);

        return value;
    }





    private static LocalDate toDate(String value) throws ParseException {
        return DateConvertor.getObject(value, "yyyy-MM-dd");
    }
    @SuppressWarnings("unchecked")
    private static Enum toEnum(Class type, String value) {
        return Enum.valueOf(type, value);
    }


}
