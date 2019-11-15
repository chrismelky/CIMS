package org.church.repository;
import org.church.domain.Rite;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Rite entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RiteRepository extends JpaRepository<Rite, Long>, JpaSpecificationExecutor<Rite> {

}
