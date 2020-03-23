package org.church.repository;

import org.church.domain.FinancialYear;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the FinancialYear entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FinancialYearRepository extends JpaRepository<FinancialYear, Long>, JpaSpecificationExecutor<FinancialYear> {
}
