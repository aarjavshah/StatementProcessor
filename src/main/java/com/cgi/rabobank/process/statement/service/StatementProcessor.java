package com.cgi.rabobank.process.statement.service;

import java.io.File;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cgi.rabobank.process.statement.exceptions.RecordProcessingException;
import com.cgi.rabobank.process.statement.model.Records.Record;

/**
 * Generic Interface that can further be implemented for a particular type of
 * file Processing
 * 
 * @author arjav.shah
 *
 */
@Service
public interface StatementProcessor {

	public List<Record> processStatement(File statementFile) throws RecordProcessingException;

}
