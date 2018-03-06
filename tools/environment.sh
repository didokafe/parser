#!/bin/bash

mysql -u root -proot < tools/migration.sql
#create database parser;


mvn clean package
java -jar target/parser.jar --accesslog=/home/daniel/Dropbox/Java_MySQL_Test/access.log --startDate=2017-01-01.15:00:00 --duration=hourly --threshold=200

#datite trqbva da opravq
#select po ip
#documentation
#clean
#sql file
#select r.* from log_entry r where r.date BETWEEN '2017-01-01.15:00:00' AND '2017-01-01.15:59:59' GROUP BY r.ip HAVING count(r.ip) > 200;