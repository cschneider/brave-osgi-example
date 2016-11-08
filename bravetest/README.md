# Testing brave on OSGi

## Set docker ip as kafka advertised host name

	ifconfig docker0
	
Set this ip as KAFKA_ADVERTISED_HOST_NAME in docker-compose.yml.

Add "kafka" as an alias for localhost in /etc/hosts.

## Start zipkin server

	docker-compose rm	
	docker-compose up

This will start zookeeper, kafka and zipkin

## Build

	mvn clean install

## Install brave and example

Download and start karaf

	bin/karaf

Inside karaf install the example

	feature:repo-add mvn:net.lr/bravetest/0.0.1-SNAPSHOT/xml/features
	feature:install brave-example


## Find trace in UI

	http://localhost:9411/

You might have to expand the time span to find your trace.
