package com.hellofresh.challenge.stepdefinitions;

import com.hellofresh.challenge.steps.BookingSteps;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import net.thucydides.core.annotations.Steps;

/**
 * Created by sekarayukarindra.
 */
public class BookingStepDefinitions {

    @Steps
    BookingSteps bookingSteps;

    @Then("^user verify recently created booking$")
    public void userVerifyRecentlyCreatedBooking() {
        bookingSteps.verifyRecentlyCreatedBooking();
    }

    @Given("^user wants to create \"([^\"]*)\" booking$")
    public void userWantsToCreateBooking(String arg0) {
        bookingSteps.createBooking(arg0);
    }

    @When("^user get recent multiple booking with same room id$")
    public void userGetMultipleBookingWithSameRoomId() {
        bookingSteps.getRecentBookingByRoomid();
    }

    @Then("^user verify multiple booking result$")
    public void userVerifyMultipleBookingResult() {
        bookingSteps.verifyMultiBooking();
    }
}
