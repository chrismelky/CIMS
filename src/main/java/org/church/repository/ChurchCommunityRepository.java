package org.church.repository;
import org.church.domain.ChurchCommunity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ChurchCommunity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChurchCommunityRepository extends JpaRepository<ChurchCommunity, Long>, JpaSpecificationExecutor<ChurchCommunity> {
    ChurchCommunity findFirstByNameIgnoreCaseAndChurch_Id(String name, Long churchId);
}
