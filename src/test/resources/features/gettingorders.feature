# new feature
# Tags: optional

Feature: Getting orders

  Scenario: User call web service to get order by parameter
    Given user wants to get information regarding "2" orders with state "ordered" from "1" page
    When user retrieves the order by parameters
    Then the status code should be "200"
    Then the response time should be less than 2000 ms