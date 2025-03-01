FROM openjdk:21-jdk
WORKDIR /app
COPY .env .env
COPY build/libs/crm4ig.jar app.jar
ARG LOG_FOLDER
RUN mkdir -p ${LOG_FOLDER}
ARG SERVER_PORT
EXPOSE ${SERVER_PORT}
ENTRYPOINT ["java", "-cp", "app.jar:BOOT-INF/lib/*", "org.springframework.boot.loader.launch.JarLauncher"]