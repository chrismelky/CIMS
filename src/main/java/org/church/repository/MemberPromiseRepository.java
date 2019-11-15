package org.church.repository;
import org.church.domain.MemberPromise;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the MemberPromise entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MemberPromiseRepository extends JpaRepository<MemberPromise, Long>, JpaSpecificationExecutor<MemberPromise> {

}
