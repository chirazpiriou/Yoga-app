# Yoga App <sub>(NumDev-Agency)</sub>

## Description
Yoga App is a management application for yoga studios. It allows administrators to create, update, and delete yoga sessions, while enabling users to register and participate in sessions.
## Prerequisites
Before starting, make sure the following are installed
#### <u>*Technologies Used*</u>
<div align="center">

| **Back-end**                                      | **Front-end**                                    | **Database**       |
|---------------------------------------------------|--------------------------------------------------|--------------------|
| ![Java](https://img.shields.io/badge/Java-17.0.12-blue) | ![Angular](https://img.shields.io/badge/Angular-14.2.0-brightgreen) | ![MySQL](https://img.shields.io/badge/MySQL-8.0.39-blue) |
| ![Spring Boot](https://img.shields.io/badge/Spring_Boot-2.6.1-green) | ![Angular CLI](https://img.shields.io/badge/Angular_CLI-14.2.1-blue) |                    |
| ![Apache Maven](https://img.shields.io/badge/Maven-3.9.9-yellow)  | ![Node.js](https://img.shields.io/badge/Node.js-20.17.0-brightgreen) |                    |
</div>
<br>

#### <u>*Testing Frameworks & Coverage Tools*</u>
<div align="center">

| **Testing**                                             |
|---------------------------------------------------------|
| **Front-end**  &nbsp;&nbsp;&nbsp;&nbsp;  ![Jest](https://img.shields.io/badge/Jest-28.1.3-orange)  |
| **Back-end**   &nbsp;&nbsp;&nbsp;&nbsp; ![Mockito](https://img.shields.io/badge/Mockito-3.11.0-red) ![JUnit](https://img.shields.io/badge/JUnit-5.8.2-brightgreen) ![JaCoCo](https://img.shields.io/badge/JaCoCo-0.8.5-blue) |
| **End-to-End** &nbsp;&nbsp;&nbsp;&nbsp; ![Cypress](https://img.shields.io/badge/Cypress-10.4.0-blue) |

</div>


## Install the Application
1- Clone the git repository with the following command in a terminal
````
git clone https://github.com/chirazpiriou/Yoga-app.git
````
2- Install the frontend dependencies

In your terminal, execute the following command to install the dependencies specified in the package.json file.
    
 ```
cd front
npm install
```

3- Install the backend dependencies

In your terminal, execute the command below to install the dependencies specified in the pom.xml file.
    
```
cd back
mvn clean install
```
## Setting Up the Database
To create the database, follow these steps:
- Make sure that MySQL Server is running on your machine before proceeding.
- You can create the database using either MySQL Workbench for a graphical interface or MySQL CLI for command-line interaction.
- Authenticate by logging in with your MySQL username and password "root" for both
- Navigate to the following path to find the SQL script:  `Testez-une-application-full-stack/ressources/sql/script.sql`.

- In MySQL Workbench, go to `File > Open SQL Script`, and select the script located at the path above.
- At the beginning of the script, add the following lines to ensure that the `yoga-appDatabase` database is created:
   ```sql
   CREATE DATABASE IF NOT EXISTS `yoga-appDatabase`;
   USE `yoga-appDatabase`;

- Run the Script: Click on the lightning bolt icon (⚡) in MySQL Workbench to execute the script.
- To connect the database to the Spring Boot API, the necessary configurations have been set up in the back/src/main/resources/application.properties file.

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/yoga-appDatabase?allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```
## Run the Application
**- Frontend**

Make sure you are in the frontend directory (front). Then, follow these steps:
* Run the following command to launch the Angular application
```
ng serve
```
* Once the command completes, open your browser and go to http://localhost:4200. The frontend will be live and accessible. 

<div align="center">

|                             |
|-----------------------------|
| **By default, the admin account is:**  |
| login: yoga@studio.com       |
| password: test!1234          |

</div>

**- Backend**

Ensure you are in the backend directory (back). Then, follow these steps:
* Start the Spring Boot backend server by running:
```
mvn spring-boot:run
```
* Once the backend is up and running, the API will be available at http://localhost:8080.
## Run the Different Tests & Generate Coverage Reports


### ✅ Back-End Tests


* Ensure you are in the backend directory (back) then Run the tests 

```
mvn clean test
```
* To generate a coverage report, navigate to the `back/target/site/jacoco` directory and opening the `index.html` file in a browser.



### ✅ Front-End Tests 


* Ensure you are in the frontend directory (front) then Run the tests 

```
npm run test
```
* To view the coverage report, run the command 
```
npm run test:coverage
```
* To generate a coverage report, navigate to the `front/coverage/jest/lcov-report` directory and opening the `index.html` file in a browser


### ✅ End-to-End Tests


* Ensure you are in the frontend directory (front) then run the tests 
* There are two ways to run the tests:

Open the Cypress GUI:
```
npx cypress open
```
Run the tests in headless mode:
```
npx cypress run
```

* To view the coverage report, run the command 
```
npm run test:coverage
```
* The report index.html will be generated in the `front/coverage/lcov-report/index.html` directory.
