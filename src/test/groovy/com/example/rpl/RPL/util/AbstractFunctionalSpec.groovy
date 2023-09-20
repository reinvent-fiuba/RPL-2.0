package com.example.rpl.RPL.util

import groovy.json.JsonSlurper
import io.restassured.RestAssured
import io.restassured.config.HeaderConfig
import io.restassured.http.ContentType
import io.restassured.response.Response
import io.restassured.specification.RequestSpecification
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import spock.lang.Shared

import jakarta.servlet.http.HttpServletResponse

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class AbstractFunctionalSpec extends AbstractSpec {

    @Shared
    RequestSpecification api

    @LocalServerPort
    int randomServerPort

    def setup() {
        RestAssured.port = randomServerPort
        RestAssured.baseURI = "http://localhost"
        api = RestAssured.given().config(RestAssured.config().headerConfig(
                // Override Authorization header for multiple users in same test
                HeaderConfig.headerConfig().overwriteHeadersWithName("Authorization")
            )).contentType(ContentType.JSON)
    }

    def authenticate(String username = null, String password = null) {
        if (username != null && password != null) {
            Map body = [username_or_email: username, password: password]
            def loginResponse = getJsonResponse(post("/api/auth/login", body))
            api.header("Authorization", "${loginResponse.token_type} ${loginResponse.access_token}")
        }
    }

    def get(String url, String username = null, String password = null) {
        authenticate(username, password)
        return api.get(url)
    }

    def get(String url, Map<String, String> headers) {
        return api.headers(headers).get(url)
    }

    def post(String url, def body, String username = null, String password = null) {
        authenticate(username, password)
        api.body(body)
        return api.post(url)
    }

    def post(String url, def body, Map<String, String> headers) {
        api.body(body)
        return api.headers(headers).post(url)
    }

    def put(String url, def body, String username = null, String password = null) {
        authenticate(username, password)
        if (body != null)
            api.body(body)

        return api.put(url)
    }

    def patch(String url, def body, String username = null, String password = null) {
        authenticate(username, password)
        if (body != null)
            api.body(body)

        return api.patch(url)
    }

    def delete(String url, String username = null, String password = null) {
        authenticate(username, password)
        return api.delete(url)
    }

    def getJsonResponse(Response response) {
        String jsonAsString = response.asString()
        def jsonResponse = new JsonSlurper().parseText(jsonAsString)

        return jsonResponse
    }

    void assertOK(Response response) {
        assert response.statusCode == HttpServletResponse.SC_OK
    }

    void assertNoContent(Response response) {
        assert response.statusCode == HttpServletResponse.SC_NO_CONTENT
    }

    void assertCreated(Response response) {
        assert response.statusCode == HttpServletResponse.SC_CREATED
    }

    void assertAccepted(Response response) {
        assert response.statusCode == HttpServletResponse.SC_ACCEPTED
    }

    void assertForbiddenResponse(Response response, message, error) {
        assert response.statusCode == HttpServletResponse.SC_FORBIDDEN

        def result = getJsonResponse(response)
        def resultError = result.error
        def resultMessage = result.message
        def status = result.status

        assert status == HttpServletResponse.SC_FORBIDDEN

        assert resultError != null
        assert error == resultError
        assert resultMessage != null
        assert message == resultMessage
    }

    void assertBadRequestResponse(Response response, message, error) {
        assert response.statusCode == HttpServletResponse.SC_BAD_REQUEST

        def result = getJsonResponse(response)
        def resultError = result.error
        def resultMessage = result.message
        def status = result.status

        assert status == HttpServletResponse.SC_BAD_REQUEST

        assert resultError != null
        assert error == resultError
        assert resultMessage != null
        assert message == resultMessage
    }

    void assertNotFoundResponse(Response response, message, error) {
        assert response.statusCode == HttpServletResponse.SC_NOT_FOUND

        def result = getJsonResponse(response)
        def resultError = result.error
        def resultMessage = result.message
        def status = result.status

        assert status == HttpServletResponse.SC_NOT_FOUND
        assert resultError != null
        assert error == resultError
        assert resultMessage != null
        assert message == resultMessage
    }

    void assertInternalServerError(Response response, message, error) {
        assert response.statusCode == HttpServletResponse.SC_INTERNAL_SERVER_ERROR

        def result = getJsonResponse(response)
        def resultError = result.error
        def resultMessage = result.message
        def status = result.status

        assert status == HttpServletResponse.SC_INTERNAL_SERVER_ERROR

        assert resultError != null
        assert error == resultError
        assert resultMessage != null
        assert message == resultMessage
    }
}
