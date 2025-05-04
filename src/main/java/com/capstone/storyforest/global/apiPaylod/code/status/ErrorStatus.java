package com.capstone.storyforest.global.apiPaylod.code.status;

import com.capstone.storyforest.global.apiPaylod.code.BaseErrorCode;
import com.capstone.storyforest.global.apiPaylod.code.ErrorReasonDTO;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorStatus implements BaseErrorCode {

    /* ===== 공통 ===== */
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST           (HttpStatus.BAD_REQUEST,         "COMMON400", "잘못된 요청입니다."),
    _UNAUTHORIZED          (HttpStatus.UNAUTHORIZED,        "COMMON401", "인증이 필요합니다."),
    _FORBIDDEN             (HttpStatus.FORBIDDEN,           "COMMON403", "금지된 요청입니다."),

    /* ===== 사용자(User) 영역 ===== */
    USER_NOT_FOUND         ("USER4001", "사용자가 없습니다."),
    USERNAME_NOT_EXIST     ("USER4002", "닉네임은 필수입니다."),
    PASSWORDS_NOT_MATCH    ("USER4003", "비밀번호와 비밀번호 확인이 일치하지 않습니다."),
    USERNAME_ALREADY_EXISTS("USER4004", "이미 존재하는 닉네임입니다."),
    USER_WRONG_PASSWORD    ("USER4005", "잘못된 비밀번호입니다."),

    /* ===== 단어(Word-Game) 영역 ===== */
    INVALID_LEVEL   ("WORD4001", "level 값은 1~3이어야 합니다."),
    NOT_ENOUGH_WORDS("WORD4002", "요청한 난이도의 단어가 충분하지 않습니다."),

    /* ===== 리더보드 영역 ===== */
    INVALID_PAGE ("LEAD4001", "page 값은 0 이상이어야 합니다."),
    INVALID_SIZE ("LEAD4002", "size 값은 1~50 사이여야 합니다."),

    /* ===== 문장게임 영역 ===== */
    INVALID_LEVEL_SENTENCE   ("SENT4001", "level 값은 1~3이어야 합니다."),
    NOT_ENOUGH_WORDS_SENTENCE("SENT4002", "요청 레벨의 단어가 부족합니다."),
    PROFANITY_DETECTED       ("SENT4003", "문장에 비속어가 포함되어 있습니다."),

    /* ===== 대외(외부 API) ===== */
    OPENAI_CALL_FAILED ("OPENAI5001", "OpenAI Moderation API 호출에 실패했습니다."),


    /* ===== 검증/리소스 ===== */
    VALIDATION_ERROR ("COMMON400A", "유효성 검증 실패"),
    NOT_FOUND        (HttpStatus.NOT_FOUND, "COMMON404", "리소스를 찾을 수 없습니다.")
    ;

    /* ---------- 필드 ---------- */
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    /* ---------- 생성자 ---------- */
    // 3-파라미터 : HttpStatus 직접 지정
    ErrorStatus(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code       = code;
        this.message    = message;
    }

    // 2-파라미터 : BAD_REQUEST 기본
    ErrorStatus(String code, String message) {
        this.httpStatus = HttpStatus.BAD_REQUEST;
        this.code       = code;
        this.message    = message;
    }

    /* ---------- 인터페이스 구현 ---------- */
    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .isSuccess(false)
                .code(code)
                .message(message)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .isSuccess(false)
                .code(code)
                .message(message)
                .httpStatus(httpStatus)
                .build();
    }
}
