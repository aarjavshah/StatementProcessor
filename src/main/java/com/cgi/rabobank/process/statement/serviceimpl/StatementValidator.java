
package com.cgi.rabobank.process.statement.serviceimpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.cgi.rabobank.process.statement.model.Records.Record;

import lombok.extern.slf4j.Slf4j;

/**
 * This is a statement validation.
 * Contains methods that validates the statements as per the business rules.
 * 
 * @author arjav.shah
 *
 */
@Service
@Slf4j
public class StatementValidator {

	public List<Record> validateRecords(List<Record> records) {
		List<Record> recordList = new ArrayList<>();
		Set<Integer> referenceSet = new HashSet<>();
		float epsilon = 0.00001f;
		records.forEach(record->{
			float calculatedBalance = record.getStartBalance() + record.getMutation();
			if ((Math.abs(calculatedBalance - record.getEndBalance()) > epsilon)) {
				recordList.add(record);
			}
			Integer refNum = record.getReference();
			if (refNum!=null && !referenceSet.contains(refNum)) {
				referenceSet.add(refNum);
			} else {
				recordList.add(record);
			}
		});
		int count = records.size();
		log.debug("Finished processing {} records.",count);
		log.debug("Found {} failed records.",recordList.size());
		return recordList;
	}

}
