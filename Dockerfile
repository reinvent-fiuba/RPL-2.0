FROM azul/zulu-openjdk-alpine:11
VOLUME /tmp
ARG JAR_FILE="./build/libs/RPL-0.0.1-SNAPSHOT.jar"
COPY ${JAR_FILE} app.jar

COPY "./datadog/dd-java-agent.jar" "/tmp"

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-javaagent:/tmp/dd-java-agent.jar", "-Ddd.profiling.enabled=true", "-Ddd.logs.injection=true", "-Ddd.trace.analytics.enabled=true", "-Ddd.service=producer", "-Ddd.env=prod", "-jar", "/app.jar"]