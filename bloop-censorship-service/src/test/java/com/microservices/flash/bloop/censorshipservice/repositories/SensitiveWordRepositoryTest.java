package com.microservices.flash.bloop.censorshipservice.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.microservices.flash.bloop.censorshipservice.domain.SensitiveWord;
import com.microservices.flash.bloop.censorshipservice.utils.SensitiveWordsList;

/**
 * DataJpaTest
 *     Leverage Spring Data JPA Tests
 * AutoConfigureTestDatabase
 *     Run test against the real database instead of an inMemory database 
 * Rollback
 *     Tell SpringJPATest not to commit data after running the test
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(true)
public class SensitiveWordRepositoryTest {

    @Autowired
    private SensitiveWordRepository sensitiveWordRepository;

    @Test
    void testFindByTextIgnoreCaseContaining() {

        String sensitiveWord = "SELECT * FROM";

        List<SensitiveWord> dbSensitiveWords = sensitiveWordRepository.findByTextIgnoreCaseContaining(sensitiveWord);

        assertThat(dbSensitiveWords.size()).isGreaterThan(0);    

    }

    @Test
    void testCreateSensitiveWords() {

        List<SensitiveWord> sensitiveWords = SensitiveWordsList.sensitiveWords
                                                .stream()             
                                                .map(word -> SensitiveWord.builder().text(word).build())
                                                .collect(Collectors.toList());
                                                
        Iterable<SensitiveWord> savedSensitiveWords = sensitiveWordRepository.saveAll(sensitiveWords);

        assertThat(savedSensitiveWords).size().isEqualTo(sensitiveWords.size());

    }    

}
