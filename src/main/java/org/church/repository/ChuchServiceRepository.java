package org.church.repository;
import org.church.domain.ChuchService;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ChuchService entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChuchServiceRepository extends JpaRepository<ChuchService, Long>, JpaSpecificationExecutor<ChuchService> {

}
