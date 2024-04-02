package zerobase.dividends.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class DividendDto {

    private LocalDateTime date;
    private String dividend;
}
