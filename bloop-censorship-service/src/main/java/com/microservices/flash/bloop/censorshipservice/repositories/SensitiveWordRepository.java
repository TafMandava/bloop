package com.microservices.flash.bloop.censorshipservice.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservices.flash.bloop.censorshipservice.domain.SensitiveWord;

public interface SensitiveWordRepository extends JpaRepository<SensitiveWord, UUID> {

    List<SensitiveWord> findByTextIgnoreCaseContaining(String text);
    
}
