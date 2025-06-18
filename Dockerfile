# base image: use latest official image from Maven
FROM maven:latest

# create directory to store app and cd into it
WORKDIR /app

# copy file used by Maven for build
COPY pom.xml .

# copy source code
COPY src ./src

# run the database main method
ENTRYPOINT ["mvn", "clean", "test"]
