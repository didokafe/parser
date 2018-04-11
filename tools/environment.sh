#!/bin/bash

USER="root"
PASS="root"

mysql -u $USER -p$PASS < tools/migration.sql

./mvnw clean package
#put a real pat to file
java -jar target/parser.jar --accesslog=~/access.log --startDate=2017-01-01.15:00:00 --duration=hourly --threshold=200

