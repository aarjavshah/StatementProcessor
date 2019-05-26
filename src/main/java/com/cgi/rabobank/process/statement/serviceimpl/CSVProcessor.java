package com.cgi.rabobank.process.statement.serviceimpl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cgi.rabobank.process.statement.exceptions.RecordProcessingException;
import com.cgi.rabobank.process.statement.model.Records.Record;
import com.cgi.rabobank.process.statement.service.StatementProcessor;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of {@link StatementProcessor} for .csv type of file processing
 * 
 * @author arjav.shah
 *
 */
@Service
@Slf4j
public class CSVProcessor implements StatementProcessor {

	@Override
	public List<Record> processStatement(File statementFile) throws RecordProcessingException {
		try (FileReader fileReader = new FileReader(statementFile)) {

			CsvToBean<Record> csvToBean = new CsvToBeanBuilder<Record>(fileReader).withType(Record.class).build();
			
			return csvToBean.parse();
		} catch (FileNotFoundException e) {
			log.error("Unable to locate the CSV File ", e);
			throw new RecordProcessingException(e.getMessage());
		} catch (IOException e) {
			log.error("Error while processing CSV file", e);
			throw new RecordProcessingException(e.getMessage());
		}
	}

}
