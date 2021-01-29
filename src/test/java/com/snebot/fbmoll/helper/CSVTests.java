package com.snebot.fbmoll.helper;

import com.snebot.fbmoll.data.CSVObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;
import org.w3c.dom.Document;

import java.util.List;

@SpringBootTest
public class CSVTests {
    @Test
    void tryCSVParse() {
        String content = "col1,col2,col3,col4\ntest,\"this is, a test\",final test\n\"test, number, 2,\",ok,maybe,it works!";
        CSVParserProperties properties = new CSVParserProperties(',', '\n', '\"', true, false);
        CSVParser parser = new CSVParser(properties);
        List<CSVObject> objects = parser.parse(content);
        Document document = parser.convertToDocument(objects, "root");
        Assert.notNull(document, "failed to convert to document");
        String docToXML = parser.documentToXML(document);
        Assert.notNull(docToXML, "failed to convert document to xml");
        String result = parser.convert(objects, "root");
        Assert.notNull(result, "failed to convert to xml");
    }

    @Test
    void tryStripCharacters() {
        CSVParser parser = new CSVParser();
        String correct = "Test-1";
        String content = "0-Te#st-/1-";
        String result = parser.stripCharacters(content);
        Assert.isTrue(correct.equals(result), "failed to strip invalid characters");
    }
}
