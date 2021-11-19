# Car Web REST Demo using Spring Data Rest with PostgresSQL 
Car Web Stack
* SpringBoot
* Spring Data Rest
* Spring Data JPA
* Spring Sleuth
* Swagger
* H2 DB
* Postgres SQL
* Maven

This project aims to provide a RESTful service with HAL (Hypertext Application Language) that provide links 
to the top level car and sub resources make and model. 

We provide a default application yaml that uses a Postgres docker instance or a running with the profile set
to local which uses an in memory H2 DB for non volatile storage.


##Testing
To run the test simply run the maven command:
```mvn clean test```

##Running locally with docker PostgresDB for persistent storage
To run locally with persistent storage you would need to have docker installed on your machine.
In the root directory of the application run the following command to start up the PostgresSQL database:
```docker-compose up -d```

Once the pod has started run the following maven command to start the application:
```mvn clean spring-boot:run```

##Running locally with in memory H2 DB
Run the application using local profile
```` mvn spring-boot:run -Dspring-boot.run.profiles=local````