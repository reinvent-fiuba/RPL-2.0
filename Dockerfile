FROM azul/zulu-openjdk-alpine:11
VOLUME /tmp
ARG JAR_FILE="./build/libs/RPL-0.0.1-SNAPSHOT.jar"
COPY ${JAR_FILE} app.jar

COPY "./newrelic/newrelic.jar" "/tmp"
COPY "./newrelic/newrelic.yml" "/tmp"

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-javaagent:/tmp/newrelic.jar", "-jar", "/app.jar"]