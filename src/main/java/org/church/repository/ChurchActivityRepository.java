package org.church.repository;
import org.church.domain.ChurchActivity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ChurchActivity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChurchActivityRepository extends JpaRepository<ChurchActivity, Long>, JpaSpecificationExecutor<ChurchActivity> {

}
