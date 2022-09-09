package com.microservices.flash.bloop.client.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.microservices.flash.bloop.common.data.entities.Member;
import com.microservices.flash.bloop.common.data.enums.AuthenticationType;

public interface MemberRepository extends JpaRepository<Member, UUID> {

    Member findByEmail(String email);

    Member findByVerificationCode(String verificationCode);

    @Query("UPDATE Member AS c SET c.enabled = true, c.verificationCode = null WHERE c.id = :id")
    @Modifying
    void enable(@Param("id") UUID id);

    @Query("UPDATE Member AS c SET c.authenticationType = :authenticationType WHERE c.id = :id")
    @Modifying
    public void updateAuthenticationType(AuthenticationType authenticationType, UUID id);

    Member findByResetPasswordToken(String resetPasswordToken);

}
