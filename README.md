#Pre-requisite Tools:

Apache Maven
JDK 8
#Project Structure:

pom.xml
src/test/java/com/hellofresh/challenge/constant : to list constant variable, like Base URL.
src/test/java/com/hellofresh/challenge/steps : to list all of the actions that will be performed.
src/test/java/com/hellofresh/challenge/utils : to list common actions that can be performed in any step class.
src/test/resources/log4j2.xml : logging properties
src/test/java/com/hellofresh/challenge/TestRunner.java : Test runner
src/test/java/com/hellofresh/challenge/stepdefinitions : consist of classes that can be used to map the story (scenario) in feature file with "steps" classes.
src/test/resources/features : feature file
#Note:

I used Serenity BDD (Thucydides) combine with Cucumber and it will create good graphical report. You can find anything about Serenity here : http://www.serenity-bdd.info/#/
As for the programming language, I used Java and Apache Maven as build tool.
To run the test : point to the project directory (where the pom.xml exist) - type "mvn clean verify" and enter
To check the report : /target/site/serenity/index.html
To check the log (if you put any logger) : /target/application.log