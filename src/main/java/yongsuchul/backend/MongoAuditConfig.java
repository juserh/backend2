package yongsuchul.backend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

import java.time.LocalDateTime;
import java.util.Optional;

@Configuration
//@EnableMongoRepositories("yongsuchul.backend.repository")
@EnableMongoAuditing(dateTimeProviderRef = "auditingDateTimeProvider")
public class MongoAuditConfig {
    @Bean(name="auditingDateTimeProvider")
    public DateTimeProvider dateTimeProvider(){
        return()-> Optional.of(LocalDateTime.now());
    }
}
