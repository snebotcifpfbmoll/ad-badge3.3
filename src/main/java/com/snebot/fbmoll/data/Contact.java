package com.snebot.fbmoll.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.snebot.fbmoll.helper.ContactValidator;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;

@Entity
@Table(name = "contact")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private ContactBook contactBook;
    private String name;
    private String lastName;
    private String email;
    private String address;
    private String phone;

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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (ContactValidator.validateEmail(email)) this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        if (ContactValidator.validatePhone(phone)) this.phone = phone;
    }

    public ContactBook getContactBook() {
        return contactBook;
    }

    public void setContactBook(ContactBook contactBook) {
        this.contactBook = contactBook;
    }

    public Contact() {}

    public Contact(ContactBook contactBook, String name, String lastName, String email, String address, String phone) {
        this.contactBook = contactBook;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.phone = phone;
    }

    /**
     * Copy contact data.
     *
     * @param contact Contact data to copy.
     */
    public void copy(Contact contact) {
        if (contact == null) return;
        if (contact.getName() != null) setName(contact.getName());
        if (contact.getLastName() != null) setLastName(contact.getLastName());
        if (contact.getEmail() != null) setEmail(contact.getEmail());
        if (contact.getAddress() != null) setAddress(contact.getAddress());
        if (contact.getPhone() != null) setPhone(contact.getPhone());
    }

    /**
     * Checks if contacts have same data.
     *
     * @param contact Contact data.
     * @return True if similar, false otherwise.
     */
    public boolean similar(Contact contact) {
        return StringUtils.equals(contact.getName(), this.name) &&
                StringUtils.equals(contact.getLastName(), this.lastName) &&
                StringUtils.equals(contact.getEmail(), this.email) &&
                StringUtils.equals(contact.getAddress(), this.address) &&
                StringUtils.equals(contact.getPhone(), this.phone);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Contact)) return false;
        return id != null && id.equals(((Contact) obj).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
