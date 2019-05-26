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
import com.cgi.rabobank.process.statement.serviceimpl.XMLProcessor;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
public class CustomerStatementProcessorTests {

	StatementProcessor xmlProcessor, csvProcessor;

	@Before
	public void InitiateObjects() {
		xmlProcessor = new XMLProcessor();
		csvProcessor = new CSVProcessor();
	}

	@Test
	public void parseXMLTest() throws RecordProcessingException {

		assertEquals(10,
				xmlProcessor
						.processStatement(
								new File(this.getClass().getClassLoader().getResource("records.xml").getFile()))
						.size());

	}

	@Test
	public void parseCSVTest() throws RecordProcessingException {

		assertEquals(10,
				csvProcessor
						.processStatement(
								new File(this.getClass().getClassLoader().getResource("records.csv").getFile()))
						.size());

	}

	@Test(expected = RecordProcessingException.class)
	public void processInvalidFiles() throws RecordProcessingException {
		assertEquals(10,
				xmlProcessor
						.processStatement(
								new File(this.getClass().getClassLoader().getResource("recordsInvalid.xml").getFile()))
						.size());

		assertEquals(10, csvProcessor.processStatement(new File("someFile.csv")).size());

	}

}
