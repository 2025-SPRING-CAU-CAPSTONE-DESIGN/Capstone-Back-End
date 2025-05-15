package com.capstone.storyforest.user.dto;

import lombok.Getter;

@Getter
public class GetTierResponseDTO {
    private int tier;
    private int progressPercent;
    private int storiesToNextTier;
    private int totalStory;

    public GetTierResponseDTO(int tier, int progressPercent, int storiesToNextTier, int totalStory) {
        this.tier = tier;
        this.progressPercent = progressPercent;
        this.storiesToNextTier = storiesToNextTier;
        this.totalStory = totalStory;
    }
}

