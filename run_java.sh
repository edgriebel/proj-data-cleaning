#!/bin/sh

mvn clean package 1>&2 && \
java -jar target/data_cleaning_proj-0.0.1-SNAPSHOT-jar-with-dependencies.jar
