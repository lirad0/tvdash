FROM maven:3.9.16-amazoncorretto-17-alpine AS build
WORKDIR /workspace

COPY pom.xml ./
COPY src ./src

RUN mvn -q -DskipTests package

FROM amazoncorretto:17-alpine-jdk
WORKDIR /app

COPY --from=build /workspace/target/*.jar /app/app.jar

EXPOSE 8080 5005

ENV DEBUG=false
ENTRYPOINT [ "sh", "-c", "if [ \"$DEBUG\" = \"true\" ]; then java -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=*:5005 -jar /app/app.jar; else java -jar /app/app.jar; fi" ]
