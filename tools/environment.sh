#!/bin/bash

#mysql -u root -proot;
#create database parser;


mvn clean package
java -jar target/parser.jar --accesslog=/home/daniel/Dropbox/Java_MySQL_Test/access.log

#select r.ip from (select ff.ip, count(ff.ip) from log_file ff where ff.date BETWEEN '2017-01-01.13:00:00' AND '2017-01-01.14:00:00' GROUP BY ff.ip HAVING count(ff.ip) > 200) as r;
