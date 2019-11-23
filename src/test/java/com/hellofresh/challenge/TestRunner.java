package com.hellofresh.challenge;

import cucumber.api.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

/**
 * Created by sekarayukarindra.
 */

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        features="src/test/resources/features",
        plugin = {"pretty"}
)
public class TestRunner {
}
