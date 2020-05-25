package org.church.repository;
import org.church.domain.MemberContribution;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;


/**
 * Spring Data  repository for the MemberContribution entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MemberContributionRepository extends JpaRepository<MemberContribution, Long>, JpaSpecificationExecutor<MemberContribution> {

    @Query("SELECT  SUM(amount) " +
        "FROM MemberContribution c " +
        "where c.memberPromise.id =:memberPromiseId")
    BigDecimal getSumByPeriodContribution_Id(@Param("memberPromiseId") Long memberPromiseId);

    Page<MemberContribution> findByMemberPromise_Id(Long promiseId, Pageable page);
    Page<MemberContribution> findByMemberPromise_IdAndPaymentDate(Long promiseId, LocalDate date, Pageable page);
}
