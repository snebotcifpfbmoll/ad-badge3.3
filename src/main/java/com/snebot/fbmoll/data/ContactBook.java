package com.snebot.fbmoll.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "contact_book")
public class ContactBook {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String name;
    @JsonIgnore
    @OneToMany(targetEntity = Contact.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "contact_book_id")
    private final List<Contact> contacts = new ArrayList<>();

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

    public List<Contact> getContacts() {
        return contacts;
    }

    public ContactBook() {
    }

    public ContactBook(String name) {
        this.name = name;
    }

    /**
     * Add contact to contact list.
     *
     * @param contact Contact to add.
     */
    public void addContact(Contact contact) {
        this.contacts.add(contact);
        contact.setContactBook(this);
    }

    /**
     * Remove contact from contact list.
     *
     * @param contact Contact to remove.
     */
    public void removeContact(Contact contact) {
        this.contacts.remove(contact);
        contact.setContactBook(null);
    }

    /**
     * Search contact by attributes.
     *
     * @param name Contact name.
     * @param lastName Contact last name.
     * @param email Contact email.
     * @param address Contact address.
     * @param phone Contact phone.
     * @return List of matching contacts.
     */
    public List<Contact> getContacts(String name, String lastName, String email, String address, String phone) {
        List<Contact> contactList = new ArrayList<>();
        for (Contact c : this.contacts) {
            if ((name == null || StringUtils.equals(c.getName(), name)) &&
                    (lastName == null || StringUtils.equals(c.getLastName(), lastName)) &&
                    (email == null || StringUtils.equals(c.getEmail(), email)) &&
                    (address == null || StringUtils.equals(c.getAddress(), address)) &&
                    (phone == null || StringUtils.equals(c.getPhone(), phone))) contactList.add(c);
        }
        return contactList;
    }

    /**
     * Copy contact book data.
     *
     * @param contactBook Contact book data to copy.
     */
    public void copy(ContactBook contactBook) {
        if (contactBook == null) return;
        if (contactBook.getName() != null) setName(contactBook.getName());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ContactBook)) return false;
        ContactBook contactBook = (ContactBook) obj;
        return contactBook.getName().equals(this.name);
    }
}
