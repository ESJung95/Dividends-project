package zerobase.dividends.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zerobase.dividends.domain.Dividend;

@Repository
public interface DividendRepository extends JpaRepository<Dividend, Long> {

}
