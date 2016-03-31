# XL Deploy Stress tests

There are two projects in this repository :

- Data Generator : an application that populates an XL Release instance with active, completed releases and templates.
- Runner : an application that connects to an XL Release instance and performs stress tests.

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

It uses the following optional parameters :

- **Base URL**: The URL of the XL Deploy server instance
    - Syntax : `-PbaseUrl=http://url.to.server:4516/deployitoo`
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
        - `CopyFiles100Simulation` : several development teams commit code which triggers new releases. Each teams consists of ~10 developers.
        - `CopyFiles200Simulation` : several ops people are working with XL Release
        - `CopyFiles400Simulation` : several release managers are working with XL Release
        - `CopyFiles800Simulation` : A simulation which combines several roles of people working with XL Release in one realistic usage scenario.
        - `ReadRepositorySimulation` : A simulation which combines several roles of people working with XL Release in one realistic usage scenario.
        - `RunCommandsSimulation` : A simulation which combines several roles of people working with XL Release in one realistic usage scenario.
    - The default value is `CopyFiles400Simulation`

Example:

    ./gradlew :runner:run -PbaseUrl=http://localhost:4516/deployit -Psimulation=CopyFiles400Simulation -Pusername=admin -Ppassword=admin


