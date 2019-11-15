package org.church.repository;
import org.church.domain.MemberRelative;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the MemberRelative entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MemberRelativeRepository extends JpaRepository<MemberRelative, Long>, JpaSpecificationExecutor<MemberRelative> {

}
