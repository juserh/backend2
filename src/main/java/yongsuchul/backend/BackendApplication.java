package yongsuchul.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
//@EnableAutoConfiguration
public class BackendApplication {

	public static void main(String[] args) {
		System.setProperty( "jdk.tls.client.protocols", "TLSv1.2" );
		SpringApplication.run(BackendApplication.class, args);
	}

}
