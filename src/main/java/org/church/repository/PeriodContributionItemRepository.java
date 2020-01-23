package org.church.repository;
import org.church.domain.PeriodContributionItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;


/**
 * Spring Data  repository for the PeriodContributionItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PeriodContributionItemRepository extends
    JpaRepository<PeriodContributionItem, Long>,
    JpaSpecificationExecutor<PeriodContributionItem> {

    @Query("SELECT  SUM(amount) " +
        "FROM PeriodContributionItem i " +
        "where i.periodContribution.id =:periodContributionId")
    BigDecimal getSumByPeriodContribution_Id(@Param("periodContributionId") Long periodContributionId);
}
