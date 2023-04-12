package com.yakoub.ea.enums;

public enum Difficulty {
    Easy("easy"), Medium("medium") , Difficult("difficult"), Varies("varies");
    private String label;

    private  Difficulty(String label) {
        this.label = label;
    }

    public static Difficulty findByLabel(String byLabel){
        for (Difficulty d : Difficulty.values()){
            if(d.label.equalsIgnoreCase(byLabel))
                return d;
        }
        return null;
    }
}
