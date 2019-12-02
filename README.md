# LinkitAir 

LinkitAir is a demo PoC of an online flights search application.  
It consists of an API (Spring Boot) and a UI (Angular). 

### TL;DR

```shell script
$ mkdir -p ~/.mongodb/data
$ docker run -d -p 27017:27017 -v "${HOME}/.mongodb/data:/data/db" mongo
```
```shell script
$ git clone <url of this repository>
$ cd linkitair
$ mvn clean install 
$ cd /flights-service
$ mvn spring-boot:run
```

Access LinkitAir at http://localhost:8080 and start searching flights.    

```shell script
$ cd linkitair
$ docker run -d -p 9090:9090 -v "$(pwd)/prometheus.yaml:/etc/prometheus/prometheus.yml" prom/prometheus
```  

```shell script
$ docker run -d -p 3000:3000 grafana/grafana
```
Access Grafana at http://localhost:3000 (username `admin` and pasword `admmin`).  
Once there go to "+ > Import" and import the contents of `dashboard.json`.  


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
It consists on a flights search page in which we can search available flights for a given "From" and "To" airports.  
The page displays two flight search boxes that will dynamically be populated with the airports matching the characters entered.  
Once an airport is selected, the search box "locks-in" with a light blue colour indicating that it is "ready" to search.  

The Angular application is running embedded in the Tomcat server of Spring Boot. 

## How to run it

### Requirements

  - MongoDB  
  - Java 11

### Run MongoDB
The easiest way to run MongoDB is probably launching it in a Docker container:
```shell script
$ mkdir -p ~/.mongodb/data
$ docker run -d -p 27017:27017 -v "${HOME}/.mongodb/data:/data/db" mongo
```

You may prefer running MongoDB locally as described in the following section.

#### Install MongoDB (Mac OS X)
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

#### Install MongoDB (Linux)
You can easily install MongoDB in Linux with one of the major distribution package systems (apt, rmp, ...). Eg.:
```bash
apt install mongodb
```

## Serve LinkitAir
```shell script
$ git clone <url of this repository>
$ cd linkitair
$ mvn clean install 
$ cd /flights-service
$ mvn spring-boot:run
```

That builds it all, runs some tests and stats Spring Boot. 
Spring Boot is serving both the API and the UI in one.

### Test the API
To return a list of flights from Amsterdam/Schiphol:
```shell script
$ curl -v -G "http://localhost:8080/flights/from/AMS"
```

To return a list of flights from Amsterdam/Schiphol to Frankfurt/Frankfurt am Main:
```shell script
$ curl -v -G "http://localhost:8080/flights/from/AMS/to/FRA"
```

### Test the UI
Browse to http://localhost:8080  
Try entering just "am" in the "From" search box. Both "AMS" and "FRA" will be found. Select "AMS".  
Try entering just "fra" in the "To" search box. "FRA" will be found. Select "FRA".  
Search the available flights.  

### API Documentation
Documentation about the endpoints and models is exposed by Swagger at http://localhost:8080/swagger-ui.html  

### Add or Edit Data
Try to add a couple more flights:
```shell script
$ mongo

> use linkitair;
> db.flights.insert({ 
"_id" : "XX666", 
"from" : { "code" : "AMS", "description" : "AMS - Schiphol (Amsterdam)" }, 
"to" : { "code" : "MAD", "description" : "MAD - Bajaras (Madrid)" }, 
"time" : "23:59", 
"price" : "1.0", 
"_class" : "com.lubumbax.linkitair.flights.model.Flight"
});

> db.flights.insert({ 
"_id" : "XX000", 
"from" : { "code" : "MAD", "description" : "MAD - Bajaras (Madrid)" }, 
"to" : { "code" : "AMS", "description" : "AMS - Schiphol (Amsterdam)" }, 
"time" : "00:00", 
"price" : "1.0", 
"_class" : "com.lubumbax.linkitair.flights.model.Flight"
});
```
 
## Metrics
On the Spring Boot API, Micrometer gathers metrics that are made available by Actuator under http://localhost:8080/actuator/metrics  

Adding the dependency `micrometer-registry-prometheus` is enough for Spring Boot to add a Prometheus metrics registry to `MeterRegistry`.
Prometheus then can be configured to pull (_scrape_) the values of actuator metrics periodically and store them as time series values.

Then we can visualize data from Prometheus in a dashboard with Grafana.

### Prometheus
The following steps assume that you have Docker running on your machine.
```shell script
$ cd linkitair
$ docker run -d -p 9090:9090 -v "$(pwd)/prometheus.yaml:/etc/prometheus/prometheus.yml" prom/prometheus
```  

Once it is running, go to http://localhost:9090/targets and check that Prometheus lists the label `linkitair_micrometer`.  
That confirms that Prometheus is now pulling data from our Spring Boot application.

### Grafana  
The following steps assume that you have Docker running on your machine.
```shell script
$ docker run -d -p 3000:3000 grafana/grafana
```
Access Grafana at http://localhost:3000 (username `admin` and pasword `admmin`).  
Once in, go to "+ > Import" and import the contents of `dashboard.json`.  

