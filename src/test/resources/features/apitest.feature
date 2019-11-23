Feature: Bookings

  Scenario: Create Random Booking and Verify it
    Given user wants to create "random" booking
    Then user verify recently created booking

  Scenario: Create multiple bookings with same room id and Verify it
    Given user wants to create "multiple" booking
    When user get recent multiple booking with same room id
    Then user verify multiple booking result
