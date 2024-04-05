package zerobase.dividends.exception.impl;

import org.springframework.http.HttpStatus;
import zerobase.dividends.exception.AbstractException;

public class EmptyTickerException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "ticker 를 확인해주세요.";
    }
}
