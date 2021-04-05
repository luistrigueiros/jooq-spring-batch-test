package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) throws SQLException {
		System.exit(SpringApplication.exit(SpringApplication.run(DemoApplication.class, args)));
	}

}
