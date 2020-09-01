# new feature
# Tags: optional

Feature: Create customer

  @CreateCustomer
  Scenario: User able to create customer
    Given user wants to create customer with first name "Steve" and last name "McLaren"
    When users submits POST request to the endpoint
    Then the status code should be "201"
    Then the response time should be less than 2000 ms

  @GetCustomerById
  Scenario: User able to get information about customer by ID
    When user submits GET request to the endpoint
    Then the status code should be "200"
    Then the response time should be less than 2000 ms

  @UpdateCustomer
  Scenario: User able to update customer's name
    Given user wants update customers name with new first name "Peter"
    When user submits PATCH request to the endpoint
    Then the status code should be "200"
    Then the response time should be less than 2000 ms

  @CreateOrder
  Scenario: User able to create order for customer
    Given user adds parameter "customer_url" into POST request body
    When user sends POST request to the endpoint
    Then the status code should be "201"
    Then the response time should be less than 2000 ms

  @RemoveOrder
  Scenario: User able to delete the order
    Given user submits DELETE request to delete the order
    Then the status code should be "200"
    Then the response time should be less than 2000 ms

  @RemoveCustomer
  Scenario: User able to delete the customer
    Given user submits DELETE request to delete the customer
    Then the status code should be "200"
    Then the response time should be less than 2000 ms

