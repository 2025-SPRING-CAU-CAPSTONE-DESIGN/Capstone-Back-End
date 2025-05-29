package com.capstone.storyforest.global.apiPaylod.exception.handler;

import com.capstone.storyforest.global.apiPaylod.code.BaseErrorCode;
import com.capstone.storyforest.global.apiPaylod.exception.GeneralException;

public class FriendHandler extends GeneralException {
    public FriendHandler(BaseErrorCode code) {
        super(code);
    }
}

