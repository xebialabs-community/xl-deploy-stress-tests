# XL Deploy Stress tests

There are two projects in this repository :

- Data Generator : an application that populates an XL Deploy instance with Applications, Environments and Infrastructure.
- Runner : an application that connects to an XL Deploy instance and performs stress tests.
- Selenium Browser: an application that connects to XL Deploy and performs web browser automation.

# CI status #

[![Build Status][xl-deploy-stress-tests-travis-image] ][xl-deploy-stress-tests-travis-url]
[![Codacy][xl-deploy-stress-tests-codacy-image] ][xl-deploy-stress-tests-codacy-url]
[![Code Climate][xl-deploy-stress-tests-code-climate-image] ][xl-deploy-stress-tests-code-climate-url]


[xl-deploy-stress-tests-travis-image]: https://travis-ci.org/xebialabs-community/xl-deploy-stress-tests.svg?branch=master
[xl-deploy-stress-tests-travis-url]: https://travis-ci.org/xebialabs-community/xl-deploy-stress-tests
[xl-deploy-stress-tests-codacy-image]: https://api.codacy.com/project/badge/Grade/f97410a1c91d4af4be5bbc5add23a17e
[xl-deploy-stress-tests-codacy-url]: https://www.codacy.com/app/rvanstone/xl-deploy-stress-tests
[xl-deploy-stress-tests-code-climate-image]: https://codeclimate.com/github/xebialabs-community/xl-deploy-stress-tests/badges/gpa.svg
[xl-deploy-stress-tests-code-climate-url]: https://codeclimate.com/github/xebialabs-community/xl-deploy-stress-tests



## Requirements

- Java 8 SDK
- XL Deploy 5.1.1 or greater

# Data Generator

The data generator should **not** be run against a production environment, as it will generate many CI's.

It should be run only once against a newly installed XL Deploy instance, running it several times on the same XL Deploy instance will result in errors.

## Running the data generator

The application can be started with the following command :

    ./gradlew :data-generator:run [parameters]

or on windows

    gradlew :data-generator:run [parameters]

In case you want to change the `Infrastructure` host, you can do this in the following file [application.conf](https://github.com/xebialabs-community/xl-deploy-stress-tests/blob/master/data-generator/src/main/resources/application.conf)

It uses the following optional parameters :

- **Base URL**: The URL of the XL Deploy server instance
    - Syntax : `-PbaseUrl=http://url.to.server:4516/deployit`
    - The default value iso `http://xldstressM.xebialabs.com:4516/deployit`
- **Username**: The username that will be used to connect to the server instance. This username needs "admin" permissions in order to populate data
    - Syntax : `-Pusername=admin`
    - The default value is `admin`
- **Password**: The password of the user account that will be used to connect to the server instance.
    - Syntax : `-Ppassword=password`
    - The default value is `admin`
- **nrOfMbPerArtifacts**: The number of Mb to be used for each artifact
    - Syntax : `-PnrOfMbPerArtifacts=100,200,400`
    - The default value is `100,200,400`

Example :

    ./gradlew :data-generator:run -PbaseUrl=http://localhost:4516/deployit -Pusername=admin -Ppassword=admin -PnrOfMbPerArtifacts=100,200
    
# Runner

The runner should **not** be run against a production environment.

It should be run against an XL Deploy Server on which the data-generator has already been run.

## Running

The application can be started with the following command :

    ./gradlew :runner:run [parameters]

or on windows

    gradlew :runner:run [parameters]

It uses the following optional parameters :

- **Server URL**: The URL of the XL Deploy server instance
    - Syntax : `-PbaseUrl=http://url.to.server:4516/deployit`
    - The default value is `http://xldstressM.xebialabs.com:4516/deployit`
- **Username**: The username that will be used to connect to the server instance. This username needs "admin" permissions in order to view all data
    - Syntax : `-Pusername=admin`
    - The default value is `admin`
- **Password**: The password of the user account that will be used to connect to the server instance.
    - Syntax : `-Ppassword=password`
    - The default value is `admin`
- **Simulation**: The simulations to execute (separated by a comma). If it is empty then `CopyFiles400Simulation` will run.
    - Syntax :
        - `-Psimulation=CopyFiles400Simulation` or
        - `-Psimulation=RunCommandsSimulation,ReadRepositorySimulation`
    - The possible values are :
        - `CopyFiles100Simulation`
        - `CopyFiles200Simulation`
        - `CopyFiles400Simulation`
        - `CopyFiles800Simulation`
        - `ReadRepositorySimulation`
        - `RunCommandsSimulation`
        - `DashboardReportSimulation`
    - The default value is `CopyFiles400Simulation`

Example:

    ./gradlew :runner:run -PbaseUrl=http://localhost:4516/deployit -Psimulation=CopyFiles400Simulation -Pusername=admin -Ppassword=admin


# Selenium Browser (Web Browser Automation Tests)

The Selenium browser should **not** be run against a production environment.

It should be run against an XL Deploy Server on which the data-generator has already been run. 
The best is however to have more diverse and at scale data, thus a copy of obfuscated production data.
The infrastructure for obfuscated data at scale is already available via [DEPL-13504](https://xebialabs.atlassian.net/browse/DEPL-13504).

Selenium browser tests use XPath expressions that have been tested against frontend of XL Deploy version 8.2.0 and higher.

## Running

The application can be started with the following command :

    ./gradlew :selenium-browser:run [parameters]

or on windows

    gradlew :selenium-browser:run [parameters]
    
It uses the following optional parameters :

- **Server URL**: The URL of the XL Deploy server instance
    - Syntax : `-PbaseUrl=http://url.to.server:4516/#/login`
    - The default value is `http://localhost:4516/#/login`
- **Username**: The username that will be used to connect to the server instance. This username needs "admin" permissions in order to view all data
    - Syntax : `-Pusername=admin`
    - The default value is `admin`
- **Password**: The password of the user account that will be used to connect to the server instance.
    - Syntax : `-Ppassword=admin`
    - The default value is `admin`

Example:

    ./gradlew :selenium-browser:run -PbaseUrl=http://localhost:4516/#/login -Pusername=admin -Ppassword=admin
    
    
## Logging 

The application logs performance characteristics of the page under test (e.g. resource load times) to file under 
root project directory `xl-deploy-stress-tests/log/perfmon_selenium.log` using slf4j and logback.

ChromeDriver supports performance logging, from which you can get events of domains "Timeline", "Network", and "Page", 
as well as trace data for specified trace categories. Documentation on Performance Log for ChromeDriver can be found 
[here](http://chromedriver.chromium.org/logging/performance-log)

