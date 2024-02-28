package com.example.demo.repository;

import com.example.demo.Contact;
import com.example.demo.exception.ContactNotFoundException;
import com.example.demo.repository.mapper.ContactRowMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Primary
@Repository
@RequiredArgsConstructor
@Slf4j
public class DatabaseContactRepository implements ContactRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Contact> findAll() {
        log.debug("Calling DatabaseContactRepository->findAll");

        String sql = "SELECT * FROM contact";

        return jdbcTemplate.query(sql, new ContactRowMapper());
    }

    @Override
    public Optional<Contact> findById(Long id) {
        log.debug("Calling DatabaseContactRepository->findById, ID : {}", id);
        String sql = "SELECT * FROM contact WHERE id = ?";
        Contact contact = DataAccessUtils.singleResult(
                jdbcTemplate.query(sql,
                        new ArgumentPreparedStatementSetter(new Object[]{id}),
                        new RowMapperResultSetExtractor<>(new ContactRowMapper(), 1)
                )
        );
        return Optional.ofNullable(contact);
    }

    @Override
    public Contact save(Contact contact) {
        log.debug("Calling DatabaseContactRepository->save");
        contact.setId(System.currentTimeMillis());
        String sql = "INSERT INTO contact (id, first_name, last_name, email, phone_number) VALUES(?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, contact.getId(), contact.getFirstName(), contact.getLastName(), contact.getEmail(), contact.getPhoneNumber());
        return contact;
    }

    @Override
    public Contact update(Contact contact) {
        log.debug("Calling DatabaseContactRepository->update");
        Contact existedContact = findById(contact.getId()).orElse(null);
        if (existedContact != null) {
            String sql = "UPDATE contact SET first_name = ?, last_name = ?, email = ?, phone_number = ? WHERE id = ?";
            jdbcTemplate.update(sql, contact.getFirstName(), contact.getLastName(),
                    contact.getEmail(), contact.getPhoneNumber(), contact.getId());
            return contact;
        }

        log.warn("Contact with id {} not found!", contact.getId());

        throw new ContactNotFoundException("Contact for update not found! ID: " + contact.getId());
    }

    @Override
    public void deleteById(Long id) {
        log.debug("Calling DatabaseContactRepository->delete with ID: {}", id);

        String sql = "DELETE FROM contact WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void batchInsert(List<Contact> contacts) {
        log.debug("Calling DatabaseContactRepository->batchInsert");

        String sql = "INSERT INTO contact (id, first_name, last_name, email, phone_number) VALUES(?, ?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Contact contact = contacts.get(i);
                ps.setLong(1, contact.getId());
                ps.setString(2, contact.getFirstName());
                ps.setString(3, contact.getLastName());
                ps.setString(4, contact.getEmail());
                ps.setString(5, contact.getPhoneNumber());
            }

            @Override
            public int getBatchSize() {
                return contacts.size();
            }
        });
    }
}
