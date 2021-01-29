package com.snebot.fbmoll.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.snebot.fbmoll.helper.CSVParserProperties;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;

@Entity
@Table(name = "csv_record")
public class CSVRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "value_separator")
    private char separator;
    @Column(name = "line_separator")
    private char lineSeparator;
    @Column(name = "text_separator")
    private char textSeparator;
    @Column(name = "first_line_as_name")
    private boolean firstLineColumnName;
    @Column(name = "lowercase_tags")
    private boolean lowerCaseTags;
    @Column(name = "content")
    private String content;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Transient
    private String xml;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public char getSeparator() {
        return separator;
    }

    public void setSeparator(char separator) {
        this.separator = separator;
    }

    public char getLineSeparator() {
        return lineSeparator;
    }

    public void setLineSeparator(char lineSeparator) {
        this.lineSeparator = lineSeparator;
    }

    public char getTextSeparator() {
        return textSeparator;
    }

    public void setTextSeparator(char textSeparator) {
        this.textSeparator = textSeparator;
    }

    public boolean isFirstLineColumnName() {
        return firstLineColumnName;
    }

    public void setFirstLineColumnName(boolean firstLineColumnName) {
        this.firstLineColumnName = firstLineColumnName;
    }

    public boolean isLowerCaseTags() {
        return lowerCaseTags;
    }

    public void setLowerCaseTags(boolean lowerCaseTags) {
        this.lowerCaseTags = lowerCaseTags;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public void setCSVProperties(CSVParserProperties properties) {
        this.separator = properties.getSeparator();
        this.lineSeparator = properties.getLineSeparator();
        this.textSeparator = properties.getTextSeparator();
        this.firstLineColumnName = properties.isFirstLineColumnName();
        this.lowerCaseTags = properties.isLowerCaseTags();
    }

    public CSVRecord() {
    }

    public CSVRecord(String name, char separator, char lineSeparator, char textSeparator, boolean firstLineColumnName, boolean lowerCaseTags, String content) {
        this.name = name;
        this.separator = separator;
        this.lineSeparator = lineSeparator;
        this.textSeparator = textSeparator;
        this.firstLineColumnName = firstLineColumnName;
        this.lowerCaseTags = lowerCaseTags;
        this.content = content;
    }

    public CSVRecord(String name, CSVParserProperties properties, String content, String xml) {
        this.name = name;
        setCSVProperties(properties);
        this.content = content;
        this.xml = xml;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof CSVRecord)) return false;
        CSVRecord record = (CSVRecord) obj;
        return this.id.equals(record.id) &&
                StringUtils.equals(this.name, record.name) &&
                StringUtils.equals(this.content, record.content) &&
                this.separator == record.separator &&
                this.lineSeparator == record.lineSeparator &&
                this.textSeparator == record.textSeparator &&
                this.firstLineColumnName == record.firstLineColumnName &&
                this.lowerCaseTags == record.lowerCaseTags;
    }
}