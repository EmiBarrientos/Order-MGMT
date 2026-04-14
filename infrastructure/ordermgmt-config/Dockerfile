FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app

RUN apk add --no-cache maven

COPY pom.xml .

# 2. Copiamos las carpetas nuevas respetando la estructura
COPY infrastructure/ infrastructure/
COPY domain/ domain/

RUN mvn clean package -DskipTests -pl infrastructure/ordermgmt-config -am

# Runtime
FROM eclipse-temurin:17-jre-alpine
RUN apk add --no-cache curl bash
WORKDIR /app

COPY --from=build /app/infrastructure/ordermgmt-config/target/*.jar app.jar

EXPOSE 8888

ENTRYPOINT ["java", "-jar", "app.jar"]