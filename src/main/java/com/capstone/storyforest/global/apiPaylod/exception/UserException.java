package com.capstone.storyforest.global.apiPaylod.exception;

import com.capstone.storyforest.global.apiPaylod.code.BaseErrorCode;

public class UserException extends GeneralException {
    public UserException(BaseErrorCode code) {
        super(code);
    }
}
