package com.ziomacki.stackoverflowclient.search.model;

public class QueryParams {
    private String query;
    private Order order;
    private Sort sort;

    public QueryParams(String query, Order order, Sort sort) {
        this.query = query;
        this.order = order;
        this.sort = sort;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Sort getSort() {
        return sort;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }
}
