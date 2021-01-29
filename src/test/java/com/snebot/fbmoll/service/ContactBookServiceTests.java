package com.snebot.fbmoll.service;

import com.snebot.fbmoll.data.ContactBook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.List;

@SpringBootTest
public class ContactBookServiceTests {
    @Autowired
    private ContactBookService contactBookService;

    @Test
    void tryContactBookService() {
        this.contactBookService.deleteAll();
        ContactBook book1 = new ContactBook("book1");
        this.contactBookService.save(book1);
        ContactBook result1 = this.contactBookService.findById(book1.getId());
        Assert.isTrue(book1.equals(result1), "failed to find Contact Book by id.");

        ContactBook book2 = new ContactBook("book2");
        this.contactBookService.save(book2);
        List<ContactBook> result2 = this.contactBookService.findByName(book2.getName());
        Assert.isTrue(result2.size() == 1, "failed to find Contact Book by name.");
        Assert.isTrue(book2.equals(result2.get(0)), "failed to find Contact Book by name.");

        List<ContactBook> all = this.contactBookService.findAll();
        Assert.isTrue(all.size() == 2, "failed to find all Contact Books.");

        this.contactBookService.delete(book1);
        this.contactBookService.delete(book2.getId());
        all = this.contactBookService.findAll();
        Assert.isTrue(all.size() == 0, "failed to delete Contact Book.");
    }
}
