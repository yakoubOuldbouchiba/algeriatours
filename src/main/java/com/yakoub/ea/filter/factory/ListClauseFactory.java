package com.yakoub.ea.filter.factory;


import com.yakoub.ea.filter.clause.Clause;
import com.yakoub.ea.filter.execptions.ClauseStructureExecption;
import com.yakoub.ea.filter.execptions.OperationNotFoundExecption;
import com.yakoub.ea.filter.execptions.ParamsRequirededExeception;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ListClauseFactory {
    public static List<Clause> getClauses(List<String> filters) {
        if(filters == null) return  new ArrayList<>();
        return filters.stream().map(filter -> {
            try {
                return ClauseFactory.getClause(filter);
            } catch (ClauseStructureExecption | ParamsRequirededExeception |OperationNotFoundExecption e ) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }
}


