#!/bin/bash
printenv
mvn clean
mvn -B -Dmaven.test.skip=true package
java -jar target/tir-sportif.jar