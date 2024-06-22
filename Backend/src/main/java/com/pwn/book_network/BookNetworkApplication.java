package com.pwn.book_network;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import com.pwn.book_network.role.*;

import javax.management.relation.Role;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware") // enable Auditing Mechanisms with the bean --> which enables auditing by userId
@EnableConfigurationProperties
@EnableAsync
public class BookNetworkApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookNetworkApplication.class, args);
	}

	@Bean
	public CommandLineRunner runner(roleRepository roleRepository){
		return args -> {
			if(roleRepository.findByName("USER").isEmpty()){
				roleRepository.save(
						Roles.builder().name("USER").build()
				);
			}
		};
	}

}
