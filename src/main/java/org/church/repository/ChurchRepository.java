package org.church.repository;
import org.church.domain.Church;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Church entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChurchRepository extends JpaRepository<Church, Long>, JpaSpecificationExecutor<Church> {
  // Church findFirst();
}
