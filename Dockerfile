FROM java:8

EXPOSE 8084

ADD target/orderService-1.0-SNAPSHOT.jar orderService-1.0-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "orderService-1.0-SNAPSHOT.jar"]