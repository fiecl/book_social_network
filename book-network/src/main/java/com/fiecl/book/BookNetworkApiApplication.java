package com.fiecl.book;

import com.fiecl.book.role.Role;
import com.fiecl.book.role.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
public class BookNetworkApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookNetworkApiApplication.class, args);
		System.out.println("Spring Boot is running...");
	}

	// To initialize the user (to prevent the error: "ROLE USER was not initialized" when running the SpringBoot app)

	@Bean
	public CommandLineRunner runner(RoleRepository roleRepository) {
		return args -> {
			if (roleRepository.findByName("USER").isEmpty()) {
				roleRepository.save(
						Role.builder().name("USER").build()
				);
			}
		};
	}
}
