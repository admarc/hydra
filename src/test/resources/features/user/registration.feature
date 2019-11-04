Feature: Registering accounts
In order to have an identity in appliaction
As a user
I want to be able to create an account

Scenario: Successfully creating an account
    Given I provide "email" with value "booby@example.com"
    And I provide "username" with value "bobby"
    And I provide "password" with value "secret123"
    When I try to create an account
    Then The response status should be "successful"
    And "id" should be in the response
    And there should be "username" in the response with value "bobby"
    And there should be "email" in the response with value "booby@example.com"

Scenario: Invalid data while creating user
    Given users exist:
    | username   | password | email                 |
    | bobby      | 123456   | my.emaial@example.com |
    And I provide "email" with value "my.emaial@example.com"
    And I provide "username" with value "bobby"
    When I try to create an account
    Then The response status should be "invalid"
    And error message for "email" should say "UniqueProperty"
    And error message for "username" should say "UniqueProperty"
    And error message for "password" should say "NotBlank"

