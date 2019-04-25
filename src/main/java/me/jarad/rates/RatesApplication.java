package me.jarad.rates;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

@Slf4j
@EnableAutoConfiguration(exclude = RepositoryRestMvcAutoConfiguration.class)
@SpringBootApplication(scanBasePackages = "me.jarad.rates")
public class RatesApplication {

	public static void main(String[] args) throws UnknownHostException {
		ConfigurableApplicationContext run = SpringApplication.run(RatesApplication.class, args);

		Environment env = run.getEnvironment();

		log.info("\n----------------------------------------------------------\n\t" +
						"Application '{}' is running! Access URLs:\n\t" +
						"Local: \t\thttp://127.0.0.1:{}\n\t" +
						"Swagger: \thttp://127.0.0.1:{}/swagger-ui.html\n\t" +
						"External: \thttp://{}:{}\n\t" +
						"----------------------------------------------------------",
				env.getProperty("spring.application.name"),
				env.getProperty("server.port")+env.getProperty("server.servlet.contextPath"),
				env.getProperty("server.port")+env.getProperty("server.servlet.contextPath"),
				InetAddress.getLocalHost().getHostAddress(),
				env.getProperty("server.port")+env.getProperty("server.servlet.contextPath"));
		log.info("Running with Spring profile(s) : {}", Arrays.toString(env.getActiveProfiles()));
	}

}
