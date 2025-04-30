package com.capstone.storyforest.global.apiPaylod.exception;

import com.capstone.storyforest.global.apiPaylod.code.BaseErrorCode;
import com.capstone.storyforest.global.apiPaylod.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {

    private BaseErrorCode code;

    public ErrorReasonDTO getErrorReason() {
        return this.code.getReason();
    }

    public ErrorReasonDTO getErrorReasonHttpStatus(){
        return this.code.getReasonHttpStatus();
    }
}
