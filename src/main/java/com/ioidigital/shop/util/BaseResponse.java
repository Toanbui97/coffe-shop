package com.ioidigital.shop.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldNameConstants
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseResponse {

    private HttpStatus httpStatus;
    private Integer resultCode;
    private String resultMsg;
    private Long resourceId;
    @Builder.Default
    private String responseTimestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .withZone(ZoneOffset.UTC)
            .format(Instant.now());

    public BaseResponse(HttpStatus httpStatus,
                        Integer resultCode,
                        String resultMsg,
                        Long resourceId) {
        this.httpStatus = httpStatus;
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
        this.resourceId = resourceId;
        this.responseTimestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .withZone(ZoneOffset.UTC)
                .format(Instant.now());
    }
}
