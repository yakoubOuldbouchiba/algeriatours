package com.yakoub.ea.filter.Converter;

import com.yakoub.ea.filter.clause.Clause;
import com.yakoub.ea.filter.execptions.ClauseStructureExecption;
import com.yakoub.ea.filter.execptions.OperationNotFoundExecption;
import com.yakoub.ea.filter.execptions.ParamsRequirededExeception;

public interface Expression {

    public Clause interpret(String context)  throws ClauseStructureExecption, ParamsRequirededExeception, OperationNotFoundExecption;
}
