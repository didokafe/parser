#!/bin/bash

USER="root"
PASS="root"

mysql -u $USER -p$PASS < tools/migration.sql

./mvnw clean package
java -jar target/parser.jar --accesslog=/home/daniel/Dropbox/Java_MySQL_Test/access.log --startDate=2017-01-01.00:00:00 --duration=daily --threshold=500



#documentation
#clean