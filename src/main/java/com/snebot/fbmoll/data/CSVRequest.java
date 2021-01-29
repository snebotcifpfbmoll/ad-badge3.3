package com.snebot.fbmoll.data;

import com.snebot.fbmoll.helper.CSVParserProperties;

public class CSVRequest {
    private String name;
    private CSVParserProperties properties;
    private String content;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CSVParserProperties getProperties() {
        return properties;
    }

    public void setProperties(CSVParserProperties properties) {
        this.properties = properties;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public CSVRequest(String name, CSVParserProperties properties, String content) {
        this.name = name;
        this.properties = properties;
        this.content = content;
    }
}
