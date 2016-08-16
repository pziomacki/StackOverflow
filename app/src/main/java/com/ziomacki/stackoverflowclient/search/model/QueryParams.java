package com.ziomacki.stackoverflowclient.search.model;

public class QueryParams {
    private String query;
    private Order order;
    private Sort sort;

    private QueryParams(Builder builder) {
        this.query = builder.query;
        this.order = builder.order;
        this.sort = builder.sort;
    }

    public static final class Builder {
        private String query = "";
        private Order order = Order.DESCENDING;
        private Sort sort = Sort.ACTIVITY;

        public Builder() {
        }

        public Builder query(String val) {
            query = val;
            return this;
        }

        public Builder order(Order val) {
            order = val;
            return this;
        }

        public Builder sort(Sort val) {
            sort = val;
            return this;
        }

        public QueryParams build() {
            return new QueryParams(this);
        }
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

    public Sort getSort() {
        return sort;
    }
}
