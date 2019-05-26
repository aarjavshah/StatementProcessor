
package com.cgi.rabobank.process.statement.serviceimpl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.cgi.rabobank.process.statement.model.Records.Record;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import lombok.extern.slf4j.Slf4j;

/**
 * This is for report generation. Contains methods for document creation and
 * data insertion
 * 
 * @author arjav.shah
 *
 */
@Service
@Slf4j
public class ReportGenerator {

	public void generateReport(List<Record> recordList, String fileName) {
		com.itextpdf.text.Document document = createDocument(fileName);
		try {
			document.open();
			PdfPTable table = new PdfPTable(2);
			document.addTitle("Report on Customer Record Processing");
			addTableHeader(table);
			recordList.stream().forEach(record -> addRows(table, record));
			log.debug("Finished inserting data into report.");
			document.add(table);
		} catch (DocumentException e) {
			log.error("Error Occurred while writing the document {}", e);
		} finally {
			document.close();
		}
	}

	private Document createDocument(String fileName) {
		Document document = new Document();
		String timeStamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Timestamp(System.currentTimeMillis()));
		String reportfilename = "RecordProcessingReport_" + fileName + "_" + timeStamp + ".pdf";
		try {
			PdfWriter.getInstance(document, new FileOutputStream(reportfilename));
		} catch (FileNotFoundException e) {
			log.error("Output file not found {}", e);
		} catch (DocumentException e) {
			log.error("Error Occurred while creating the document {}", e);
		}
		log.debug("Generated blank document {}", reportfilename);
		return document;
	}

	private void addTableHeader(PdfPTable table) {
		Stream.of("Reference", "Description").forEach(columnTitle -> {
			PdfPCell header = new PdfPCell();
			header.setBackgroundColor(BaseColor.LIGHT_GRAY);
			header.setBorderWidth(2);
			header.setPhrase(new Phrase(columnTitle));
			table.addCell(header);
		});
	}

	private void addRows(PdfPTable table, Record record) {
		table.addCell(String.valueOf(record.getReference()));
		table.addCell(record.getDescription());
		table.completeRow();
	}

}
