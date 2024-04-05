package zerobase.dividends.exception.impl;

import org.springframework.http.HttpStatus;
import zerobase.dividends.exception.AbstractException;

public class FailedSaveResultDtoException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    @Override
    public String getMessage() {
        return "배당금 정보 + 회사 정보 통합 저장에 실패했습니다.";
    }
}
