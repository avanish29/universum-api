FROM openjdk:13-jdk-alpine3.10
RUN apk --no-cache add curl
VOLUME /temp
EXPOSE 8761
RUN mkdir -p /app/
RUN mkdir -p /app/logs/
ADD target/universum-security-service-0.0.1-SNAPSHOT.jar /app/security-service.jar
ENV JAVA_OPT="-XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap  -XX:MaxRAMFraction=1 -XshowSettings:vm -Xms512m -Xmx512m"
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app/security-service.jar" ]