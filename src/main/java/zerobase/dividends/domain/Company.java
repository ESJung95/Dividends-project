package zerobase.dividends.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import zerobase.dividends.dto.CompanyDto;

@Entity(name = "COMPANY")
@Getter
@ToString
@NoArgsConstructor
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String ticker;

    private String name;

    public Company(CompanyDto companyDto) {
        this.ticker = companyDto.getTicker();
        this.name = companyDto.getName();
    }
}
