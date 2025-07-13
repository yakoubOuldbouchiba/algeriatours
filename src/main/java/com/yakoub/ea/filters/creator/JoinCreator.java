package com.yakoub.ea.filters.creator;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

public class JoinCreator {
    static public Join<?, ?>  createJoin(Root<?> root, String field) {
        if (field == null || field.isBlank()) return null;

        String[] parts = field.split("\\.");
        if (parts.length <= 1) return null; // No join needed

        Join<?, ?> join = root.join(parts[0], JoinType.LEFT);
        for (int i = 1; i < parts.length - 1; i++) {
            join = join.join(parts[i], JoinType.LEFT);
        }

        return join;
    }
}
