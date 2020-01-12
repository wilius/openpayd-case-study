FROM piegsaj/oracle-jre:1.8.0_172-b11
RUN mkdir -p /opt/openpayd /opt/openpayd/config /var/log/openpayd
ARG JAR_FILE
ARG HEALTH_CHECKER_JAR
ENV APPLICATION_PORT 8080
ADD target/${JAR_FILE} /opt/openpayd/application.jar
ADD target/${HEALTH_CHECKER_JAR} /opt/openpayd/health-checker.jar
EXPOSE $APPLICATION_PORT
WORKDIR /opt/openpayd
HEALTHCHECK CMD java -jar health-checker.jar -u=http://localhost:$APPLICATION_PORT/actuator/health
ENTRYPOINT exec java -Xbootclasspath/p:/opt/openpayd/config/ -Djava.security.egd=file:/dev/./urandom -d64 $JAVA_OPTS -jar application.jar