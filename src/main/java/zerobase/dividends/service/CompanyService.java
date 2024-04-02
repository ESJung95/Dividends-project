package zerobase.dividends.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import zerobase.dividends.domain.Company;
import zerobase.dividends.domain.Dividend;
import zerobase.dividends.dto.CompanyDto;
import zerobase.dividends.dto.ScrapedResultDto;
import zerobase.dividends.repository.CompanyRepository;
import zerobase.dividends.repository.DividendRepository;
import zerobase.dividends.scraper.Scraper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CompanyService {

    private final Scraper yahooFinanceScraper;

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    public CompanyDto save(String ticker) {
        boolean exists = this.companyRepository.existsByTicker(ticker);
        if (exists) {
            throw new RuntimeException("already exitsts ticker -> " + ticker);
        }
        return this.storeCompanyAndDividend(ticker);
    }

    private CompanyDto storeCompanyAndDividend(String ticker) {
        // ticker 를 기준으로 회사를 스크래핑
        CompanyDto companyDto = this.yahooFinanceScraper.scrapCompanyByTicker(ticker);
        if (ObjectUtils.isEmpty(companyDto)) {
            throw new RuntimeException("failed to scrap ticker ->" + ticker);
        }

        // 해당 회사가 존재할 경우, 회사의 배당금 정보를 스크래핑
        ScrapedResultDto scrapedResultDto = this.yahooFinanceScraper.scrap(companyDto);

        // 스크래핑 결과
        Company company = this.companyRepository.save(new Company(companyDto));
        List<Dividend> dividendList = scrapedResultDto.getDividends().stream()
                                                .map(e -> new Dividend(company.getId(), e))
                                                .collect(Collectors.toList());
        this.dividendRepository.saveAll(dividendList);
        return companyDto;
    }
}
