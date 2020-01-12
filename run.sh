#!/usr/bin/env bash
set -e
mvn -v >/dev/null 2>&1 || { echo >&2 "maven is required to run application.  Aborting."; exit 1; }
docker -v >/dev/null 2>&1 || { echo >&2 "docker is required to run application.  Aborting."; exit 1; }
docker-compose -v >/dev/null 2>&1 || { echo >&2 "docker-compose is required to run application.  Aborting."; exit 1; }

set +e
docker-compose -f docker-compose.yml down

set -e
mvn clean install
docker build -t com.openpayd/exchange:latest \
    --build-arg JAR_FILE=exchange-rest-service-0.1.jar \
    --build-arg HEALTH_CHECKER_JAR=actuator-health-checker-0.1.jar \
    exchange/.

docker-compose -f docker-compose.yml up -d