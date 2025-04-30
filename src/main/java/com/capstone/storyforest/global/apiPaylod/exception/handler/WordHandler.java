package com.capstone.storyforest.global.apiPaylod.exception.handler;

import com.capstone.storyforest.global.apiPaylod.code.BaseErrorCode;
import com.capstone.storyforest.global.apiPaylod.exception.GeneralException;

public class WordHandler extends GeneralException {
    public WordHandler(BaseErrorCode code) {
        super(code);
    }
}
