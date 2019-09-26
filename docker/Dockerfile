FROM maven:3.6.1-jdk-11

ENV WORKDIR=/opt/tir-sportif/backend

RUN mkdir -p ${WORKDIR}
WORKDIR ${WORKDIR}

ADD . ${WORKDIR}

ADD docker/start.sh /
RUN ["chmod", "+x", "/start.sh"]
CMD ["/start.sh"]
