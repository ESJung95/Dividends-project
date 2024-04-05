package zerobase.dividends.exception.impl;

import org.springframework.http.HttpStatus;
import zerobase.dividends.exception.AbstractException;

public class NoExistEumMonthException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "Unexpected Month enum value";
    }
}
