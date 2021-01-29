package com.snebot.fbmoll.service;

import com.snebot.fbmoll.data.Contact;
import com.snebot.fbmoll.data.ContactBook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

@SpringBootTest
public class ContactServiceTests {
    @Autowired
    private ContactBookService contactBookService;
    @Autowired
    private ContactService contactService;

    @Test
    void tryContactService() {
        this.contactService.deleteAll();
        this.contactBookService.deleteAll();

        ContactBook book = new ContactBook("test book");
        this.contactBookService.save(book);

        Contact contact1 = new Contact(book, "name1", "lastName1", "email1@example.com", "address1", "111111111");
        this.contactService.save(contact1);
        Contact contact2 = new Contact(book, "name2", "lastName2", "email2@example.com", "address2", "222222222");
        this.contactService.save(contact2);

        Contact result1 = this.contactService.findContactById(contact1.getId());
        Assert.isTrue(contact1.equals(result1), "failed to find contact 1 by id.");
        Contact result2 = this.contactService.findContactById(contact2.getId());
        Assert.isTrue(contact2.equals(result2), "failed to find contact 2 by id.");

        this.contactService.delete(contact1);
        this.contactService.delete(contact2.getId());
        result1 = this.contactService.findContactById(contact1.getId());
        Assert.isTrue(result1 == null, "failed to delete contact.");
        result2 = this.contactService.findContactById(contact2.getId());
        Assert.isTrue(result2 == null, "failed to delete contact by id.");
    }
}
