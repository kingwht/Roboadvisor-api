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

## Running RoboAdvisor Service
Ensure docker is running with MySQL and Adminer containers running using the dev-tools.
`mvn spring-boot:run`

## Running RoboAdvisor Jar
`java -jar target/<roboadvisor jar>.jar`

## Swagger Docs
Once the service/jar is running, the swagger docs can be reached locally at  `localhost:5000/swagger-ui.html`
