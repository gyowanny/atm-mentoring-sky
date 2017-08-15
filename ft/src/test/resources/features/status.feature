Feature: Status

  Scenario: Application is up and running
    When the user invokes the status endpoint
    Then response content type should be 'application/json' and status code 200
    And response body should contain status 'OK'