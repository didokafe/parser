#!/bin/bash

#mysql -u root -proot;
#create database parser;


mvn clean package
java -jar target/parser.jar --accesslog=/home/dido/Dropbox/Java_MySQL_Test/Java_MySQL_Test_Instructions.txt