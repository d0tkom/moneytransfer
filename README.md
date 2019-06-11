# Revolut Backend Test
This project contains the solution to the mentioned backend test. It was created using Intellij, Java 8 and Maven.
## How to run
`mvn install`

`mvn exec:java -Dexec.mainClass="server.RestApi"`
## Overview
- The data store consist two concurrentHashMaps, to keep track of accounts and transfers. 
- The current account of a balance
is always the sum of all the transfers concerning that account. This was used as a simulation for the SQL SUM command.
In SQL a trigger could be used to have a balance value keep updating on a new transfer insert, having a balance that we
can always look up in O(1) time.
- For dates we use LocalDateTime for simplification
- An account cannot have negative balance
- Account starting balance is represented as a transfer from null
- Services and handlers have their own corresponding tests
- There are also some integration tests, to test the actual rest endpoints
- To make sure concurrent transfers are handled correctly, a lock was used, so only ever one thread can insert
a transfer after checking required balance
- For account and transfer ids UUIDs were used, and it was assumed those never collide


## Endpoints
All endpoints return json. If there was an error, (wrong user input, non existing account etc.), endpoints return
an error json object in this format:
`{error: "error message" }`
#### `GET http://localhost:4567/accounts`
Returns the list of all accounts.

#### `GET http://localhost:4567/accounts/:id`
Returns the account with the given id if found.

#### `GET http://localhost:4567/accounts/:id/transfers`
Returns all transfers concerning account with the given id.

#### `POST http://localhost:4567/accounts`

Without sending any body, a new account will be returned with 0 balance. By providing a body in this format:

`{balance: :startingBalance }` 

an account can be created with a starting balance.



#### `GET http://localhost:4567/transfers`
Returns all the transfers. (Even the initial deposits)

#### `GET http://localhost:4567/transfers/:id`
Returns the transfer with the given id.

#### `POST http://localhost:4567/transfers`
Needs to be called with the following json format:

`{source: :sourceAccountId, target: :targetAccountId, amount: :transferAmount}`

If successful returns the new transfer.
