package com.snebot.fbmoll.service;

import com.snebot.fbmoll.data.CSVRecord;
import com.snebot.fbmoll.data.CSVRequest;
import com.snebot.fbmoll.helper.CSVParserProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.List;

@SpringBootTest
public class CSVServiceTests {
    @Autowired
    private CSVRecordService csvRecordService;

    @Test
    void tryCSVService() {
        String content1 = "Identifier;Access code;Recovery code;First name;Last name;Department;Location\\n9012;12se74;rb9012;Rachel;Booker;Sales;Manchester\\n2070;04ap67;lg2070;Laura;Grey;Depot;London;Mallorca\\n4081;30no86;cj4081;Craig;Johnson;Depot;London\\n9346;14ju73;mj9346;Mary;Jenkins;Engineering;Manchester\\n5079;09ja61;js5079;Jamie;Smith;Engineering;Manchester";
        CSVParserProperties properties1 = new CSVParserProperties(';', '\n', '\"', true, false);
        CSVRequest request1 = new CSVRequest("test1", properties1, content1);
        CSVRecord record1 = this.csvRecordService.processCSVRequest(request1);
        Assert.notNull(record1, "failed to process CSVRequest 1.");

        String content2 = "Identifier,Access code,Recovery code,First name,Last name,Department,Location\\n9022,22se74,rb9022,Rachel,Booker,Sales,Manchester\\n2070,04ap67,lg2070,Laura,Grey,Depot,London,Mallorca\\n4082,30no86,cj4082,Craig,Johnson,Depot,London\\n9346,24ju73,mj9346,Mary,Jenkins,Engineering,Manchester\\n5079,09ja62,js5079,Jamie,Smith,Engineering,Manchester";
        CSVParserProperties properties2 = new CSVParserProperties(',', '\n', '\"', true, false);
        CSVRequest request2 = new CSVRequest("test2", properties2, content2);
        CSVRecord record2 = this.csvRecordService.processCSVRequest(request2);
        Assert.notNull(record2, "failed to process CSVRequest 2.");

        List<CSVRecord> result = this.csvRecordService.findCSVRecord(null, null, null, null, null, null, null);
        Assert.isTrue(result.size() == 2, "failed to search CSVRecords.");

        List<CSVRecord> result1 = this.csvRecordService.findCSVRecord(record1.getId(),
                record1.getName(),
                record1.getSeparator(),
                record1.getLineSeparator(),
                record1.getTextSeparator(),
                record1.isFirstLineColumnName(),
                record1.isLowerCaseTags());
        Assert.isTrue(result1.size() == 1 && record1.equals(result1.get(0)), "failed to find record 1.");

        List<CSVRecord> result2 = this.csvRecordService.findCSVRecord(record2.getId(),
                record2.getName(),
                record2.getSeparator(),
                record2.getLineSeparator(),
                record2.getTextSeparator(),
                record2.isFirstLineColumnName(),
                record2.isLowerCaseTags());
        Assert.isTrue(result2.size() == 1 && record2.equals(result2.get(0)), "failed to find record 2.");

        this.csvRecordService.delete(record1);
        this.csvRecordService.delete(record2);
        result = this.csvRecordService.findCSVRecord(null, null, null, null, null, null, null);
        Assert.isTrue(result.size() == 0, "failed to search CSVRecords.");
    }
}
