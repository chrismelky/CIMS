package org.church.repository;
import org.church.domain.HomeChurchCommunity;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the HomeChurchCommunity entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HomeChurchCommunityRepository
    extends JpaRepository<HomeChurchCommunity, Long>,
    JpaSpecificationExecutor<HomeChurchCommunity> {

    HomeChurchCommunity findFirstByNameIgnoreCaseAndChurch_Id(String name, Long churchId);
}
