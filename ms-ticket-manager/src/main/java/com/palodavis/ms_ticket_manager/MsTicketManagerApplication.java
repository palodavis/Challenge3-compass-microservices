package com.palodavis.ms_ticket_manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})

public class MsTicketManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsTicketManagerApplication.class, args);
	}

}
