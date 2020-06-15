package com.example.rpl.RPL.util

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.transaction.PlatformTransactionManager
import spock.lang.Specification

import javax.persistence.EntityManager

abstract class AbstractSpec extends Specification {

    @Autowired
    EntityManager entityManager
    @Autowired
    PlatformTransactionManager transactionManager

    void assertNotNull(def value) {
        assert value != null
    }

    void assertNull(def value) {
        assert value == null
    }

    void assertEquals(def valueDefault, def value) {
        assert valueDefault == value
    }

    String underscoreToCamelCase(String underscore){
        if(!underscore || underscore.isAllWhitespace()){
            return ''
        }
        return underscore.replaceAll(/_\w/){ it[1].toUpperCase() }
    }
}