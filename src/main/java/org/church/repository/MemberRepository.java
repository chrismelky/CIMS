package org.church.repository;
import org.church.domain.Church;
import org.church.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Member entity.
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, JpaSpecificationExecutor<Member> {

    @Query(value = "select distinct mb from Member mb left join fetch mb.churchCommunities",
        countQuery = "select count(distinct mb) from Member mb")
    Page<Member> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct mb from Member mb left join fetch mb.churchCommunities")
    List<Member> findAllWithEagerRelationships();

    @Query("select mb from Member mb left join fetch mb.churchCommunities where mb.id =:id")
    Optional<Member> findOneWithEagerRelationships(@Param("id") Long id);

    Long countAllByChurch(@NotNull Church church);

    Optional<Member> findByMemberRn(String memberRn);

}
