package com.jalgoz.encuesta;

import com.jalgoz.encuesta.security.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class EncuestaApplication {

	public static void main(String[] args) {
		SpringApplication.run(EncuestaApplication.class, args);
		System.out.println("Funcionando");
	}

	// Para encriptar la contraseña, lo creamos aquí para que podamos acceder en toda la app
	@Bean public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// Tenemos que crear el @Bean que nos ayudará a retornar las demás @Bean
	@Bean
	public SpringApplicationContext springApplicationContext() {
		return new SpringApplicationContext();
	}

	// Creamos un @Bean que nos ayude a obtener el token secreto
	@Bean(name = "AppProperties")
	public AppProperties getAppProperties() {
		return new AppProperties();
	}

}
