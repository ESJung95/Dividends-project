package zerobase.dividends.exception.impl;

import org.springframework.http.HttpStatus;
import zerobase.dividends.exception.AbstractException;

public class FailedScrapingException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.INTERNAL_SERVER_ERROR.value();
    }

    @Override
    public String getMessage() {
        return "failed to scrap ticker";
    }
}
