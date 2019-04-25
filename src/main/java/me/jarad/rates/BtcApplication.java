package me.jarad.rates;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration;

@EnableAutoConfiguration(exclude = RepositoryRestMvcAutoConfiguration.class)
@SpringBootApplication(scanBasePackages = "me.jarad.rates")
public class BtcApplication {

	public static void main(String[] args) {
		SpringApplication.run(BtcApplication.class, args);
	}

}
