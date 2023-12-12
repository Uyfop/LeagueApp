package org.example;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.example.tablesImport.CreateModel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EntityScan("org.example.tables")
@EnableJpaRepositories("org.example.repositories")
public class Main {

    public static void main(String[] args) {
        System.out.println("STARTED");
        SpringApplication.run(Main.class, args);
        System.out.println("Done");
    }



}
