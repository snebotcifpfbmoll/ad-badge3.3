package com.snebot.fbmoll.service;

import com.snebot.fbmoll.data.Contact;
import com.snebot.fbmoll.data.ContactRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ContactService {
    private final ContactRepository contactRepository;

    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    /**
     * Find contact by id.
     *
     * @param id Contact id.
     * @return Contact with matching id.
     */
    public Contact findContactById(Integer id) {
        Contact result = null;
        Optional<Contact> contact = this.contactRepository.findById(id);
        if (contact.isPresent()) result = contact.get();
        return result;
    }

    /**
     * Save Contact to database.
     *
     * @param contact Contact to save.
     */
    public void save(Contact contact) {
        this.contactRepository.save(contact);
    }

    /**
     * Delete contact.
     *
     * @param contact Contact to delete.
     */
    public void delete(Contact contact) {
        this.contactRepository.delete(contact);
    }

    /**
     * Delete contact by id.
     *
     * @param id Contact id.
     */
    public void delete(Integer id) {
        this.contactRepository.deleteById(id);
    }

    /**
     * Delete all Contacts.
     */
    public void deleteAll() {
        this.contactRepository.deleteAll();
    }
}
