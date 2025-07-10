package com.yakoub.ea.filters.specification;

import lombok.Data;

@Data
public class SearchSort {
    private String selector;
    private boolean desc;

    public SearchSort() {
    }
}
