package com.ziomacki.stackoverflowclient.search.model;

import com.ziomacki.stackoverflowclient.component.RealmWrapper;
import javax.inject.Inject;
import io.realm.Realm;
import rx.Observable;
import rx.Subscriber;

public class SearchResultsRepository {

    private RealmWrapper realmWrapper;

    @Inject
    public SearchResultsRepository(RealmWrapper realmWrapper) {
        this.realmWrapper = realmWrapper;
    }

    public void storeResults(final SearchResults results) {
        Realm realm = realmWrapper.getRealmInstance();
        realm.beginTransaction();
        realm.delete(SearchResults.class);
        realm.commitTransaction();
        realm.beginTransaction();
        realm.copyToRealm(results);
        realm.commitTransaction();
        realm.close();
    }

    public Observable<SearchResults> getSearchResults() {
        return Observable.create(new Observable.OnSubscribe<SearchResults>() {
            @Override
            public void call(Subscriber<? super SearchResults> subscriber) {
                Realm realm = realmWrapper.getRealmInstance();
                SearchResults realmResults = realm.where(SearchResults.class).findFirst();
                if (realmResults == null) {
                    subscriber.onNext(new SearchResults());
                } else {
                    subscriber.onNext(realm.copyFromRealm(realmResults));
                }
                realm.close();
            }
        });
    }
}
