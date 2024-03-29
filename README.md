# LinkitAir 

LinkitAir is a demo PoC of an online flights search application.  
It consists on a REST API (Spring Boot) and a UI (Angular). 

### TL;DR

```shell script
$ mkdir -p ~/.mongodb/data
$ docker run -d --name mongo -p 27017:27017 -v "${HOME}/.mongodb/data:/data/db" mongo
```
```shell script
$ git clone https://github.com/lubumbax/linkitair.git
$ cd linkitair/flights-service
$ mvn clean spring-boot:run
```
```shell script
$ git clone https://github.com/lubumbax/linkitair.git
$ cd linkitair/linkitair-web
$ npm install
$ ng serve
```

  - Access LinkitAir at http://localhost:4200 and start searching flights.
  - Access Swagger UI at http://localhost:8080/swagger-ui.html

```shell script
$ cd linkitair
$ docker run -d --name prometheus -p 9090:9090 -v "$(pwd)/prometheus.yaml:/etc/prometheus/prometheus.yml" prom/prometheus
```  

```shell script
$ docker run -d --name grafana -p 3000:3000 grafana/grafana
```
  - Access Grafana at http://localhost:3000 (username `admin` and pasword `admmin`).    
  - Once in, follow "`Configuration` (left menu) > `Datasources` > `[Add data source]` > `Prometheus`: {`URL = http://host.docker.internal:9090`} > `Save and Test`". 
  - Once the data source has been created, follow "`Create` (+ on the left menu) > `Import` > (paste the contents of `dashboard.json`) > `[Load]` > `Prometheus`: (select the _Prometheus_ data source) > `[Import]`".  

## Overview

LikitAir consists on a backend Spring Boot REST API serving data to a frontend Angular 8 UI.  
These run each in its own process (and port/service).

It is possible to have the UI served by the Spring Boot process.  
You can check that checking out to the `feature/ui-in-boot` branch in this repository (see doc for that there).  

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

The Angular application is to be run from the command line with `ng serve`.  
It is possible to build it and embed it in the Spring Boot application.  
See the `ui-in-boot` branch to see how to do that.

## How to run it

### Requirements

  - MongoDB  
  - Java 17

### Run MongoDB
The easiest way to run MongoDB is probably launching it in a Docker container:
```shell script
$ mkdir -p ~/.mongodb/data
$ docker run -d --name mongo -p 27017:27017 -v "${HOME}/.mongodb/data:/data/db" mongo
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

## Serve the API 
```shell script
$ git clone <url of this repository>
$ cd linkitair/flights-service
$ mvn clean spring-boot:run
```

### Test it
To return a list of flights from Amsterdam/Schiphol:
```shell script
$ curl -v -G "http://localhost:8080/flights/from/AMS"
```

To return a list of flights from Amsterdam/Schiphol to Frankfurt/Frankfurt am Main:
```shell script
$ curl -v -G "http://localhost:8080/flights/from/AMS/to/FRA"
```

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
 
## Run the UI
```shell script
$ git clone <url of this repository>
$ cd linkitair/linkitair-web
$ npm install
$ ng serve
```

### Test it
Browse to http://localhost:4200  
Try entering just "am" in the "From" search box. Both "AMS" and "FRA" will be found. Select "AMS".  
Try entering just "fra" in the "To" search box. "FRA" will be found. Select "FRA".  
Search the available flights.  

## Metrics
On the Spring Boot API, Micrometer gathers metrics that are made available by Actuator under http://localhost:8080/actuator/metrics  

Adding the dependency `micrometer-registry-prometheus` is enough for Spring Boot to add a Prometheus metrics registry to `MeterRegistry`.
Prometheus then can be configured to pull (_scrape_) the values of actuator metrics periodically and store them as time series values.

Then we can visualize data from Prometheus in a dashboard with Grafana.

### Prometheus
The following steps assume that you have Docker running on your machine.
```shell script
$ cd linkitair
$ docker run -d --name prometheus -p 9090:9090 -v "$(pwd)/prometheus.yaml:/etc/prometheus/prometheus.yml" prom/prometheus
```  

*Note*: for some versions of Docker (or the driver used) the _volume binding_ (`-v`) feature doesn't work.   
As a workaround, we can create our own prometheus image:
```shell script
docker build -t lubumbax/prometheus -f - . << __EOF
FROM prom/prometheus
COPY prometheus.yaml /etc/prometheus/prometheus.yml
__EOF
docker run -d --name prometheus -p 9090:9090 lubumbax/prometheus
``` 

Once it is running, go to http://localhost:9090/targets and check that Prometheus lists the label `linkitair_micrometer`.  
That confirms that Prometheus is now pulling data from our Spring Boot application.

### Grafana  
The following steps assume that you have Docker running on your machine.
```shell script
$ docker run -d -p 3000:3000 grafana/grafana
```
  - Access Grafana at http://localhost:3000 (username `admin` and pasword `admmin`).    
  - Once in, follow "`Configuration` (left menu) > `Datasources` > `[Add data source]` > `Prometheus`: {`URL = http://host.docker.internal:9090`} > `Save and Test`". 
  - Once the data source has been created, follow "`Create` (+ on the left menu) > `Import` > (paste the contents of `dashboard.json`) > `[Load]` > `Prometheus`: (select the _Prometheus_ data source) > `[Import]`".  

