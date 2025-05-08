package com.capstone.storyforest.user.dto;

import lombok.Getter;

@Getter
public class GetTierResponseDTO {
    private int tier;
    private int progressPercent;
    private int storiesToNextTier;

    public GetTierResponseDTO(int tier, int progressPercent, int storiesToNextTier) {
        this.tier = tier;
        this.progressPercent = progressPercent;
        this.storiesToNextTier = storiesToNextTier;
    }
}

