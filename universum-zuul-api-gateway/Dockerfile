FROM openjdk:13-jdk-alpine3.10
RUN apk --no-cache add curl
VOLUME /temp
EXPOSE 8080
RUN mkdir -p /app/
RUN mkdir -p /app/scripts/
RUN mkdir -p /app/logs/
ADD target/universum-zuul-api-gateway.jar /app/service.jar
ADD ./wait-for-it.sh /app/scripts/waitforservices.sh
RUN chmod +x /app/scripts/waitforservices.sh
ENV JAVA_OPTS="-XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap  -XX:MaxRAMFraction=1 -XshowSettings:vm -Xms512m -Xmx512m"
ENTRYPOINT [ "sh", "-c", "java -Djava.security.egd=file:/dev/./urandom -jar /app/service.jar" ]