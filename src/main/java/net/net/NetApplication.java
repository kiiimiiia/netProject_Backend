package net.net;

import net.net.file.FileStorage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories("net.net.repository")
@EntityScan("net.net.entity")
@SpringBootApplication
@EnableConfigurationProperties({FileStorage.class})
public class NetApplication {

    public static void main(String[] args) {
        SpringApplication.run(NetApplication.class, args);
    }

}
