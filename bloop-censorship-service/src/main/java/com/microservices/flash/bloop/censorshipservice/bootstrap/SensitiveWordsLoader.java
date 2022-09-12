package com.microservices.flash.bloop.censorshipservice.bootstrap;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.microservices.flash.bloop.censorshipservice.domain.SensitiveWord;
import com.microservices.flash.bloop.censorshipservice.repositories.SensitiveWordRepository;
import com.microservices.flash.bloop.censorshipservice.utils.SensitiveWordsList;

/**
 *  Run everytime the Spring Context starts
 *      The class can run on startup
 *  "bootstrap" Booting up from a computer that comes from an old phrase of pull yourself up by your bootstraps 
 */
@Component
public class SensitiveWordsLoader implements CommandLineRunner {

    private final SensitiveWordRepository sensitiveWordRepository;

    public SensitiveWordsLoader(SensitiveWordRepository sensitiveWordRepository) {
        this.sensitiveWordRepository = sensitiveWordRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        loadSensitiveWordObjects();
    }

    private void loadSensitiveWordObjects() {
        if(sensitiveWordRepository.count() == 0) {

   
            List<SensitiveWord> sensitiveWords = SensitiveWordsList.sensitiveWords
                                                               .stream()             
                                                               .map(word -> SensitiveWord.builder().text(word).build())
                                                               .collect(Collectors.toList());
            sensitiveWordRepository.saveAll(sensitiveWords);

        }
    }     
    
}
