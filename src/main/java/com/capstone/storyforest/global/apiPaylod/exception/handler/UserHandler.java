package com.capstone.storyforest.global.apiPaylod.exception.handler;

import com.capstone.storyforest.global.apiPaylod.code.BaseErrorCode;
import com.capstone.storyforest.global.apiPaylod.exception.GeneralException;

public class UserHandler extends GeneralException {
    public UserHandler(BaseErrorCode baseErrorCode){
        super(baseErrorCode);
    }
}
