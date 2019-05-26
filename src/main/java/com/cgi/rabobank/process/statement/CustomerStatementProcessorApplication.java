package com.cgi.rabobank.process.statement;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeExceptionMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.context.annotation.Bean;

import com.cgi.rabobank.process.statement.exceptions.InvalidFileTypeException;
import com.cgi.rabobank.process.statement.exceptions.RecordProcessingException;
import com.cgi.rabobank.process.statement.model.Records.Record;
import com.cgi.rabobank.process.statement.service.StatementProcessor;
import com.cgi.rabobank.process.statement.serviceimpl.ReportGenerator;
import com.cgi.rabobank.process.statement.serviceimpl.StatementValidator;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@EnableTask
@Slf4j
public class CustomerStatementProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(CustomerStatementProcessorApplication.class, args);
	}

	@Bean
	public StatementProcessing statementProcessing() {
		return new StatementProcessing();
	}

	public class StatementProcessing implements CommandLineRunner {
		@Autowired
		@Qualifier("XMLProcessor")
		private StatementProcessor statementProcessor;
		@Autowired
		@Qualifier("CSVProcessor")
		private StatementProcessor statementProcessorCsv;
		@Autowired
		private ReportGenerator reportGenerator;
		@Autowired
		private StatementValidator statementValidator;

		@Override
		public void run(String... args) throws Exception {
			if (args.length == 2) {
				String fileName = args[1];
				String fileType = fileName.substring(fileName.lastIndexOf('.'));
				log.debug("File Name: {}", fileName);
				log.debug("File Type: {}", fileType);
				if (fileType.equalsIgnoreCase(".xml")) {
					processFile(fileName, fileType, statementProcessor);
				} else if (fileType.equalsIgnoreCase(".csv")) {
					processFile(fileName, fileType, statementProcessorCsv);
				} else {
					log.error("Invalid File type {}", fileType);
					throw new InvalidFileTypeException("The file type is not valid for processing.");
				}
			} else {
				log.error("Mismatch in the number of parameters");
			}
		}

		public void processFile(String fileName, String fileType, StatementProcessor statementProcessor) throws RecordProcessingException {
			List<Record> records = statementProcessor.processStatement(new File(fileName));
			if (records != null) {
				reportGenerator.generateReport(statementValidator.validateRecords(records), fileType.substring(1));
			}
			else {
				log.error("Error in reading records from file {}", fileName);
			}
		}

	}
	
	@Bean
	ExitCodeExceptionMapper exitCodeMapper() {
		return exception -> {
		      if (exception.getCause() instanceof InvalidFileTypeException) {
		        return 3;
		      }
		      else if (exception.getCause() instanceof RecordProcessingException) {
				return 2;
			}
		      return 1;
		    };
	}
	

}
