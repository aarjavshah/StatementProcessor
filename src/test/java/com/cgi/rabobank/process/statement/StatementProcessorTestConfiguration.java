package com.cgi.rabobank.process.statement;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import com.cgi.rabobank.process.statement.service.StatementProcessor;
import com.cgi.rabobank.process.statement.serviceimpl.CSVProcessor;
import com.cgi.rabobank.process.statement.serviceimpl.XMLProcessor;

@Profile("test")
@Configuration
public class StatementProcessorTestConfiguration {
	@Bean
	@Primary
	public StatementProcessor xmlProcessor() {
		return Mockito.mock(XMLProcessor.class);
	}

	@Bean
	public StatementProcessor csvProcessor() {
		return Mockito.mock(CSVProcessor.class);
	}

}
