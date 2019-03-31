# RoboAdvisor-api

## Overview 
RoboAdvisor Fund Rebalancer is an Web API that customers can use to manage their portfolio rebalancing preferences and execute portfolio rebalances.

## Requirements
- Java 8
- Docker containers with dev services (use dev-tools for faster setup)
    - MySQL
    - Adminer

## Build
Build with unit test only:
`mvn clean package`

## Integration Test
Run Springboot Integration test ONLY. Please ensure dev-tools is up and running.
mvn verify -DskipSurefire

## Running RoboAdvisor Service
Ensure docker is running with MySQL and Adminer containers running using the dev-tools.
`mvn spring-boot:run`

## Running RoboAdvisor Jar
`java -jar target/<roboadvisor jar>.jar`

## Swagger Docs
Once the service/jar is running, the swagger docs can be reached locally at  `localhost:5000/swagger-ui.html`

## SonarQube
Sonarqube static code analysis is enabled for this project.
To run scan locally, ensure that the necessary Docker services are running in dev-tools.
If you have not obtained a Sonarqube user token, please access `localhost:9000` and create a user. You will be provided a user token.

Sonarqube scanning can be initiated by running this command:
`mvn sonar:sonar -Dsonar.projectKey=roboadvisor-api -Dsonar.host.url=http://localhost:9000 -Dsonar.login=<user token>`
