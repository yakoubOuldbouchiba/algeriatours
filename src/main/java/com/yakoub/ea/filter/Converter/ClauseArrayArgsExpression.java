package com.yakoub.ea.filter.Converter;

import com.yakoub.ea.filter.clause.Clause;
import com.yakoub.ea.filter.clause.ClauseArrayArgs;
import com.yakoub.ea.filter.clause.ClauseOneArg;
import com.yakoub.ea.filter.execptions.ClauseStructureExecption;
import com.yakoub.ea.filter.execptions.OperationNotFoundExecption;
import com.yakoub.ea.filter.execptions.ParamsRequirededExeception;

public class ClauseArrayArgsExpression implements Expression{
    @Override
    public Clause interpret(String context) throws ClauseStructureExecption, ParamsRequirededExeception, OperationNotFoundExecption {
        Clause clause = (new ClauseExpression()).interpret(context);
        String[] arr = context.split(";",3);
        return new ClauseArrayArgs(clause.getFiled(), clause.getOperation()  , arr[2].split("~"));
    }
}
