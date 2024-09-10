package carrot.market.common.baseutil;

import lombok.Getter;

@Getter
public enum BaseResponseStatus {
    SUCCESS("SU", "Success"),
    VALIDATION_FAILED("VF", "Validation failed"),
    DUPLICATE_EMAIL("DE", "Duplicate email"),
    DUPLICATE_NICKNAME("DN", "Duplicate nickname"),
    DUPLICATE_TEL_NUMBER("DT", "Duplicate tel number"),
    NOT_EXISTED_USER("NU", "This user does not exist"),
    NOT_EXISTED_BOARD("NB", "This board does not exist"),
    NOT_EXISTED_POST("NP", "This post does not exist"),
    NOT_EXISTED_AREA("NP", "This area does not exist"),
    NOT_EXISTED_CATEGORY("NC", "This category does not exist"),
    NOT_MATCHED_PASSWORD("NM", "Not Matched Password"),
    SIGN_IN_FAIL("SF", "Login information mismatch"),
    AUTHORIZATION_FAIL("AF", "Authorization Failed"),
    NO_PERMISSION("NP", "Do not have permission"),
    DATABASE_ERROR("DBE", "Database error"),
    TOKEN_MISMATCHED("RM", "RefreshToken mismatched"),
    INVALID_TOKEN("IT", "Invalid JWT Token"),
    EXPIRED_TOKEN("ET", "Expired JWT Token"),
    UNSUPPORTED_TOKEN("UT", "Unsupported JWT Token"),
    TOKEN_ISEMPTY("TE", "JWT claims string is empty"),
    TOKEN_PARSE_ERROR("TP", "Unexpected JWT parsing error"),
    UNKNOWN_ERROR("UE", "Unknown error"),
    ALREADY_LIKED("AL", "Already liked"),
    AREA_NOT_DEFINED("AD", "AreaInfo Not Defined"),
    UNSUPPORT_FILETYPE("UF", "Unsupported File Type!");

    private final String code;
    private final String message;

    private BaseResponseStatus(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
