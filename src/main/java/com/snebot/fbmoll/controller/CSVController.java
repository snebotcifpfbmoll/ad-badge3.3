package com.snebot.fbmoll.controller;

import com.snebot.fbmoll.data.CSVRecord;
import com.snebot.fbmoll.data.CSVRequest;
import com.snebot.fbmoll.service.CSVRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/csv")
public class CSVController {
    private static final Logger log = LoggerFactory.getLogger(CSVController.class);
    private final CSVRecordService csvRecordService;

    public CSVController(CSVRecordService csvRecordService) {
        this.csvRecordService = csvRecordService;
    }

    /**
     * Convert CSV formatted text to XML.
     *
     * @param request CSVRequest object.
     * @return XML result.
     */
    @RequestMapping(value = "", method = {RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> csv(@RequestBody CSVRequest request) {
        try {
            Object result = this.csvRecordService.processCSVRequest(request);
            HttpStatus status = result != null ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
            return new ResponseEntity<>(result, status);
        } catch (Exception e) {
            log.error("failed to precess request ", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Find all CSVRecords with optional attributes.
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
    @RequestMapping(value = "/records", method = {RequestMethod.GET, RequestMethod.DELETE}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> csvAll(@RequestParam(value = "id", required = false) Integer id,
                                         @RequestParam(value = "name", required = false) String name,
                                         @RequestParam(value = "separator", required = false) Character separator,
                                         @RequestParam(value = "lineSeparator", required = false) Character lineSeparator,
                                         @RequestParam(value = "textSeparator", required = false) Character textSeparator,
                                         @RequestParam(value = "firstLineColumnName", required = false) Boolean firstLineColumnName,
                                         @RequestParam(value = "lowercaseTags", required = false) Boolean lowercaseTags,
                                         HttpServletRequest request) {
        try {
            Object result = null;
            HttpStatus status = HttpStatus.OK;
            List<CSVRecord> records = this.csvRecordService.findCSVRecord(id, name, separator, lineSeparator, textSeparator, firstLineColumnName, lowercaseTags);
            switch (RequestMethod.valueOf(request.getMethod())) {
                case GET:
                    result = records;
                    break;
                case DELETE:
                    records.forEach(this.csvRecordService::delete);
                    result = records;
                    break;
                default:
                    log.warn("received unexpected request method: " + request.getMethod());
                    break;
            }
            return new ResponseEntity<>(result, status);
        } catch (Exception e) {
            log.error("failed to precess request ", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
