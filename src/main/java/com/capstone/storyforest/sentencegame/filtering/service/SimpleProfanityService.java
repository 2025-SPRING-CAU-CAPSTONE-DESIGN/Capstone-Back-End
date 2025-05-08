package com.capstone.storyforest.sentencegame.filtering.service;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@ConditionalOnMissingBean(OpenAiProfanityService.class)
public class SimpleProfanityService implements ProfanityService {
    private static final Set<String> BAD = Set.of("foo","bar");
    public boolean hasProfanity(String text){
        String lo = text.toLowerCase();
        return BAD.stream().anyMatch(lo::contains);
    }
}
