Feature: Tournaments

  Scenario: It should create tournament
    Given I'm authenticated with role "admin"
    And I provide "name" with value "World championship 2021"
    And I provide "start_at" with value ""
    And I provide "name" with value "World championship 2021"
    When I try to create a tournament
    Then The response status should be "created"

