Feature: Getting category

  @GetCategoryByID
  Scenario: GET request for https://api.predic8.de:443/shop/categories/{id} with invalid categoryID returns status code 200
    Given user adds "----" id as query parameter for GET categories by ID
    When user submits GET request to the get category by ID endpoint
    Then the status code should be "200"
    Then the response time should be less than 2000 ms