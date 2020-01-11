FROM piegsaj/oracle-jre:1.8.0_172-b11
RUN mkdir -p /opt/openpayd /opt/openpayd/properties /var/log/openpayd
ARG JAR_FILE
ADD target/${JAR_FILE} /opt/openpayd/application.jar
WORKDIR /opt/openpayd
ENTRYPOINT exec java -Xbootclasspath/p:/opt/openpayd/properties/ -Djava.security.egd=file:/dev/./urandom -d64 $JAVA_OPTS -jar application.jar