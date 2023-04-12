package com.yakoub.ea.filter.Converter;

import com.yakoub.ea.filter.clause.Clause;
import com.yakoub.ea.filter.clause.ClauseTwoArgs;
import com.yakoub.ea.filter.execptions.ClauseStructureExecption;
import com.yakoub.ea.filter.execptions.OperationNotFoundExecption;
import com.yakoub.ea.filter.execptions.ParamsRequirededExeception;

public class ClauseTwoArgExpression implements Expression{
    @Override
    public Clause interpret(String context) throws ClauseStructureExecption, ParamsRequirededExeception, OperationNotFoundExecption {
        Clause clause = (new ClauseExpression()).interpret(context);
        String[] arr = context.split(";",3);
        String[] args = arr[2].split("~" , 2);
        if(args.length != 2 ){
            throw new ClauseStructureExecption("Clause Structure Execption");
        }
        if(args[0].isEmpty() || args[1].isEmpty()){
            throw new ParamsRequirededExeception("Params Requireded Exeception");
        }
        return  new ClauseTwoArgs(clause.getFiled(), clause.getOperation()  , args[0], args[1]);
    }
}
