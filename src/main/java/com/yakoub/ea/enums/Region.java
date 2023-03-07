package com.yakoub.ea.enums;

public enum Region {
    North("north"),
    South("south"),
    East("east"),
    West("west");
    private String label;

    private  Region(String label) {
        this.label = label;
    }

    public static Region findByLabel(String byLabel){
        for (Region r : Region.values()){
            if(r.label.equalsIgnoreCase(byLabel))
            return r;
        }
        return null;
    }
}
