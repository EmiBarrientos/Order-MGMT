FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app

RUN apk add --no-cache maven

COPY pom.xml .

# 2. Copiamos las carpetas nuevas respetando la estructura. si lo hice con IA cual fue?
COPY infrastructure/ infrastructure/
COPY domain/ domain/

RUN mvn clean package -DskipTests -pl infrastructure/ordermgmt-gateway -am

# Runtime
FROM eclipse-temurin:17-jre-alpine
RUN apk add --no-cache curl bash
WORKDIR /app

COPY --from=build /app/infrastructure/ordermgmt-gateway/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]