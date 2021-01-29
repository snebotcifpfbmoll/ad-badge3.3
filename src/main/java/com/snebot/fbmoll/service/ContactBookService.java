package com.snebot.fbmoll.service;

import com.snebot.fbmoll.data.ContactBook;
import com.snebot.fbmoll.data.ContactBookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactBookService {
    private static final Logger log = LoggerFactory.getLogger(ContactBookService.class);
    private final ContactBookRepository contactBookRepository;

    public ContactBookService(ContactBookRepository contactBookRepository) {
        this.contactBookRepository = contactBookRepository;
    }

    /**
     * Find all Contact Books.
     *
     * @return Contact Book list.
     */
    public List<ContactBook> findAll() {
        return this.contactBookRepository.findAll();
    }

    /**
     * Find contact book by id.
     *
     * @param id Contact book id.
     * @return Contact book with matching id.
     */
    public ContactBook findById(Integer id) {
        ContactBook result = null;
        Optional<ContactBook> contactBook = this.contactBookRepository.findById(id);
        if (contactBook.isPresent()) result = contactBook.get();
        return result;
    }

    /**
     * Find all Contact Books with name.
     *
     * @param name Contact Book name.
     * @return Contact Book list with matching name.
     */
    public List<ContactBook> findByName(String name) {
        return this.contactBookRepository.findByName(name);
    }

    /**
     * Save Contact Book data to database.
     *
     * @param contactBook Contact Book data.
     */
    public void save(ContactBook contactBook) {
        this.contactBookRepository.save(contactBook);
    }

    /**
     * Delete Contact Book.
     *
     * @param contactBook Contact Book to delete.
     */
    public void delete(ContactBook contactBook) {
        this.contactBookRepository.delete(contactBook);
    }

    /**
     * Delete Contact Book by id.
     *
     * @param id Contact Book id.
     */
    public void delete(Integer id) {
        this.contactBookRepository.deleteById(id);
    }

    /**
     * Delete all Contact Books.
     */
    public void deleteAll() {
        this.contactBookRepository.deleteAll();
    }
}
