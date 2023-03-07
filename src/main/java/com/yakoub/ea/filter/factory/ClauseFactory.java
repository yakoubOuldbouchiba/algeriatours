package com.yakoub.ea.filter.factory;


import com.yakoub.ea.filter.Converter.ClauseArrayArgsExpression;
import com.yakoub.ea.filter.Converter.ClauseOneArgExpression;
import com.yakoub.ea.filter.Converter.ClauseTwoArgExpression;
import com.yakoub.ea.filter.Converter.Expression;
import com.yakoub.ea.filter.clause.Clause;
import com.yakoub.ea.filter.clause.ClauseArrayArgs;
import com.yakoub.ea.filter.clause.ClauseOneArg;
import com.yakoub.ea.filter.clause.ClauseTwoArgs;
import com.yakoub.ea.filter.enums.Operation;
import com.yakoub.ea.filter.execptions.ClauseStructureExecption;
import com.yakoub.ea.filter.execptions.OperationNotFoundExecption;
import com.yakoub.ea.filter.execptions.ParamsRequirededExeception;

public class ClauseFactory {

    public static Clause getClause(String filter) throws ClauseStructureExecption, ParamsRequirededExeception, OperationNotFoundExecption {
        System.out.println("new"+filter);
        String[] arr = filter.split(";",3);
        Operation operation = Operation.valueOfLabel(arr[1]);
        if(operation == Operation.Between){
            return new ClauseTwoArgExpression().interpret(filter);
        }
        if(operation != Operation.NotIn && operation != Operation.In ){
            return  new ClauseOneArgExpression().interpret(filter);
        }
        if(operation == Operation.NotIn || operation == Operation.In){
            return  new ClauseArrayArgsExpression().interpret(filter);
        }
        return new Clause();
    }


}
