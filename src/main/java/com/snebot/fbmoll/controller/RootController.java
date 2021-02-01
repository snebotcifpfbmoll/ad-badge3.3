package com.snebot.fbmoll.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class RootController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * Root message.
     *
     * @return Hello message.
     */
    @RequestMapping(value = "", method = {RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> rootMessage() {
        try {
            return new ResponseEntity<>(new String[]{"Hello from ad-badge3.3 :)", "this is a test"}, HttpStatus.OK);
        } catch (Exception e) {
            log.error("failed to precess request ", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
