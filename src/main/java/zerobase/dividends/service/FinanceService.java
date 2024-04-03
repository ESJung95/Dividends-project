package zerobase.dividends.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import zerobase.dividends.domain.Company;
import zerobase.dividends.domain.Dividend;
import zerobase.dividends.dto.CompanyDto;
import zerobase.dividends.dto.DividendDto;
import zerobase.dividends.dto.ScrapedResultDto;
import zerobase.dividends.repository.CompanyRepository;
import zerobase.dividends.repository.DividendRepository;
import zerobase.dividends.type.CacheKey;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class FinanceService {

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    @Cacheable(key = "#companyName", value = CacheKey.KEY_FINANCE)
    public ScrapedResultDto getDividendByCompanyName(String companyName) {
        log.info("search company -> " + companyName);
        // 1. 회사명을 기준으로 회사 정보를 조회
        Company company = this.companyRepository.findByName(companyName)
                                        .orElseThrow(() -> new RuntimeException("존재하지 않는 회사명 입니다."));

        // 2. 조회된 회사의 ID 로 배당금 정보 조회
        List<Dividend> dividends = this.dividendRepository.findAllByCompanyId(company.getId());

        // 3. 결과 조합 후 반환
        List<DividendDto> dividendDtos = dividends.stream()
                                                .map(e -> new DividendDto(e.getDate(), e.getDividend()))
                                                .collect(Collectors.toList());

        return new ScrapedResultDto(new CompanyDto(company.getTicker(), company.getName()),
                                    dividendDtos);
    }
}
