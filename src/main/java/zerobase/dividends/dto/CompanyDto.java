package zerobase.dividends.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CompanyDto {

    private String ticker;
    private String name;
}
