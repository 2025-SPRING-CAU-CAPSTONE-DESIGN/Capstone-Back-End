package com.capstone.storyforest.global.apiPaylod.exception.handler;

import com.capstone.storyforest.global.apiPaylod.code.BaseErrorCode;
import com.capstone.storyforest.global.apiPaylod.exception.GeneralException;

public class SentenceHandler extends GeneralException {
    public SentenceHandler(BaseErrorCode code) {
        super(code);
    }
}
