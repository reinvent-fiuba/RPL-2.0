package com.example.rpl.RPL.controller


import com.example.rpl.RPL.util.AbstractFunctionalSpec
import org.springframework.test.context.ActiveProfiles

import static jakarta.servlet.http.HttpServletResponse.SC_OK

@ActiveProfiles("test-functional")
class HealthControllerFunctionalSpec extends AbstractFunctionalSpec {

    def "Ping must return pong"() {
        when: "get ping url"
            def response = get("/api/health")

        then: "must return a plain text containing pong"
            response.statusCode == SC_OK
            response.body.asString() == "pong"
    }
}
