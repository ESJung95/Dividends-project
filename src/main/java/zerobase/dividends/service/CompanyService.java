package zerobase.dividends.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.Trie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
@Slf4j
public class CompanyService {

    private final Trie trie;
    private final Scraper yahooFinanceScraper;

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    public CompanyDto save(String ticker) {
        boolean exists = this.companyRepository.existsByTicker(ticker);
        if (exists) {
            throw new RuntimeException("already exits ticker -> " + ticker);
        }
        return this.storeCompanyAndDividend(ticker);
    }

    public Page<Company> getAllCompany(final Pageable pageable) {
        log.info("모든 정보 조회 중");
        return this.companyRepository.findAll(pageable);
    }

    private CompanyDto storeCompanyAndDividend(String ticker) {
        log.info("회사 및 배당금 정보 저장을 시작합니다. ticker -> " + ticker);
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
        log.info("회사 및 배당금 정보 저장이 완료되었습니다.");
        return companyDto;
    }

    public List<String> getCompanyNameByKeyword(String keyword) {
        Pageable limit = PageRequest.of(0, 10);
        Page<Company> companies = this.companyRepository.findByNameStartingWithIgnoreCase(keyword, limit);
        return companies.stream()
                            .map(Company::getName)
                            .collect(Collectors.toList());
    }

    public void addAutocompleteKeyword(String keyword) {
        this.trie.put(keyword, null);
    }

//    public List<String> autocomplete(String keyword) {
//        return (List<String>) this.trie.prefixMap(keyword).keySet()
//                .stream()
//                .limit(10)
//                .collect(Collectors.toList());
//    }
    public void deleteAutocompleteKeyword(String keyword) {
        this.trie.remove(keyword);
    }

    public String deleteCompany(String ticker) {
        var company = this.companyRepository.findByTicker(ticker)
                               .orElseThrow(() -> new RuntimeException("존재하지 않는 회사입니다."));

        this.dividendRepository.deleteAllByCompanyId(company.getId());
        this.companyRepository.delete(company);

        this.deleteAutocompleteKeyword(company.getName());
        return company.getName();
    }
}
