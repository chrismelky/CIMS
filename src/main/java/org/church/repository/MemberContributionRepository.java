package org.church.repository;
import org.church.domain.MemberContribution;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the MemberContribution entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MemberContributionRepository extends JpaRepository<MemberContribution, Long>, JpaSpecificationExecutor<MemberContribution> {

}
