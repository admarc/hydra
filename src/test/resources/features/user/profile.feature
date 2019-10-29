Feature: User profile
In order manage my user profile
As a user
I want to be able fetch my profile data

Scenario: Unable to fetch profile without authorization
    Given roles exist:
    | name          |
    | STANDARD_USER |
    And users exist:
    | username   | password | email                 | role          |
    | bobby      | ar2fR$   | booby@example.com     | STANDARD_USER |
    When I try to fetch "bobby" profile
    Then The response status should be "unauthorized"

Scenario: Unable to fetch other users profiles
    Given roles exist:
    | name          |
    | STANDARD_USER |
    And users exist:
    | username   | password | email                 | role          |
    | bobby      | ar2fR$   | booby@example.com     | STANDARD_USER |
    | tom        | ar2fR$   | tom@example.com       | STANDARD_USER |
    And I'm authenticated as "bobby" with password "ar2fR$"
    When I try to fetch "tom" profile
    Then The response status should be "forbidden"

Scenario: Successful profile fetch
    Given roles exist:
    | name          |
    | STANDARD_USER |
    And users exist:
    | username   | password | email                 | role          |
    | bobby      | ar2fR$   | booby@example.com     | STANDARD_USER |
    And I'm authenticated as "bobby" with password "ar2fR$"
    When I try to fetch "bobby" profile
    Then The response status should be "successful"
    And "id" should be in the response
    And there should be "username" in the response with value "bobby"
    And there should be "email" in the response with value "booby@example.com"
    And "password" should not be in the response
