package com.example.rpl.RPL.other

import com.example.rpl.RPL.util.AbstractFunctionalSpec
import groovy.json.JsonOutput
import org.springframework.test.context.ActiveProfiles

import static javax.servlet.http.HttpServletResponse.SC_OK

@ActiveProfiles("test-functional")
class SwaggerConfigSpec extends AbstractFunctionalSpec {

    def "generateSwagger"() {
        when:
            def response = api.get("/v2/api-docs")

        then:
            assert response.contentType == "application/json"
            assert response.statusCode == SC_OK
            def result = getJsonResponse(response)

            assert result.swagger == "2.0"
            assert result.info.title == "RPL 2.0"

            def json_beauty = JsonOutput.prettyPrint(response.body.asString())
            File file = new File("swagger.json")
            file.write(json_beauty)

    }
}