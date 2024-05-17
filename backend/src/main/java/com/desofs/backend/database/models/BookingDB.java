package com.desofs.backend.database.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity(name = "booking")
@Getter
public class BookingDB {

    @Id
    private String id;

    @Column(nullable = false)
    private String accountId;

    @Column(nullable = false)
    private String propertyId;

    @Column(nullable = false)
    private Date from;

    @Column(nullable = false)
    private Date to;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public BookingDB() {
    }

    public BookingDB(String id, String accountId, String propertyId,
                     Date from, Date to, LocalDateTime createdAt) {
        this.id = id;
        this.accountId = accountId;
        this.propertyId = propertyId;
        this.from = from;
        this.to = to;
        this.createdAt = createdAt;
    }
}
