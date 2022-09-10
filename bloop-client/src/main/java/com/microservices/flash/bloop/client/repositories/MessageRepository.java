package com.microservices.flash.bloop.client.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.microservices.flash.bloop.common.data.entities.Member;
import com.microservices.flash.bloop.common.data.entities.Message;

public interface MessageRepository extends JpaRepository<Message, UUID> {

    Long countById(UUID id);

    Page<Message> findByMemberOrderByCreatedDateAsc(Member member, Pageable pageable);

}
