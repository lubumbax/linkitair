# LinkitAir 

LinkitAir is a demo PoC of an online flights search application.
It consists of an API (Spring Boot) and a UI (Angular). 

## Overview

### The API
The API is a Spring Boot application that serves flight information through a Spring MVC REST Controller.
The data is retrieved from MongoDB. This allows us to add data any time during the execution of the demo.

Here are the entities involved:
  - Airport: identified uniquely by an airport `code` (eg "`AMS`") and contains information as `name`, `city`, `altitude`, ...   
  - Flight: identified uniquely by a flight number (eg "`LK0003`") and contains a `from` and `to` 
  - Flight.AirportData: contain the `code` of an airport and a `description` attribute made with the `code`, `name` and `city` of the actual Airport.  
    
The `Flight.AirportData.description` attribute is what the API will use in order to search flights by a given airport token. 
The token could be just part of the name of a city, the name of the airport itself of just the airport code.

### The UI
The UI is a basic Angular application that pulls flights information from the Spring Boot application. 
It consists of a flights search page in which we can search available flights for a given "From" and "To" airports.
The page displays two flight search boxes that will dynamically be populated with the airports matching the characters entered.
Once an airport is selected, the search box "locks-in" with a light blue colour indicating that it is "ready" to search.

## How to run it

First make sure that you have MongoDB installed and running. 

### Install MongoDB (Mac OS X)

```shell script
$ brew install mongodb
```

To launch MongoDB manually:
```shell script
$ mongod --config /usr/local/etc/mongod.conf
```

You may prefer to have `launchd` auto-starting mongodb at login:
```shell script
$ brew services start mongodb
```

The OOTB config looks something like this:
```
	systemLog:
	  destination: file
	  path: /usr/local/var/log/mongodb/mongo.log
	  logAppend: true
	storage:
	  dbPath: /usr/local/var/mongodb
	net:
	  bindIp: 127.0.0.1
```	

### Install MongoDB (Linux)
You can easily install MongoDB in Linux with one of the major distribution package systems (apt, rmp, ...). Eg.:
```bash
apt install mongodb
```

## Serve the API 
```shell script
git clone <url of this repository>
cd linkitair/flights-service
mvn clean spring-boot:run
```

### Test it
To return a list of flights from Amsterdam/Schiphol:
```shell script
curl -v -G "http://localhost:8080/flights/from/AMS"
```

To return a list of flights from Amsterdam/Schiphol to Frankfurt/Frankfurt am Main:
```shell script
curl -v -G "http://localhost:8080/flights/from/AMS/to/FRA"
```

Try to add a couple more flights:
```shell script
mongo
use linkitair;
db.flights.insert({ 
"_id" : "XX666", 
"from" : { "code" : "AMS", "description" : "AMS - Schiphol (Amsterdam)" }, 
"to" : { "code" : "MAD", "description" : "MAD - Bajaras (Madrid)" }, 
"time" : "23:59", 
"price" : "1.0", 
"_class" : "com.lubumbax.linkitair.flights.model.Flight"
});


db.flights.insert({ 
"_id" : "XX000", 
"from" : { "code" : "MAD", "description" : "MAD - Bajaras (Madrid)" }, 
"to" : { "code" : "AMS", "description" : "AMS - Schiphol (Amsterdam)" }, 
"time" : "00:00", 
"price" : "1.0", 
"_class" : "com.lubumbax.linkitair.flights.model.Flight"
});
```
 
## Run the UI
```shell script
git clone <url of this repository>
cd linkitair/inkitair-web
ng serve
```

### Test it
Browse to http://localhost:4200
Try entering just "am" in the "From" search box. Both "AMS" and "FRA" will be found. Select "AMS".
Try entering just "fra" in the "To" search box. "FRA" will be found. Select "FRA".
Search the available flights.

