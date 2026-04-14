FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app

RUN apk add --no-cache maven

COPY pom.xml .

# 2. Copiamos las carpetas nuevas respetando la estructura
COPY infrastructure/ infrastructure/
COPY domain/ domain/

RUN mvn clean package -DskipTests -pl domain/ordermgmt-product -am

# Runtime
FROM eclipse-temurin:17-jre-alpine
RUN apk add --no-cache curl bash
WORKDIR /app

COPY --from=build /app/domain/ordermgmt-product/target/*.jar app.jar

EXPOSE 8090

ENTRYPOINT ["java", "-jar", "app.jar"]