Feature: Shopping list

  Scenario:
    Given Claire, an existing user
    When she creates a new list with title 'Apero tonight'
    Then she sees the new list in her shopping lists
