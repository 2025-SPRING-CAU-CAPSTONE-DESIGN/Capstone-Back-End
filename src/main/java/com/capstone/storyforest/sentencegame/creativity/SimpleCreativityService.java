package com.capstone.storyforest.sentencegame.creativity;

import org.springframework.stereotype.Component;

@Component
public class SimpleCreativityService implements CreativityService {

    public boolean isCreative(String s){ return s.split("\\s+").length >= 15; }
}
