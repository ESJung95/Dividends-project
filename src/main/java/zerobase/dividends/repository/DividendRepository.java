package zerobase.dividends.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import zerobase.dividends.domain.Dividend;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DividendRepository extends JpaRepository<Dividend, Long> {
    List<Dividend> findAllByCompanyId(Long companyId);

    @Transactional
    void deleteAllByCompanyId(Long Id);

    boolean existsByCompanyIdAndDate(Long companyId, LocalDateTime date);
}
