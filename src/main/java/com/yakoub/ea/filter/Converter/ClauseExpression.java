package com.yakoub.ea.filter.Converter;

import com.yakoub.ea.filter.clause.Clause;
import com.yakoub.ea.filter.enums.Operation;
import com.yakoub.ea.filter.execptions.ClauseStructureExecption;
import com.yakoub.ea.filter.execptions.OperationNotFoundExecption;

public class ClauseExpression implements Expression{
    @Override
    public Clause interpret(String context) throws ClauseStructureExecption, OperationNotFoundExecption {

        String[] arr = context.split(";",3);
        Operation operation = Operation.valueOfLabel(arr[1]);
        if(operation == null){
            throw new OperationNotFoundExecption("Operation Not Found Execption");
        }
        if(arr.length != 3 ){
            throw new ClauseStructureExecption("Clause Structure Execption");
        }
        return  new Clause(arr[0],operation);

    }
}
