package com.microservices.flash.bloop.common.data.entities;

import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.microservices.flash.bloop.common.data.enums.AuthenticationType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(callSuper=true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Member extends BaseEntity {

    @Column(length = 128, nullable = false, unique = true)
    private String email;

    @Column(length = 64, nullable = false)
    private String password;    

    @Column(length = 45, nullable = false)
    private String firstName;

    @Column(length = 45, nullable = false)
    private String lastName;

    @Column(length = 15, nullable = false)
    private String phoneNumber;
    
    @Column(length = 64)
    private String verificationCode;
    
    private boolean enabled;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private AuthenticationType authenticationType;

    @Column(length = 30)
    private String resetPasswordToken;

    @Builder
    public Member(UUID id, Long version, Timestamp createdDate, Timestamp lastModifiedDate, String email,
            String password, String firstName, String lastName, String phoneNumber, String verificationCode, boolean enabled,
            AuthenticationType authenticationType, String resetPasswordToken) {

        super(id, version, createdDate, lastModifiedDate);

        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.verificationCode = verificationCode;
        this.enabled = enabled;
        this.authenticationType = authenticationType;
        this.resetPasswordToken = resetPasswordToken;
    }

    /**
     * Return a customer's full name 
     */
    public String getFullname() {
        return String.format("%s %s", getFirstName(), getLastName());
    }
    
}