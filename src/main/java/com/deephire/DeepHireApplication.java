package com.deephire;

import com.deephire.models.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DeepHireApplication {

	public static void main(String[] args) {
		User user= new User();
		user.setFirstName("John");
		System.out.println(user.getFirstName());
		SpringApplication.run(DeepHireApplication.class, args);

	}

}
