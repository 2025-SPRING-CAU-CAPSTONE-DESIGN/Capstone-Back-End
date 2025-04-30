package com.capstone.storyforest.global.apiPaylod.exception.handler;

import com.capstone.storyforest.global.apiPaylod.code.BaseErrorCode;
import com.capstone.storyforest.global.apiPaylod.exception.GeneralException;

public class LeaderboardHandler extends GeneralException {
    public LeaderboardHandler(BaseErrorCode code) {
        super(code);
    }
}
