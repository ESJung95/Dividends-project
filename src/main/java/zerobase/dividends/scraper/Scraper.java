package zerobase.dividends.scraper;

import zerobase.dividends.dto.CompanyDto;
import zerobase.dividends.dto.ScrapedResultDto;

public interface Scraper {
    CompanyDto scrapCompanyByTicker(String ticker);
    ScrapedResultDto scrap(CompanyDto companyDto);


}
