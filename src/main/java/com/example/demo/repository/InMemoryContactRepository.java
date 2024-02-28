package com.example.demo.repository;

import com.example.demo.Contact;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class InMemoryContactRepository implements ContactRepository {

    private final List<Contact> contacts = new ArrayList<>();

    @Override
    public List<Contact> findAll() {
        log.debug("Call findAll in InMemoryContactRepository");
        return contacts;
    }

    @Override
    public Optional<Contact> findById(Long id) {
        log.debug("Call findById in InMemoryContactRepository");
        return contacts.stream().filter(contact -> contact.getId()
                .equals(id)).findFirst();
    }

    @Override
    public Contact save(Contact contact) {
        log.debug("Call save in InMemoryContactRepository");
        contact.setId(System.currentTimeMillis());
        contacts.add(contact);
        return contact;
    }

    @Override
    public Contact update(Contact contact) {
        log.debug("Call update in InMemoryContactRepository");

        Contact existedContact = findById(contact.getId()).orElse(null);
        if (existedContact != null){
            existedContact.setFirstName(contact.getFirstName());
            existedContact.setLastName(contact.getLastName());
            existedContact.setEmail(contact.getEmail());
            existedContact.setPhoneNumber(contact.getPhoneNumber());
        }

        return existedContact;
    }

    @Override
    public void deleteById(Long id) {
        log.debug("Call deleteById in InMemoryContactRepository");

        findById(id).ifPresent(contacts::remove);
    }

    @Override
    public void batchInsert(List<Contact> contacts) {
        this.contacts.addAll(contacts);
    }
}
