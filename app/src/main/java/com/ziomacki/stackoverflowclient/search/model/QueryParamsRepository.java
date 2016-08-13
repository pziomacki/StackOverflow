package com.ziomacki.stackoverflowclient.search.model;

import android.content.SharedPreferences;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;

public class QueryParamsRepository {

    private static final String KEY_QUERY = "key_query";
    private static final String KEY_ORDER = "key_order";
    private static final String KEY_SORT = "key_sort";

    private SharedPreferences sharedPreferences;

    @Inject
    public QueryParamsRepository(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public void saveQueryParams(QueryParams queryParams) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_QUERY, queryParams.getQuery());
        editor.putString(KEY_ORDER, queryParams.getOrder().toString());
        editor.putString(KEY_SORT, queryParams.getSort().toString());
        editor.apply();
    }

    private QueryParams getQueryParams() {
        QueryParams.Builder builder = new QueryParams.Builder();
        String orderString = sharedPreferences.getString(KEY_ORDER, "");
        if (!orderString.equals("")) {
            builder.order(Order.fromString(orderString));
        }
        String sortString = sharedPreferences.getString(KEY_SORT, "");
        if (!sortString.equals("")) {
            builder.sort(Sort.fromString(sortString));
        }
        builder.query(sharedPreferences.getString(KEY_QUERY, ""));
        return builder.build();
    }

    public Observable<QueryParams> getQueryParamsObservable() {
        return Observable.create(new Observable.OnSubscribe<QueryParams>() {
            @Override
            public void call(Subscriber<? super QueryParams> subscriber) {
                subscriber.onNext(getQueryParams());
            }
        });
    }

}
