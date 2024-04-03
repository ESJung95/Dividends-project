package zerobase.dividends.scraper;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import zerobase.dividends.dto.CompanyDto;
import zerobase.dividends.dto.DividendDto;
import zerobase.dividends.dto.ScrapedResultDto;
import zerobase.dividends.type.Month;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class YahooFinanceScraper implements Scraper {

    private static final String URL = "https://finance.yahoo.com/quote/%s/history?period1=%d&period2=%d&interval=1mo";
    private static final String SUMMARY_URL = "https://finance.yahoo.com/quote/%s?p=%s";
    private  static final long START_TIME = 86400;  // 60초 * 60분 * 24시간

    @Override
    public ScrapedResultDto scrap(CompanyDto companyDto) {
        var scrapResultDto = new ScrapedResultDto();
        scrapResultDto.setCompanyDto(companyDto);

        try {
            long now = System.currentTimeMillis() / 1000;

            String url = String.format(URL, companyDto.getTicker(), START_TIME, now);
            Connection connection = Jsoup.connect(url);
            Document document = connection.get();

            Elements parsingDivs = document.getElementsByAttributeValue("data-test", "historical-prices");
            Element tableElement = parsingDivs.get(0); // table 전체

            Element tbody = tableElement.children().get(1);

            List<DividendDto> dividendDtos = new ArrayList<>();
            for (Element e : tbody.children()) {
                String txt = e.text();
                if (!txt.endsWith("Dividend")) {
                    continue;
                }

                String[] splits = txt.split(" ");
                int month = Month.strToNumber(splits[0]);
                int day = Integer.valueOf(splits[1].replace(",", ""));
                int year = Integer.valueOf(splits[2]);
                String dividend = splits[3];

                if (month < 0) {
                    throw new RuntimeException("Unexpected Month enum value ->" + splits[0]);
                }

                dividendDtos.add(new DividendDto(LocalDateTime.of(year, month, day, 0, 0), dividend));

            }
            scrapResultDto.setDividends(dividendDtos);
        } catch (IOException e) {
            // TODO
            e.printStackTrace();
        }
        return scrapResultDto;
    }

    @Override
    public CompanyDto scrapCompanyByTicker(String ticker) {
        String url = String.format(SUMMARY_URL, ticker, ticker);

        try {
            Document document = Jsoup.connect(url).get();
            Element titleElement = document.getElementsByTag("h1").get(0);
            String title = titleElement.text().split("\\(")[0].trim();

            return new CompanyDto(ticker, title);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
