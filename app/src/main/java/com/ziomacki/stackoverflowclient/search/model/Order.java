package com.ziomacki.stackoverflowclient.search.model;

public enum Order {

    ASCENDING("asc"),
    DESCENDING("desc");

    private final String value;

    Order(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static Order fromString(String sortString) {
        if (sortString != null) {
            for (Order order : Order.values()) {
                if (sortString.equalsIgnoreCase(order.value)) {
                    return order;
                }
            }
        }
        return DESCENDING;
    }
}
