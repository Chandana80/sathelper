# SAT Test System

Maven project to design REST end points for SAT Test System

This Project is build on top of ``spring-mvc-jpa-archetype`` and has following components:

 * HSQLDB
 * Logback logging
 * TestNG
 * Environment for functional tests
 * Deployment via Cargo plugin
 * Angular JS as rest client
 * Spring Security for Authentication and Authorization
 * For emails used fake SMTP server

## Building and Running the project

Get the project from github using git clone. Navigate to root directory and execute following command:

    $ mvn install

Navigate to webapp directory and execute the following command:

    $ mvn tomcat:run


## UI

    *Navigate to http://localhost:8080/sathelper/pages/index.html#/login in your browser
    *Click on register link to register Student or Teacher
    *After registering login as teacher to create test and enter the results.

## Design

    Entities
        *User
        *Role
        *Subject
        *Result
        *TestResult

    Rest End Points
        *TestResultController
        *UsersController

## TODO
    * Implement Caching
    * More functional Tests
    

