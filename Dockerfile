FROM maven:3.6.1-jdk-11

ENV WORKDIR=/opt/tir-sportif/backend

RUN mkdir -p ${WORKDIR}
WORKDIR ${WORKDIR}

ADD . ${WORKDIR}

RUN mvn clean
RUN mvn -B -Dmaven.test.skip=true package

RUN ls

CMD java -Xmx256m -jar ${WORKDIR}/target/tir-sportif.jar
