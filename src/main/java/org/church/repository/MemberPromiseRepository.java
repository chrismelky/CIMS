package org.church.repository;

import org.church.domain.MemberPromise;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data  repository for the MemberPromise entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MemberPromiseRepository extends JpaRepository<MemberPromise, Long>, JpaSpecificationExecutor<MemberPromise> {

    Optional<MemberPromise> findFirstByChurch_IdAndMember_IdAndPeriodContributionType_IdAndFinancialYear_Id(Long churchId, Long memberId, Long typeId, Long fyId);
}
