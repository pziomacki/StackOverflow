package com.ziomacki.stackoverflowclient.search.model

import spock.lang.Specification

class QueryValidatorSpec extends Specification {

    def "empty query for null string"() {
        given:
            QueryValidator queryValidator = new QueryValidator();
            String testQuery;
        when:
            int isValid = queryValidator.isQueryValid(testQuery);
        then:
           isValid == 1;
    }

    def "empty query for empty string"(){
        given:
            QueryValidator queryValidator = new QueryValidator();
            String testQuery = "";
        when:
            int isValid = queryValidator.isQueryValid(testQuery);
        then:
            isValid == 1;
    }

    def "query is fine non empty string"(){
        given:
            QueryValidator queryValidator = new QueryValidator();
            String testQuery = "test";
        when:
            int isValid = queryValidator.isQueryValid(testQuery);
        then:
            isValid == 0;
    }

}
