@tag
  Feature: Purchase the order

    Background:
      Given Landing on site url

    @Regression
    Scenario Outline: Positive test for purchasing order
      Given Logged in with username <name> and password <password>
      When Add product <productName> to cart
      And Checkout the same <productName> and click submit the order
      Then Message "Thankyou for the order." is displayed on confirmation page

      Examples:
      | name                   | password      | productName  |
      | ookbooir@gmail.com      | Ilovetest1!   | ZARA COAT 3 |