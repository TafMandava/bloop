package com.microservices.flash.bloop.censorshipservice.services.impl;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.microservices.flash.bloop.censorshipservice.domain.SensitiveWord;
import com.microservices.flash.bloop.censorshipservice.repositories.SensitiveWordRepository;
import com.microservices.flash.bloop.censorshipservice.services.WordCensorshipService;
import com.microservices.flash.bloop.common.data.entities.Message;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class WordCensorshipServiceImpl implements WordCensorshipService {

    private final SensitiveWordRepository sensitiveWordRepository;

    @Override
    public Message censorSensitiveWords(Message message) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(message.getText());
        
        Map<String, Integer> sensitiveWordsSortedByLength = prioritizeCensorShipBySupersets(message);
        Set uniqueSensitiveWordsSortedByLength = sensitiveWordsSortedByLength.entrySet();
        Iterator iterator = uniqueSensitiveWordsSortedByLength.iterator();

        while (iterator.hasNext()) {
            Map.Entry mapEntry = (Map.Entry) iterator.next();
            System.out.println(String.format("%s : %s", mapEntry.getKey(), sensitiveWordsSortedByLength.get(mapEntry.getKey())));

            String regexText = mapEntry.getKey().toString();
            // Replacement pattern
            String replacementText = StringUtils.repeat("*", regexText.length());;
            // Step 1: Allocate a Pattern object to compile a regex
            Pattern pattern = Pattern.compile(Pattern.quote(regexText), Pattern.CASE_INSENSITIVE);
            // Step 2: Allocate a Matcher object from the pattern, and provide the input
            Matcher matcher = pattern.matcher(stringBuilder);
            
            if(matcher.find()) {
                stringBuilder.replace(0, stringBuilder.length(), matcher.replaceAll(replacementText));
                System.out.println(String.format("Replacement text '%s'", stringBuilder.toString()));
            }
        }

        message.setText(stringBuilder.toString()); 

        return message;
    }

    /**
     * 
     * Longer sensitive words can also be supersets. Therefore, prioritize replacement of supersets values
     *
     */
    private Map<String, Integer> prioritizeCensorShipBySupersets(Message message) {

        Map<String, Integer> matchedSensitiveWords = findAllSensitiveWords(message);

       return matchedSensitiveWords.entrySet()
                                    .stream()
                                    .sorted(Entry.comparingByValue( Comparator.reverseOrder() ))
                                    .collect(Collectors.toMap(Entry::getKey, Entry::getValue, (element1, element2) -> element1, LinkedHashMap::new));

    }

    private Map<String, Integer> findAllSensitiveWords(Message message) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(message.getText());

        Map<String, Integer>  matchedSensitiveWords = new HashMap<>();

        for(SensitiveWord sensitiveWord : sensitiveWordRepository.findAll()) {

            System.out.println(String.format("Stored Sensitive Word  '%s'", sensitiveWord.getText()));

            // Pattern to be matched
            String regexText = sensitiveWord.getText();

            // Step 1: Allocate a Pattern object to compile a regex. Pattern.quote is necessary to escape special chars that need to be treated as literal chars in the regex pattern.
            Pattern pattern = Pattern.compile(Pattern.quote(regexText), Pattern.CASE_INSENSITIVE);
            // Step 2: Allocate a Matcher object from the pattern, and provide the input
            Matcher matcher = pattern.matcher(stringBuilder);

            if(matcher.find()) {
                
                matchedSensitiveWords.put(regexText, regexText.length());

            }

        }

        return matchedSensitiveWords;
    }
    
}
