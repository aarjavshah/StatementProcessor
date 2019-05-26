package com.cgi.rabobank.process.statement;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cgi.rabobank.process.statement.exceptions.RecordProcessingException;
import com.cgi.rabobank.process.statement.service.StatementProcessor;
import com.cgi.rabobank.process.statement.serviceimpl.CSVProcessor;
import com.cgi.rabobank.process.statement.serviceimpl.StatementValidator;
import com.cgi.rabobank.process.statement.serviceimpl.XMLProcessor;
import com.google.common.collect.Lists;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
public class StatementValidatorTests {

	StatementProcessor xmlProcessor, csvProcessor;

	@Before
	public void InitiateObjects() {
		xmlProcessor = new XMLProcessor();
		csvProcessor = new CSVProcessor();
	}

	@Test
	public void validateXMLTest() throws RecordProcessingException {
		assertEquals(
				2, Lists
						.newArrayList(new StatementValidator().validateRecords(xmlProcessor.processStatement(
								new File(this.getClass().getClassLoader().getResource("records.xml").getFile()))))
						.size());

	}

	@Test
	public void validateCSVTest() throws RecordProcessingException {
		assertEquals(
				2, Lists
						.newArrayList(new StatementValidator().validateRecords(csvProcessor.processStatement(
								new File(this.getClass().getClassLoader().getResource("records.csv").getFile()))))
						.size());

	}

}
