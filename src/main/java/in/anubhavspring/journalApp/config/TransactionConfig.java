package in.anubhavspring.journalApp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//trx configs
@Configuration
@EnableTransactionManagement//to enable trx
public class TransactionConfig {

    //	creating a bean which manages our trx
    @Bean
    public PlatformTransactionManager trxManager(MongoDatabaseFactory dbFactory){
        return new MongoTransactionManager(dbFactory);
    }
}

