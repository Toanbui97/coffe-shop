package com.ioidigital.shop.util;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
public class BaseDataResponse<D> extends BaseResponse {

    private D data;

    @Builder(builderMethodName = "build")
    public BaseDataResponse(final HttpStatus httpStatus,
                            final Integer resultCode,
                            final String resultMsg,
                            final Long resourceId,
                            final D data) {
        super(httpStatus, resultCode, resultMsg, resourceId);
        this.data = data;
    }
}
