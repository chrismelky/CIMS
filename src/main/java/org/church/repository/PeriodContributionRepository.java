package org.church.repository;

import org.church.domain.PeriodContribution;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the PeriodContribution entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PeriodContributionRepository extends JpaRepository<PeriodContribution, Long>, JpaSpecificationExecutor<PeriodContribution> {
    List<PeriodContribution> findByMemberPromise_Id(Long memberPromiseId);
}
