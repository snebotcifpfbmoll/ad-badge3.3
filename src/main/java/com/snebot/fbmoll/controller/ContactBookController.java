package com.snebot.fbmoll.controller;

import com.snebot.fbmoll.data.Contact;
import com.snebot.fbmoll.data.ContactBook;
import com.snebot.fbmoll.service.ContactBookService;
import com.snebot.fbmoll.service.ContactService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/book")
public class ContactBookController {
    private static final Logger log = LoggerFactory.getLogger(ContactBookController.class);
    private final ContactBookService contactBookService;
    private final ContactService contactService;

    public ContactBookController(ContactBookService contactBookService, ContactService contactService) {
        this.contactBookService = contactBookService;
        this.contactService = contactService;
    }

    /**
     * Responds to different RequestMethods on "/book":
     * - GET: List all contact books that match the query.
     * - POST: Create a new contact book.
     *
     * @param name    Contact book name.
     * @param request RequestMethod type.
     * @return Processed contact book.
     */
    @RequestMapping(value = "", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> book(@RequestParam(value = "name", required = false) String name,
                                       HttpServletRequest request) {
        try {
            Object result = null;
            HttpStatus status = HttpStatus.OK;
            switch (RequestMethod.valueOf(request.getMethod())) {
                case GET:
                    if (name == null) {
                        result = this.contactBookService.findAll();
                    } else {
                        List<ContactBook> books = this.contactBookService.findByName(name);
                        if (books == null || books.size() == 0) status = HttpStatus.NOT_FOUND;
                        result = books;
                    }
                    break;
                case POST:
                    if (name != null) {
                        ContactBook contactBook = new ContactBook(name);
                        this.contactBookService.save(contactBook);
                        result = contactBook;
                    }
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

    /**
     * Responds to different RequestMethods:
     * - GET: Get contact book by id.
     * - PUT: Update contact book by id.
     * - DELETE: Delete contact by id.
     *
     * @param id             Contact book id.
     * @param newContactBook New contact book data.
     * @param request        Request method type.
     * @return Processed contact book.
     */
    @RequestMapping(value = "/{id}", method = {RequestMethod.GET, RequestMethod.PUT, RequestMethod.DELETE}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> bookId(@PathVariable(value = "id") Integer id,
                                         @RequestBody(required = false) ContactBook newContactBook,
                                         HttpServletRequest request) {
        try {
            Object result = null;
            HttpStatus status = HttpStatus.OK;
            ContactBook contactBook = this.contactBookService.findById(id);
            if (contactBook != null) {
                switch (RequestMethod.valueOf(request.getMethod())) {
                    case GET:
                        result = contactBook;
                        break;
                    case PUT:
                        contactBook.copy(newContactBook);
                        this.contactBookService.save(contactBook);
                        result = contactBook;
                        break;
                    case DELETE:
                        this.contactBookService.delete(id);
                        result = contactBook;
                        break;
                    default:
                        log.warn("received unexpected request method: " + request.getMethod());
                        break;
                }
            } else {
                status = HttpStatus.NOT_FOUND;
            }
            return new ResponseEntity<>(result, status);
        } catch (Exception e) {
            log.error("failed to precess request ", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to different RequestMethods:
     * - GET: List contacts in contact book.
     * - POST: Create new contact in contact book.
     *
     * @param id       Contact book id.
     * @param name     Contact name.
     * @param lastName Contact last name.
     * @param email    Contact email.
     * @param address  Contact address.
     * @param phone    Contact phone.
     * @param contact  New contact data.
     * @param request  Request method type.
     * @return Processed contact.
     */
    @RequestMapping(value = "/{id}/contact", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> bookIdContact(@PathVariable(value = "id") Integer id,
                                                @RequestParam(name = "name", required = false) String name,
                                                @RequestParam(name = "lastName", required = false) String lastName,
                                                @RequestParam(name = "email", required = false) String email,
                                                @RequestParam(name = "address", required = false) String address,
                                                @RequestParam(name = "phone", required = false) String phone,
                                                @RequestBody(required = false) Contact contact,
                                                HttpServletRequest request) {
        try {
            Object result = null;
            HttpStatus status = HttpStatus.OK;
            ContactBook contactBook = this.contactBookService.findById(id);
            if (contactBook != null) {
                switch (RequestMethod.valueOf(request.getMethod())) {
                    case GET:
                        result = contactBook.getContacts(name, lastName, email, address, phone);
                        break;
                    case POST:
                        if (contact != null) {
                            contactBook.addContact(contact);
                            contact.setContactBook(contactBook);
                            this.contactService.save(contact);
                            result = contact;
                        }
                        break;
                    default:
                        log.warn("received unexpected request method: " + request.getMethod());
                        break;
                }
            } else {
                status = HttpStatus.NOT_FOUND;
            }
            return new ResponseEntity<>(result, status);
        } catch (Exception e) {
            log.error("failed to precess request ", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to different RequestMethods:
     * - GET: Find contact by id.
     * - PUT: Update contact data.
     * - DELETE: Delete contact by id.
     *
     * @param bid        Contact book id.
     * @param cid        Contact id.
     * @param newContact New contact data.
     * @param request    RequestMethod type.
     * @return Processed contact.
     */
    @RequestMapping(value = "/{id}/contact/{cid}", method = {RequestMethod.GET,RequestMethod.PUT, RequestMethod.DELETE}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> updateContact(@PathVariable(value = "id") Integer bid,
                                                @PathVariable(value = "cid") Integer cid,
                                                @RequestBody(required = false) Contact newContact,
                                                HttpServletRequest request) {
        try {
            Object result = null;
            HttpStatus status = HttpStatus.OK;
            ContactBook contactBook = this.contactBookService.findById(bid);
            Contact contact = this.contactService.findContactById(cid);
            if (contactBook != null && contact != null) {
                switch (RequestMethod.valueOf(request.getMethod())) {
                    case GET:
                        result = contact;
                        break;
                    case PUT:
                        contact.copy(newContact);
                        this.contactService.save(contact);
                        result = contact;
                        break;
                    case DELETE:
                        this.contactService.delete(contact);
                        result = contact;
                        break;
                    default:
                        log.warn("received unexpected request method: " + request.getMethod());
                        break;
                }
            } else {
                status = HttpStatus.NOT_FOUND;
            }
            return new ResponseEntity<>(result, status);
        } catch (Exception e) {
            log.error("failed to precess request ", e);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
