package com.snebot.fbmoll.service;

import com.snebot.fbmoll.data.CSVObject;
import com.snebot.fbmoll.data.CSVRecord;
import com.snebot.fbmoll.data.CSVRecordRepository;
import com.snebot.fbmoll.data.CSVRequest;
import com.snebot.fbmoll.helper.CSVParser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CSVRecordService {
    private static final Logger log = LoggerFactory.getLogger(CSVRecordService.class);
    private final CSVRecordRepository csvRecordRepository;

    public CSVRecordService(CSVRecordRepository csvRecordRepository) {
        this.csvRecordRepository = csvRecordRepository;
    }

    /**
     * Find CSVRecords matching given attributes.
     *
     * @param id                  CSVRecord id.
     * @param name                CSVRecord name.
     * @param separator           CSVRecord value separator.
     * @param lineSeparator       CSVRecord line separator.
     * @param textSeparator       CSVRecord text separator.
     * @param firstLineColumnName CSVRecord first line as column name flag.
     * @param lowercaseTags       CSVRecord lowercase tags flag.
     * @return CSVRecord list matching attributes.
     */
    public List<CSVRecord> findCSVRecord(Integer id, String name, Character separator, Character lineSeparator, Character textSeparator, Boolean firstLineColumnName, Boolean lowercaseTags) {
        List<CSVRecord> result = new ArrayList<>();
        Iterable<CSVRecord> records = this.csvRecordRepository.findAll();
        for (CSVRecord record : records) {
            if ((id == null || record.getId().equals(id)) &&
                    (name == null || StringUtils.equals(record.getName(), name)) &&
                    (separator == null || separator.equals(record.getSeparator())) &&
                    (lineSeparator == null || lineSeparator.equals(record.getLineSeparator())) &&
                    (textSeparator == null || textSeparator.equals(record.getTextSeparator())) &&
                    (firstLineColumnName == null || record.isFirstLineColumnName() == firstLineColumnName) &&
                    (lowercaseTags == null || record.isLowerCaseTags() == lowercaseTags)) result.add(record);
        }
        return result;
    }

    /**
     * Delete CSVRecord.
     *
     * @param record CSVRecord to delete.
     */
    public void delete(CSVRecord record) {
        this.csvRecordRepository.delete(record);
    }

    /**
     * Process CSVRequest.
     *
     * @param request CSVRequest to process.
     * @return CSVRecord that has been persisted to database.
     */
    public CSVRecord processCSVRequest(CSVRequest request) {
        try {
            CSVParser parser = new CSVParser(request.getProperties());
            List<CSVObject> elements = parser.parse(request.getContent());
            String content = parser.convert(elements, request.getName());
            CSVRecord record = new CSVRecord(request.getName(), request.getProperties(), request.getContent(), content);
            this.csvRecordRepository.save(record);
            return record;
        } catch (Exception e) {
            log.error("failed to process CSVRequest ", e);
            return null;
        }
    }
}
