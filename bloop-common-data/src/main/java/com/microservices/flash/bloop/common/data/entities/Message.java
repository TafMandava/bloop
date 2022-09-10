package com.microservices.flash.bloop.common.data.entities;

import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Message extends BaseEntity {

    @Column(length = 128, nullable = false)
    private String text;

    private boolean isDeleted;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_fk")
    private Member member;
    
    @Builder
    public Message(UUID id, Long version, Timestamp createdDate, Timestamp lastModifiedDate, String text,
            boolean isDeleted, Member member) {

        super(id, version, createdDate, lastModifiedDate);

        this.text = text;
        this.isDeleted = isDeleted;
        this.member = member;
    }

    
}
