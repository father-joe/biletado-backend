# biletado-backend
Web-Engeneering-Project: Backend for Biletado

# Requirements
* Java 17
* maven 3.8.1

## How To
### Start server locally
```
mvn spring-boot:run
```
### Run unit tests
```
mvn verify
```
### Build docker image and install to local docker daemon
```
mvn jib:dockerBuild  
```
