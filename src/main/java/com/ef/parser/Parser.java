package com.ef.parser;

import com.ef.parser.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableTask
@EnableJpaRepositories(basePackages="com.ef.parser.repository")
public class Parser {


	public static void main(String[] args) {
        /*System.out.println("$$$$$$$$$$$$$: " + args[0]);
        System.out.println("$$$$$$$$$$$$$: " + args[1]);*/
		SpringApplication.run(Parser.class, args);

	}

}
