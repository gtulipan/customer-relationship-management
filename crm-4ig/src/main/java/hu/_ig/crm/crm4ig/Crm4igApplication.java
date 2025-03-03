package hu._ig.crm.crm4ig;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

@Slf4j
@SpringBootApplication
@EntityScan(basePackages = "hu._ig.crm.crm4ig.domain")
public class Crm4igApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(Crm4igApplication.class, args);
		Environment env = context.getEnvironment();
		log.debug("***************** The {} application started on {} port *****************", env.getProperty("spring.application.name"), env.getProperty("server.port"));
	}

}
