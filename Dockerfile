FROM openjdk:21-jdk
COPY build/libs/money-transfer-*-all.jar money-transfer.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/money-transfer.jar"]
