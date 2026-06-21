package in.anubhavspring.journalApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class JournalApplication {

	/**
	 * Bootstraps and starts the Spring application.
	 */
	public static void main(String[] args) {
		SpringApplication.run(JournalApplication.class, args);
	}


	/**
	 * Creates a RestTemplate bean for dependency injection in the Spring application context.
	 *
	 * @return a RestTemplate instance
	 */
	@Bean
	public RestTemplate restTemplate() {
//		so that this bean instance is ready to be injected
		return new RestTemplate();
	}
}
