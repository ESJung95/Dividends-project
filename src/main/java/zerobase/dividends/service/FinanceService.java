package zerobase.dividends.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import zerobase.dividends.domain.Company;
import zerobase.dividends.domain.Dividend;
import zerobase.dividends.dto.CompanyDto;
import zerobase.dividends.dto.DividendDto;
import zerobase.dividends.dto.ScrapedResultDto;
import zerobase.dividends.repository.CompanyRepository;
import zerobase.dividends.repository.DividendRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class FinanceService {

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    public ScrapedResultDto getDividendByCompanyName(String companyName) {

        // 1. 회사명을 기준으로 회사 정보를 조회
        Company company = this.companyRepository.findByName(companyName)
                                        .orElseThrow(() -> new RuntimeException("존재하지 않는 회사명 입니다."));

        // 2. 조회된 회사의 ID 로 배당금 정보 조회
        List<Dividend> dividends = this.dividendRepository.findAllByCompanyId(company.getId());

        // 3. 결과 조합 후 반환
        List<DividendDto> dividendDtos = new ArrayList<>();
        for (var entity : dividends) {
            dividendDtos.add(DividendDto.builder()
                    .date(entity.getDate())
                    .dividend(entity.getDividend())
                    .build());
        }

        return new ScrapedResultDto(CompanyDto.builder()
                .ticker(company.getTicker())
                .name(company.getName())
                .build(),

                dividendDtos);
    }
}
