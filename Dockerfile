# base image: use latest official image from Maven
FROM maven:latest

# set environment variable used for environment-specific behaviour
# like the MySQL connection string
ENV APP_ENV="CI"

# create working directory for image and cd into it
WORKDIR /app

# copy file used by Maven for build
COPY pom.xml .

# copy source code
COPY src ./src

# copy docker entrypoint script
COPY scripts/docker-entrypoint.sh .

# run tests
ENTRYPOINT ["./docker-entrypoint.sh"]
