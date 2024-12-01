# money-transfer

The purpose of this project was to create a simple REST API that provides an option to transfer money between bank accounts.
The whole project was implemented in Java using the Micronaut framework.
Crucial part - transfer service was implemented using pessimistic locking. Lock ordering was used to prevent deadlock.

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

## REST API
To create transfer you need to use the POST HTTP method with transfer amount and account ids passed to the body. 
The amount needs to be a positive number, all body fields are required.


Additionally, endpoints to create and retrieve bank accounts were created. 
The first (POST method) is used to create an account with initial balance (must be positive). 
After a successful request account object is returned with the generated account id.
The second one (GET method) can be used to retrieve the current balance of the account.

### Create account
```
curl -X POST \
  http://localhost:8080/accounts \
  -H 'content-type: application/json' \
  -d '{
	    "initialBalance": {initalBalance}
     }'
```

### Retrieve account
```
curl -X GET \
  http://localhost:8080/accounts/{accountId} 
```

### Create transfer
```
curl -X POST \
  http://localhost:8080/transfers \
  -H 'content-type: application/json' \
  -d '{
	    "sourceAccountId" : {sourceAccountId},
	    "destinationAccountId" : {destinationAccountId},
	    "amount": {amount}
     }'
```