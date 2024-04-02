package zerobase.dividends.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zerobase.dividends.domain.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    boolean existsByTicker(String ticker);
}
