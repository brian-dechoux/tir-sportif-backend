FROM maven:3.6.1-jdk-11

ENV WORKDIR=/opt/ihub-backend
ENV ENVIRONMENT=LOCAL,SSL,ft
ENV NEXUS_IHUB_DEPENDENCIES_USERNAME=ihub
ENV NEXUS_IHUB_DEPENDENCIES_PASSWORD=SC3GYnyuFb6TJHjw
ENV NEXUS_IHUB_RELEASES_USERNAME=deployment
ENV NEXUS_IHUB_RELEASES_PASSWORD=deployment

RUN mkdir -p ${WORKDIR}
WORKDIR ${WORKDIR}

ADD docker/ihub-backend/settings.xml /root/.m2/settings.xml

ADD .. ${WORKDIR}

CMD ["sh", "-c", "printenv && cat /root/.m2/settings.xml && mvn clean && mvn -B -Dmaven.test.skip=true package && java -jar -Dspring.profiles.active=${ENVIRONMENT} target/ihub-backend.jar"]
