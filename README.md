# HSBC - RoboAdvisor Fund Rebalancer API
RoboAdvisor Fund Rebalancer is an Web API that customers can use to manage their portfolio rebalancing preferences and execute portfolio rebalances.

## Requirements
- Java 8
- [dev-tools](https://gitlab.com/cpsc319-2018w2/hsbc/formation/dev-tools) docker services running
    - MySQL
    - Adminer
    - SonarQube

# Local Development
### Build
`mvn clean package`

### Integration Test
`mvn -DskipSurefire verify`

### Running RoboAdvisor Service Locally
`mvn spring-boot:run`

### Swagger Docs
Once the servicer is running, the swagger docs can be reached locally at `localhost:5000/swagger-ui.html`

### SonarQube
Sonarqube static code analysis is enabled for this project.
If you have not obtained a Sonarqube user token, please access `localhost:9000` and follow the on-screen wizard to create a user. A user token will be generated.

Sonarqube command: `mvn sonar:sonar -Dsonar.projectKey=roboadvisor-api -Dsonar.host.url=http://localhost:9000 -Dsonar.login=<user token>`

# Deployment to Google Cloud Platform
Please complete the UI deployment before continuing with backend deployment. The UI deployment can be found [here](https://gitlab.com/cpsc319-2018w2/hsbc/formation/roboadvisor-ui).
## Setting up the Project
1. Recall the MySQL connection string noted earlier.
2. Edit roboadvisor-api/src/main/resources/application-cloud.properties by updating `spring.datasource.url` value:
    `jdbc:mysql://google/roboadvisor?cloudSqlInstance=<Your database connection string>&socketFactory=com.google.cloud.sql.mysql.SocketFactory`

## Setting up the Service Account
1. Recall the JSON key used for service account noted earlier.
2. In Gitlab, **Repository Settings > CI/CD > Environment variables > Add new variable**  
    - Input variable key: `DEPLOY_KEY_FILE_PRODUCTION`
    - Input variable value: `<GCP Service Account Private Key JSON>`
3. Add the project name environment variable:
    - Input variable key: `PROJECT_ID`
    - Input variable value: `<GCP Project ID for the service account>`

## Automated Deployments
RoboAdvisor Fund Rebalancer API leverages Gitlab's native CI/CD capabilites. A .gitlab-ci.yml file has been configured for automated deployment via a GCP service account. 
1. In the Gitlab CI/CD tab, click on the green Run Pipeline button to deploy the UI
2. The pipeline is configured to also deploy upon successful merge requests


# Associated Repos
1. [dev-tools](https://gitlab.com/cpsc319-2018w2/hsbc/formation/dev-tools): Docker services for local development
2. [HSBC-RoboAdvisor UI](https://gitlab.com/cpsc319-2018w2/hsbc/formation/roboadvisor-ui): User Interface component of HSBC fund rebalancer
3. [HSBC-RoboAdvisor API](https://gitlab.com/cpsc319-2018w2/hsbc/formation/roboadvisor-api): Backend API service for HSBC fund rebalancer
3. [RoboAdvisor-Cron](https://gitlab.com/cpsc319-2018w2/hsbc/formation/roboadvisor-cron): Script to ensure 24hr availability
