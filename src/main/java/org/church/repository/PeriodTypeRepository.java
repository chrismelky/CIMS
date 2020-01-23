package org.church.repository;
import org.church.domain.PeriodType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the PeriodType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PeriodTypeRepository extends JpaRepository<PeriodType, Long>, JpaSpecificationExecutor<PeriodType> {

}
