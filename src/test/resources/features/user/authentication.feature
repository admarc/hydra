Feature: Authentication
In order to use all features of the application
As a user
I want to be able to authenticate

Scenario: Logging in with invalid credentials
    Given I provide "identifier" with value "bobby"
    And I provide "password" with value "secret123"
    When I try to log in
    Then The response status should be "unauthorized"

Scenario: Successful logging in
    Given roles exist:
    | name          |
    | STANDARD_USER |
    And users exist:
    | username   | password | email                 | role          |
    | bobby      | ar2fR$   | booby@example.com     | STANDARD_USER |
    And I provide "identifier" with value "bobby"
    And I provide "password" with value "ar2fR$"
    When I try to log in
    Then The response status should be "successful"
    And "access token" should be in the response
