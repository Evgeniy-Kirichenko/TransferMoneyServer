FROM openjdk:16-alpine
EXPOSE 5500
ENV TZ Europe/Moscow
ADD /target/TransferMoneyService-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]

