# money-transfer

Purpose of this project was to create simple REST API which provides option to transfer money between bank accounts.
Additionally endpoints to create and retrieve bank accounts was created.


Whole project was implemented in Java using Micronaut framework.
Crucial part - transfer service was implemented using pessimistic locking (repeatable read isolation level is another option). 
Lock ordering was used to prevent deadlock.

## Build the project
```
gradlew clean build
```
## Build docker image
```
docker build -t money-transfer .
```
## Run container
```
docker run -d -p 8080:8080 money-transfer
```