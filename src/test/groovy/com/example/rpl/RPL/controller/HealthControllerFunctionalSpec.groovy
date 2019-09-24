package com.example.rpl.RPL.controller


import com.example.rpl.RPL.util.AbstractFunctionalSpec
import org.springframework.test.context.ActiveProfiles

import static javax.servlet.http.HttpServletResponse.SC_OK

@ActiveProfiles("test-functional")
class HealthControllerFunctionalSpec extends AbstractFunctionalSpec {

    def "Ping must return pong"() {
        when: "get ping url"
            def response = get("/health")

        then: "must return a plain text containing pong"
            response.contentType == "plain/text;charset=UTF-8"
            response.statusCode == SC_OK
            response.body.asString() == "pong"
    }
}


