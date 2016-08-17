package com.ziomacki.stackoverflowclient.component;

import javax.inject.Inject;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class RealmWrapper {

    private RealmConfiguration realmConfiguration;

    @Inject
    public RealmWrapper(RealmConfiguration realmConfiguration) {
        this.realmConfiguration = realmConfiguration;
    }

    public Realm getRealmInstance() {
        return Realm.getInstance(realmConfiguration);
    }
}
