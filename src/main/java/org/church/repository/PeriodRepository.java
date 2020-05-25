package org.church.repository;

import org.church.domain.Period;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Period entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PeriodRepository extends JpaRepository<Period, Long>, JpaSpecificationExecutor<Period> {
    List<Period> findByFinancialYear_IdAndType_Id(Long fyId, Long typeId);
}
