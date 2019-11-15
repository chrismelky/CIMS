package org.church.repository;
import org.church.domain.MemberRite;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the MemberRite entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MemberRiteRepository extends JpaRepository<MemberRite, Long>, JpaSpecificationExecutor<MemberRite> {

}
