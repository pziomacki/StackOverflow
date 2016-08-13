package com.ziomacki.stackoverflowclient.search.model

import android.content.SharedPreferences
import rx.observers.TestSubscriber
import spock.lang.Specification

class QueryParamsRepositorySpec extends Specification {

    def "save all query params"() {
        given:
            QueryParams queryParams = new QueryParams.Builder().build();
            SharedPreferences.Editor editorMock = Mock(SharedPreferences.Editor);
            SharedPreferences sharedPreferencesStub = Stub(SharedPreferences);
            sharedPreferencesStub.edit() >> editorMock;
            QueryParamsRepository sut = new QueryParamsRepository(sharedPreferencesStub);
        when:
            sut.saveQueryParams(queryParams);
        then:
            1 * editorMock.putString("key_query",  _ as String);
            1 * editorMock.putString("key_order",  _ as String);
            1 * editorMock.putString("key_sort",  _ as String);
            1 * editorMock.apply();
    }

    def "get default QueryParams if not previouslu saved"() {
        given:
            SharedPreferences sharedPreferencesStub = Stub(SharedPreferences);
            sharedPreferencesStub.getString("key_query", "") >> "";
            sharedPreferencesStub.getString("key_order", "") >> "";
            sharedPreferencesStub.getString("key_sort", "") >> "";
            QueryParamsRepository sut = new QueryParamsRepository(sharedPreferencesStub);
        when:
            QueryParams queryParams = sut.getQueryParams();
        then:
            queryParams.order== Order.DESCENDING;
            queryParams.sort == Sort.ACTIVITY;
            queryParams.query.equals("");
    }

    def "get saved values for QueryParams"() {
        given:
            SharedPreferences sharedPreferencesStub = Stub(SharedPreferences);
            sharedPreferencesStub.getString("key_query", "") >> "test_query";
            sharedPreferencesStub.getString("key_order", "") >> "asc";
            sharedPreferencesStub.getString("key_sort", "") >> "relevance";
            TestSubscriber<QueryParams> subscriber = new TestSubscriber<>();
            QueryParamsRepository sut = new QueryParamsRepository(sharedPreferencesStub);
        when:
            sut.getQueryParamsObservable().subscribe(subscriber);
            QueryParams queryParams = subscriber.getOnNextEvents().get(0);
        then:
            queryParams.order== Order.ASCENDING;
            queryParams.sort == Sort.RELEVANCE;
            queryParams.query.equals("test_query");
    }
}
