package com.yakoub.ea.filter.clause;


import com.yakoub.ea.filter.enums.Operation;

public class Clause {
    private String filed;
    private Operation operation;


    public Clause() {
    }

    public Clause(String filed, Operation operation) {
        this.filed = filed;
        this.operation = operation;
    }

    public String getFiled() {
        return filed;
    }

    public void setFiled(String filed) {
        this.filed = filed;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

}
