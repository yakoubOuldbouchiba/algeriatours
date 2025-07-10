package com.yakoub.ea.filters.creator;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

public class JoinCreator {
    static public Join createJoin(Root root, String field) {
        String[] args = field.split("\\.");
        String attribute = args[args.length - 1];
        Join join = null;
        if (args.length > 1) {
            join = root.join(args[0] , JoinType.LEFT);
            for (int i = 1; i < args.length - 1; i++) {
                join = join.join(args[i] , JoinType.LEFT);
            }
            
            return join;
        }
        return null;
    }
}
