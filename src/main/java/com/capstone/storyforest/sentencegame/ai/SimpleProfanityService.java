package com.capstone.storyforest.sentencegame.ai;

import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class SimpleProfanityService implements ProfanityService{
    private static final Set<String> BAD = Set.of("foo","bar");
    public boolean hasProfanity(String text){
        String lo = text.toLowerCase();
        return BAD.stream().anyMatch(lo::contains);
    }
}
