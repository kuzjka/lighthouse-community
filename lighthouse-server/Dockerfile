FROM eclipse-temurin:21-jre-alpine

RUN mkdir /lighthouse

WORKDIR /lighthouse

COPY build/libs/lighthouse.jar ./

CMD java -jar lighthouse.jar --spring.profiles.active=prd
