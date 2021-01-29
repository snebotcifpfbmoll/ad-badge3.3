package com.snebot.fbmoll.data;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ContactBookRepository extends CrudRepository<ContactBook, Integer> {
    List<ContactBook> findAll();
    List<ContactBook> findByName(String name);
}
