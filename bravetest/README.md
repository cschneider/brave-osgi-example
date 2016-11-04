# Testing brave on OSGi

## Start zipkin server

docker run -d -p 9411:9411 openzipkin/zipkin

## Install brave and example

```
feature:install scr
install -s mvn:io.zipkin.java/zipkin/1.14.4-SNAPSHOT
install -s mvn:io.zipkin.reporter/zipkin-reporter/0.6.6-SNAPSHOT
install -s mvn:io.zipkin.reporter/zipkin-sender-urlconnection/0.6.6-SNAPSHOT
install -s mvn:io.zipkin.brave/brave-core/3.15.1-SNAPSHOT
install -s mvn:net.lr/bravetest/0.0.1-SNAPSHOT
```

## Find trace in UI

http://localhost:9411/

You might have to expand the time span to find your trace.
