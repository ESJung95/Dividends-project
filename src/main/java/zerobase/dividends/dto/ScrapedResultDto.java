package zerobase.dividends.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class ScrapedResultDto {

    private CompanyDto companyDto;

    private List<DividendDto> Dividends;

    public ScrapedResultDto() {
        this.Dividends = new ArrayList<>();
    }
}
