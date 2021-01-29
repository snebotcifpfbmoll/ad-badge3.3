package com.snebot.fbmoll.helper;

import com.snebot.fbmoll.data.CSVObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CSVParser {
    private static final Logger log = LoggerFactory.getLogger(CSVParser.class);
    private static final String VALUE_FORMAT_STRING = "value-%d";
    private static final String ELEMENT_TAG_NAME = "element";
    private static final String SPACE_REPLACEMENT = "-";
    private static final String TAG_REGEX = "^[^a-z]*|[^\\w-]|[^\\w]$";
    private CSVParserProperties properties = null;

    public CSVParserProperties getProperties() {
        if (properties == null) properties = new CSVParserProperties();
        return properties;
    }

    public void setProperties(CSVParserProperties properties) {
        this.properties = properties;
    }

    public CSVParser() {
    }

    public CSVParser(CSVParserProperties properties) {
        this.properties = properties;
    }

    /**
     * Add a value to a CSVObject.
     *
     * @param element     CSVObject element.
     * @param columnNames Column names.
     * @param name        Element name.
     * @param value       Element value.
     * @param lineIndex   Line index.
     * @param valueIndex  Value index.
     */
    private void add(CSVObject element, ArrayList<String> columnNames, String name, String value, int lineIndex, int valueIndex) {
        CSVParserProperties properties = getProperties();
        if (properties.isFirstLineColumnName()) {
            if (lineIndex == 0) {
                columnNames.add(value);
            } else if (valueIndex < columnNames.size()) {
                name = columnNames.get(valueIndex);
            }
        }
        element.add(name, value);
    }

    /**
     * Process CSV content and store it inside a list of CSVObjects.
     *
     * @param content CSV content.
     * @return CSVObject list.
     */
    public List<CSVObject> parse(String content) {
        CSVParserProperties properties = getProperties();
        char separator = properties.getSeparator();
        char lineSeparator = properties.getLineSeparator();
        char textSeparator = properties.getTextSeparator();

        CSVObject element = new CSVObject();
        ArrayList<CSVObject> elements = new ArrayList<>();
        ArrayList<String> columnNames = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();

        int lineIndex = 0;
        int valueIndex = 0;
        boolean text = false;
        char[] chars = content.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char character = chars[i];
            if (character == textSeparator) {
                text = !text;
            } else if (character == separator && !text) {
                String value = stringBuilder.toString();
                String name = String.format(VALUE_FORMAT_STRING, valueIndex);
                add(element, columnNames, name, value, lineIndex, valueIndex);
                stringBuilder.setLength(0);
                valueIndex += 1;
            } else if ((character == lineSeparator || i == chars.length - 1) && !text) {
                String value = stringBuilder.toString();
                String name = String.format(VALUE_FORMAT_STRING, valueIndex);
                add(element, columnNames, name, value, lineIndex, valueIndex);
                elements.add(element);
                stringBuilder.setLength(0);
                valueIndex = 0;
                lineIndex++;
                element = new CSVObject();
            } else {
                stringBuilder.append(character);
            }
        }

        return elements;
    }

    /**
     * Generate an XML document from CSVObject list.
     *
     * @param objects  CSVObject list.
     * @param rootName Root element name.
     * @return Processed document object.
     */
    public Document convertToDocument(List<CSVObject> objects, String rootName) {
        Document document = null;
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            document = documentBuilder.newDocument();

            Element root = document.createElement(stripCharacters(rootName));
            document.appendChild(root);

            CSVParserProperties properties = getProperties();
            int initialValue = properties.isFirstLineColumnName() ? 1 : 0;
            for (int i = initialValue; i < objects.size(); i++) {
                CSVObject object = objects.get(i);
                Element objectElement = document.createElement(ELEMENT_TAG_NAME);
                HashMap<String, String> elements = object.getElements();
                String[] keySet = elements.keySet().toArray(new String[0]);
                for (int j = 0; j < keySet.length; j++) {
                    String key = keySet[j];
                    String str = StringEscapeUtils.escapeXml10(elements.get(key));
                    String tagName = stripCharacters(key);
                    if (properties.isLowerCaseTags()) tagName = tagName.toLowerCase();
                    if (tagName == null || tagName.equals(StringUtils.EMPTY))
                        tagName = String.format(VALUE_FORMAT_STRING, j);
                    Element value = document.createElement(tagName);
                    value.setTextContent(str);
                    objectElement.appendChild(value);
                }
                root.appendChild(objectElement);
            }
        } catch (Exception e) {
            log.error("failed to process CSVObject list: ", e);
            return null;
        }

        return document;
    }

    /**
     * Convert DOM Document to XML string.
     *
     * @param document Document to convert.
     * @return XML string.
     */
    public String documentToXML(Document document) {
        String result = null;
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            StringWriter stringWriter = new StringWriter();
            transformer.transform(new DOMSource(document), new StreamResult(stringWriter));
            result = stringWriter.getBuffer().toString();
        } catch (Exception e) {
            log.error("failed to transform document to string: ", e);
        }
        return result;
    }

    /**
     * Convert CSVObject list to XML string.
     *
     * @param elements    CSVObject list.
     * @param rootElement Root element name.
     * @return XML string.
     */
    public String convert(List<CSVObject> elements, String rootElement) {
        Document document = convertToDocument(elements, rootElement);
        return documentToXML(document);
    }

    /**
     * Modify text to fit certain requirements:
     * - Alphanumeric characters only. White spaces are converted to '-'.
     * - First character must be between A-Z.
     * - Last character must be alphanumeric.
     *
     * @param str Text to process.
     * @return Modified text.
     */
    public String stripCharacters(String str) {
        String result = null;
        str = StringUtils.replace(str, StringUtils.SPACE, SPACE_REPLACEMENT);
        try {
            Pattern pattern = Pattern.compile(TAG_REGEX, Pattern.CASE_INSENSITIVE);
            Matcher m = pattern.matcher(str);
            result = m.replaceAll(StringUtils.EMPTY);
        } catch (Exception e) {
            log.error("failed to strip characters: ", e);
        }
        return result;
    }
}
