@tag
Feature: login error validation

  @ErrorValidation
  Scenario Outline: negative test for login
    Given Landing on site url
    When Logged in with username <name> and password <password>
    Then Error message "Incorrect email or password." is displayed

    Examples:
      | name                   | password      |
      | oobooir@gmail.com      | Ilovetest1!   |