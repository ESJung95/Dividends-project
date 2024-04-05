package zerobase.dividends.exception.impl;

import org.springframework.http.HttpStatus;
import zerobase.dividends.exception.AbstractException;

public class FailedSaveCompanyDtoException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    @Override
    public String getMessage() {
        return  "회사 정보 저장에 실패했습니다.";
    }
}
