package zerobase.dividends.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import zerobase.dividends.dto.DividendDto;

import java.time.LocalDateTime;

@Entity(name = "DIVIDEND")
@Getter
@ToString
@NoArgsConstructor
public class Dividend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long companyId;

    private LocalDateTime date;

    private String dividend;

    public Dividend(Long companyId, DividendDto dividendDto) {
        this.companyId = companyId;
        this.date = dividendDto.getDate();
        this.dividend = dividendDto.getDividend();
    }
}
