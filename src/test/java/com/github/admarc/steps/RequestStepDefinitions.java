package com.github.admarc.steps;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import static com.tngtech.keycloakmock.api.ServerConfig.aServerConfig;
import static com.tngtech.keycloakmock.api.TokenConfig.aTokenConfig;
import com.tngtech.keycloakmock.api.KeycloakMock;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.LocalServerPort;

import static org.hamcrest.Matchers.*;

public class RequestStepDefinitions {

    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_RESET = "\u001B[0m";

    private DatabaseStepDefinitions databaseStepDefinitions;

    public static final String url = "http://localhost";
    @Value( "${keycloak.resource}" )
    private String resource;

    private RequestSpecification request;
    private JSONObject requestParams;
    private Response response;

    static KeycloakMock mock = new KeycloakMock(aServerConfig().withPort(8080).withHostname("keycloak").withRealm("hydra").build());

    RequestStepDefinitions(DatabaseStepDefinitions databaseStepDefinitions) {
        this.databaseStepDefinitions = databaseStepDefinitions;
        request = RestAssured.given().contentType("application/json\r\n");
        requestParams = new JSONObject();
    }

    static {
        mock.start();
    }

    private String getUrl() {
        return url + ":" + this.port + "/";
    }

    private void makeRequest(Method method, String url) {
        if(requestParams.length() != 0) {
            request.body(requestParams.toString());
        }

        response = request.when().request(method, getUrl() + url);
    }

    @When("I try to create a tournament")
    public void i_try_to_create_a_tournament() {
        makeRequest(Method.POST, "tournaments");
    }


    @Given("I provide {string} with value {string}")
    public void i_provide_with_value(String key, String value) throws JSONException {
        requestParams.put(phraseToFieldName(key), value);
    }

    @Then("The response status should be {string}")
    public void the_response_status_should_be(String expectedStatus) {
        expectedStatus  = expectedStatus.replaceAll(" ", "_").toUpperCase();

        response.then().statusCode(HttpStatus.valueOf(expectedStatus).getStatusCode());
    }

    @Then("{string} should be in the response")
    public void should_be_in_the_response(String key) {
        response.then().body("$", hasKey(phraseToFieldName(key)));
    }

    @Then("error message for {string} should say {string}")
    public void error_message_for_should_say(String fieldName, String errorMessage) {
        JsonObject responseObject = new JsonParser().parse(response.asString()).getAsJsonObject();

        JsonObject errorObject = findJsonObjectByValue(responseObject.get("errors").getAsJsonArray(), fieldName);

        assert(errorObject.get("code").toString().replaceAll("\"", "").equals(errorMessage));
    }

    @Then("there should be {string} in the response with value {string}")
    public void there_should_be_in_the_response_with_value(String key, String value) {
        response.then().body(key, equalTo(value));
    }

    @Then("{string} should not be in the response")
    public void should_not_be_in_the_response(String key) {
        response.then().body("$", not(hasKey(key)));
    }

    private JsonObject findJsonObjectByValue(JsonArray jsonArray, String value) {
        for(int i=0; i < jsonArray.size(); i++) {
            JsonObject object = jsonArray.get(i).getAsJsonObject();

            if (object.get("field").toString().replaceAll("\"", "").equals(value)) {
                return object;
            }
        }
        throw new RuntimeException(String.format("Unable find object in array with value: '%s'", value));
    }

    private String phraseToFieldName(String phrase) {
        String[] split = phrase.split(" ");
        StringBuilder fieldNameBuilder = new StringBuilder();
        for (int i = 0; i < split.length; i++) {
            String firstLetter = split[i].substring(0, 1).toLowerCase();
            fieldNameBuilder.append(i == 0 ? firstLetter.toLowerCase() : firstLetter.toUpperCase());
            fieldNameBuilder.append(split[i].substring(1));
        }

        return fieldNameBuilder.toString();
    }

    public void setHeader(String name, String value) {
        request.header(name, value);
    }

    @After
    public void dumpResponseOnFail(Scenario scenario) {
        if (scenario.isFailed()) {
            System.out.println(ANSI_PURPLE + "======================== RESPONSE ========================" + ANSI_RESET);
            System.out.println("Response status code: " + response.statusCode());
            response.getBody().prettyPrint();
            System.out.println(ANSI_PURPLE + "==================== END OF RESPONSE =====================" + ANSI_RESET);
        }
    }

    @Given("I'm authenticated with role {string}")
    public void i_m_authenticated_with_role(String roleName) {
        request.auth()
                .preemptive()
                .oauth2(mock.getAccessToken(aTokenConfig().withResourceRole(resource, roleName).build()));
    }

    @LocalServerPort private int port;


    @When("I test")
    public void i_test() {
        response = request
                .when()
                .get(getUrl() + "api/vip");
    }

}