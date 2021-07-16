package com.github.admarc.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.UUID;


@Entity
@Table(name = "tournament")
public class Tournament {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "name")
    @NotBlank
    private String name;

    @Column(name = "start_at")
    @Temporal(TemporalType.TIMESTAMP)
    @NotBlank
    private Date startAt;

    @Column(name = "end_at")
    @Temporal(TemporalType.TIMESTAMP)
    @NotBlank
    private Date endAt;

    @CreatedDate
    @Column(name = "created_at")
    private Date createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private Date updatedAt;

    public Tournament() {}

    public Tournament(
            String name,
            Date startAt,
            Date endAt
    ) {
        this.name = name;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public UUID getId() {
        return id;
    }

}

