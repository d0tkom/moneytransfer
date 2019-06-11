# Revolut Backend Test
This project contains the solution to the mentioned backend test. It was created using Intellij, Java 8 and Maven.
## How to run
`mvn install`
`mvn exec:java -Dexec.mainClass="server.RestApi"`
## Overview
The data store consist two concurrentHashMaps, to keep track of accounts and transfers. The current account of a balance
is always the sum of all the transfers concerning that account. This was used as a simulation for the SQL SUM command.
In SQL triggers could be used to have a balance value keep updating on a new transfer insert, having a balance that we
can always look up in O(1) time. Here for simplification we just sum the transfers to get the account balance.



## Endpoints
#### `GET http://localhost:4567/accounts`

#### `GET http://localhost:4567/accounts/id`

#### `GET http://localhost:4567/accounts/id/transfers`

##### accepts: `{balance: startingBalance }`



#### `GET http://localhost:4567/transfers`

#### `GET http://localhost:4567/transfers/id`

#### `POST http://localhost:4567/transfers`

##### accepts: `{source: accountId, target: accountId, amount: transferAmount}`
