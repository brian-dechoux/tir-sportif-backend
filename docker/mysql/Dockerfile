FROM mysql:5.7

ENV MYSQL_DATABASE tir-sportif
ENV MYSQL_USER admin
ENV MYSQL_PASSWORD password
ENV MYSQL_ROOT_PASSWORD SomeSecurePassword!

COPY creation.sql /docker-entrypoint-initdb.d