package zerobase.dividends.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import zerobase.dividends.domain.Company;
import zerobase.dividends.dto.CompanyDto;
import zerobase.dividends.exception.impl.EmptyTicker;
import zerobase.dividends.service.CompanyService;
import zerobase.dividends.type.CacheKey;

@RestController
@RequestMapping("/company")
@AllArgsConstructor
@Slf4j
public class CompanyController {

    private final CompanyService companyService;

    private final CacheManager redisCacheManager;

    // 검색 시 자동완성
    @GetMapping("/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam String keyword) {
        log.info("Autocomplete request for keyword -> " + keyword);
        var result = this.companyService.getCompanyNameByKeyword(keyword);
        return ResponseEntity.ok(result);
    }

    // 회사 리스트 조회
    @GetMapping
    @PreAuthorize("hasRole('READ')")
    public ResponseEntity<?> searchCompany(final Pageable pageable) {
        log.info("All company list check request");
        Page<Company> companies = this.companyService.getAllCompany(pageable);
        return ResponseEntity.ok(companies);
    }

    // 회사 및 배당금 정보 추가
    @PostMapping
    @PreAuthorize("hasRole('WRITE')")
    public ResponseEntity<?> addCompany(@RequestBody CompanyDto request) {
        log.info("Request add company");
        String ticker = request.getTicker().trim();
        if (ObjectUtils.isEmpty(ticker)) {
            throw new EmptyTicker();
        }

        CompanyDto companyDto = this.companyService.save(ticker);
        this.companyService.autocompleteKeyword(companyDto.getName());

        return ResponseEntity.ok(companyDto);
    }

    // 각 회사 배당금 데이터 삭제
    @DeleteMapping("/{ticker}")
    @PreAuthorize("hasRole('WRITE')")
    public ResponseEntity<?> deleteCompany(@PathVariable String ticker) {
        log.info("Request delete company");
        String companyName =  this.companyService.deleteCompany(ticker);
        this.clearFinanceCache(companyName);
        return ResponseEntity.ok(companyName);
    }

    // 데이터를 지우면 항상 캐시도 삭제
    public void clearFinanceCache(String companyName) {
        this.redisCacheManager.getCache(CacheKey.KEY_FINANCE).evict(companyName);
    }
}
