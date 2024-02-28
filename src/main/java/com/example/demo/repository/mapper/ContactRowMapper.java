package com.example.demo.repository.mapper;

import com.example.demo.Contact;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ContactRowMapper implements RowMapper<Contact> {
    @Override
    public Contact mapRow(ResultSet rs, int rowNum) throws SQLException {
        Contact contact = new Contact();

        contact.setId(rs.getLong(Contact.Fields.id));
        contact.setFirstName(rs.getString(Contact.Fields.firstName));
        contact.setLastName(rs.getString(Contact.Fields.lastName));
        contact.setPhoneNumber(rs.getString(Contact.Fields.phoneNumber));
        contact.setEmail(rs.getString(Contact.Fields.email));

        return contact;
    }
}
