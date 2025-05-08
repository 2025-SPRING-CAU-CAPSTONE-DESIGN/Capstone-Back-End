package com.capstone.storyforest.sentencegame.filtering.dto;

import java.util.ArrayList;
import java.util.List;

public class OpenAiRequest {
    private String model;
    private List<OpenAiMessage> messages;

    public OpenAiRequest() {
        // Jackson이 역직렬화할 때 필요
    }

    public OpenAiRequest(String model, String target) {
        this.model = model;
        this.messages = new ArrayList<>();
        this.messages.add(new OpenAiMessage("user", target));
    }

    public String getModel() {
        return model;
    }

    public List<OpenAiMessage> getMessages() {
        return messages;
    }
}
