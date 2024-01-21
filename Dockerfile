FROM maven AS build

WORKDIR /app
COPY . /app
RUN mvn clean site package
RUN cd /app/target && ls

FROM openjdk:17-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar /app/app.jar

EXPOSE ${PORT}
CMD ["java", "-jar", "/app/app.jar"]

