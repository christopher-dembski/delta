# base image: use latest official image from Maven
FROM maven:latest

# create working directory for image and cd into it
WORKDIR /app

# copy file used by Maven for build
COPY pom.xml .

# copy source code
COPY src ./src

COPY infinite-loop.sh ./infinite-loop.sh

# copy mysql connector
COPY lib/mysql-connector-j-9.3.0.jar ./lib/mysql-connector-j-9.3.0.jar

# build the project
RUN mvn clean install

# run the database main method
ENTRYPOINT ["java", "-classpath", "target/classes:lib/mysql-connector-j-9.3.0.jar", "data.Database"]
