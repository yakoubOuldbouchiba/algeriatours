package com.yakoub.ea.filter.creator;

public class AttribueCreator {
    static public String createAttribute(String field){
        System.out.println(field);
        String[] args =  field.split("\\.");
        String attribute = args[args.length-1];
        return attribute;
    }
}
