package com.deephire;

import com.deephire.Models.User;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.crypto.SecretKey;

@SpringBootApplication
public class DeepHireApplication {

	public static void main(String[] args) {
		User user = new User();
		user.setFirstName("test/////////////");
		System.out.println(user.getFirstName());

		SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
		String base64Key = Encoders.BASE64.encode(key.getEncoded());
		System.out.println("Your new JWT secret: " + base64Key);



		SpringApplication.run(DeepHireApplication.class, args);

	}

}
