package com.deephire;

import com.deephire.Models.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DeepHireApplication {

	public static void main(String[] args) {
		User user = new User();
		user.setFirstName("test/////////////");
		System.out.println(user.getFirstName());

		SpringApplication.run(DeepHireApplication.class, args);

	}

}
