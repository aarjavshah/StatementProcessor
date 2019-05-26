package com.cgi.rabobank.process.statement.serviceimpl;

import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.springframework.stereotype.Service;

import com.cgi.rabobank.process.statement.exceptions.RecordProcessingException;
import com.cgi.rabobank.process.statement.model.ObjectFactory;
import com.cgi.rabobank.process.statement.model.Records;
import com.cgi.rabobank.process.statement.model.Records.Record;
import com.cgi.rabobank.process.statement.service.StatementProcessor;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class XMLProcessor implements StatementProcessor {

	@Override
	public List<Record> processStatement(File statementFile) throws RecordProcessingException {
		Unmarshaller jaxbUnmarshaller;
		try {
			jaxbUnmarshaller = JAXBContext.newInstance(ObjectFactory.class).createUnmarshaller();

			JAXBElement<Records> root = jaxbUnmarshaller.unmarshal(new StreamSource(statementFile), Records.class);
			return root.getValue().getRecord();
		} catch (JAXBException e) {
			log.error("Exception Occurred while processing XML File", e);
			throw new RecordProcessingException(e.getMessage());
		}
	}

}
