package com.snebot.fbmoll.data;

import java.util.HashMap;

public class CSVObject {
    private static final String ELEMENT_TAG_FORMAT = "%s-%d";
    private final HashMap<String, String> elements = new HashMap<>();

    public HashMap<String, String> getElements() {
        return elements;
    }

    public CSVObject() {
    }

    public void add(String tag, String value) {
        int iterations = 0;
        String newTag = tag;
        while (elements.containsKey(newTag)) {
            newTag = String.format(ELEMENT_TAG_FORMAT, tag, iterations++);
        }
        elements.put(newTag, value);
    }

    public void remove(String tag) {
        elements.remove(tag);
    }
}
