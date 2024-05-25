# eCritic-application-gateway

**Java SpringBoot Gateway Project**

## Description

The ecritic application is an eCommerce platform. The project aims to enhance my backend development skills and experience by creating microservices using Java, SpringBoot, SQL/NOSQL databases, message brokers and other technologies. The application will serve as a learning platform where I can implement various Java concepts, design patterns, and best practices.

The ecritic-application-gateway is responsible for acting as a reverse proxy to route client requests to various microservices.

This repository also contains the docker compose file with the necessary dependencies to run all other microservices for the eCritic ecosystem.

## Technologies Used

- Java 17
- SpringBoot 3.3.0

## Prerequisites

To run this project locally, you will need the following:

- Java 17 JDK installed
- Maven build tool

## Installation

1. Clone this repository:

2. Configure any necessary environment variables as needed.

3. Build the project using Maven. Run the following command from the project's root directory:
   ```bash
   mvn clean install
4. Once the build is successful, you can run the application using the following command:
   ```bash
   mvn spring-boot:run

5. To run docker compose just enter the docker folder and run the command: 
   ```bash
   docker compose up -d

# Contributing
This project is a personal endeavor, and contributions are not accepted at the moment but if you have any suggestions for improvement please feel free to contact me.
