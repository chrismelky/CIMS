package org.church.repository;
import org.church.domain.PeriodContributionType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the PeriodContributionType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PeriodContributionTypeRepository extends JpaRepository<PeriodContributionType, Long>, JpaSpecificationExecutor<PeriodContributionType> {

}
