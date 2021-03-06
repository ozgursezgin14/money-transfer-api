# Money transfer Rest API with Spring Boot

A Spring Boot RESTful API for money transfers with Currency Conversion

### Technologies
- Spring Boot
- Spring MVC for REST Api implementation
- Spring Data JPA
- Java 8
- H2 in memory database
- Junit
- ModelMapper 
- SonarLint 
- JavaMoney


### How to run
In the project's target folder:

```sh
java -jar money-transfer-api-0.0.1-SNAPSHOT.jar
```

Spring Boot starts a embeded Tomcat server on localhost port 8080 An H2 in memory database initialized with some sample user and account data To view

- http://localhost:8080/banks
- http://localhost:8080/banks/1
- http://localhost:8080/banks/1/customers
- http://localhost:8080/banks/1/customers/5
- http://localhost:8080/banks/1/customers/5/accounts
- http://localhost:8080/banks/1/customers/5/accounts/12
- http://localhost:8080/banks/1/customers/5/accounts/12/balance
- http://localhost:8080/banks/1/customers/5/accounts/12/withdraw/1000
- http://localhost:8080/banks/1/customers/5/accounts/12/deposit/1000
- http://localhost:8080/banks/1/customers/5/accounts/12/transaction

### Available Services

| HTTP METHOD | PATH | USAGE |
| -----------| ------ | ------ |
| GET | /banks    | get all banks | 
| GET | /banks/{id} | get bank by Id | 
| POST | /banks  | create a new bank | 
| PUT | /banks/{id} | update bank | 
| DELETE | /banks/{id} | delete bank | 
| GET | /banks/{bankId}/customers    | get all customers belong that bank | 
| GET | banks/{bankId}/customers/{id} | get customer by Id belongs that bank | 
| POST | /banks/{bankId}/customers  | create a new customer belongs that bank| 
| PUT | /banks/{bankId}/customers/{id} | update customer belongs that bank| 
| DELETE | /banks/{bankId}/customers/{id} | delete customer belongs that bank|
| GET | /banks/{bankId}/customers/{customerId}/accounts | get all accounts belong that bank and that customer | 
| GET | /banks/{bankId}/customers/{customerId}/accounts/{id} | get account by Id belongs that bank and that customer | 
| POST | /banks/{bankId}/customers/{customerId}/accounts | create a new account belongs that bank and that customer |
| PUT | /banks/{bankId}/customers/{customerId}/accounts/{id} | create a new account belongs that bank and that customer |
| DELETE | /banks/{bankId}/customers/{customerId}/accounts/{id} | delete account belongs that bank and that customer | 
| GET | /banks/{bankId}/customers/{customerId}/account/{id}/balance | get account balance | 
| PUT | /banks/{bankId}/customers/{customerId}/accounts/{id}/withdraw/{amount} | withdraw money from account | 
| PUT | /banks/{bankId}/customers/{customerId}/accounts/{id}/deposit/{amount} | deposit money to account | 
| POST | /banks/{bankId}/customers/{customerId}/accounts/{id}/transaction | perform transaction between 2 customer accounts | 

### Http Status
- 200 OK: The request has succeeded
- 201 Created: The request has been fullfilled and resulted in a new resourse being created
- 204 No Content: The request has been fullfilled successfully but does not need to return an entity body
- 400 Bad Request: The request could not be understood by the server due to malformed syntax
- 404 Not Found: The requested resource cannot be found
- 405 Method Not allowed: The method specified in the request in not allowed for the resourse identified by the request URI
- 409 Conflict: The request could not be completed due to a conflict with the current state of resourse
- 500 Internal Server Error: The server encountered an unexpected condition 

### Sample JSON for Response Body  for GET methods contains Bank, Customer and Account
##### Bank : 
```sh
{
    "id": 1,
    "code": "ING",
    "bankName": "ING Bank",
    "description": "ING Bank description"
}
```
##### Bank Customer: : 

```sh
{
    "id": 2,
    "email": "joe@gmail.com",
    "customerName": "Joe Doe",
    "description": "Joe Doe description",
    "banksCustomer": {   <----- Shows this  customer's bank(parent)
        "id": 1,
        "code": "ING",
        "bankName": "ING Bank",
        "description": "ING Bank description"
    }
}
```

##### Customer Account: : 

```sh
{
    "id": 3,
    "accountNumber": "ACN10000",
    "balance": 1000,
    "currencyCode": "USD",
    "description": "Account ACN1000 description",
    "customersAccount": {   <---- Shows this account's customer(parent)
        "id": 2,
        "email": "joe@gmail.com",
        "customerName": "Joe Doe",
        "description": "Joe Doe description",
        "banksCustomer": {   <---- Shows this account's bank(which customer belongs)(grand-parent)
            "id": 1,
            "code": "ING",
            "bankName": "ING Bank",
            "description": "ING Bank description"
        }
    }
}

```


### Sample JSON for Request Body contains Bank, Customer and Account, Account-Transaction for insert, update and transaction operations
##### Bank : 
```sh
  {
    "code": "ING",
    "bankName": "ING Bank",
    "description": "ING Bank description"
  }
```
##### Bank Customer: : 

```sh
{
  "email": "joe@gmail.com",
  "customerName": "Joe Doe",
  "description": "Joe Doe description"
}
```

##### Customer Account: : 

```sh
{   
	"accountNumber": "ACN10001",
    "currencyCode": "USD",
    "description": "Account ACN10001 description"
}
```

#### Account Transaction:
```sh
{  
   "amount": 100000.00,
   "toAccountId": 2
}
```

