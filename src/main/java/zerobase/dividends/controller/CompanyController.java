package zerobase.dividends.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import zerobase.dividends.dto.CompanyDto;
import zerobase.dividends.service.CompanyService;

@RestController
@RequestMapping("/company")
@AllArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    // 검색 시 자동완성
    @GetMapping("/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam String keyword) {
        return null;
    }

    // 회사 리스트 조회
    @GetMapping
    public ResponseEntity<?> searchCompany() {
        return null;
    }

    // 각 회사 배당금 데이터 저장
    @PostMapping
    public ResponseEntity<?> addCompany(@RequestBody CompanyDto request) {
        String ticker = request.getTicker().trim();
        if (ObjectUtils.isEmpty(ticker)) {
            throw new RuntimeException("ticker is empty");
        }

        CompanyDto companyDto = this.companyService.save(ticker);

        return ResponseEntity.ok(companyDto);
    }

    // 각 회사 배당금 데이터 삭제
    @DeleteMapping
    public ResponseEntity<?> deleteCompany() {
        return null;
    }
}
