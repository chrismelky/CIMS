package org.church.repository;
import org.church.domain.ChurchType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ChurchType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChurchTypeRepository extends JpaRepository<ChurchType, Long>, JpaSpecificationExecutor<ChurchType> {

}
