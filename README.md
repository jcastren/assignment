# Coding assignment

The project implements a simple Game account with two HTTP operations:
- Operation for enabling game engine to notify about purchasing a game.
- Operation for enabling game engine to notify about a win.

1. Operation purchase (PUT) /game-account/charge
   1. As an input parameter, a JSON-object is sent in the body of HTTP request with members:
      1. customerId (integer)
      2. gameEventId (integer)
      3. amount (float)
   2. Operation updates customer's balance with the amount. An event is created with gameEventId / existing event is returned
   3. Exceptions:
      1. Operation checks if customer can be found from DB with the customerId. HTTP status Not Found (404) is returned if customer doesn't exist
      2. Operation checks if an event already exists in DB with the gameEventId.
         1. If event doesn't exist, operation checks if the customer has enough balance for the purchase.
         If not, HTTP status Bad Request (400) is returned. If customer has enough balance, an event with gameEventId is created
         2. If event exists, operation checks if event belongs to this customer and has the correct event type. If either of these checks fail,
            HTTP status Bad Request (400) is returned. Otherwise HTTP status OK (200) is returned, as well as the balance after the original request
2. Operation notify about a win (PUT) /game-account/notify-win
   1. As an input parameter, a JSON-object is sent in the body of HTTP request with members:
      1. customerId (integer)
      2. gameEventId (integer)
      3. amount (float)
   2. Operation updates customer's balance with the amount. An event is created with gameEventId / existing event is returned
   3. Exceptions:
      1. Operation checks if customer can be found from DB with the customerId. HTTP status Not Found (404) is returned if customer doesn't exist
      2. Operation checks if an event already exists in DB with the gameEventId.
         1. If event doesn't exist, an event with gameEventId is created and HTTP status OK (200) is returned
         2. If event exists, operation checks if event belongs to this customer and has the correct event type. If either of these checks fail,
            HTTP status Bad Request (400). Otherwise HTTP status OK (200) is returned, as well as the balance after the original request

## Installation of the project
- [Install JDK 21](https://adoptium.net/temurin/releases/?package=jdk&version=21)
- [Install Maven](https://maven.apache.org/download.cgi)
- On command line, build the project:
<pre>mvn clean install</pre>
## Starting up the application:
<pre>mvn spring-boot:run</pre>
- Now PUT requests can be sent to https://localhost:8443/game-account/charge and https://localhost:8443/game-account/notify-win endpoints by using Postman, for instance. 
## Running the tests:
<pre>mvn test</pre>