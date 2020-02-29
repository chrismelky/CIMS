package org.church.repository;

import org.church.dto.ContributionDTO;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@SuppressWarnings("unchecked")
public class DashboardRepository {

    @PersistenceContext
    public EntityManager entityManager;


    public List<ContributionDTO> getContribution(
        @Param("periodId") Long periodId,
        @Param("churchId") Long churchId
    ) {

        return entityManager.createNativeQuery("select " +
            "  pct.name," +
            "  sum(pc.amount_promised) as promises, " +
            "  sum(pc.amount_contributed) as collection " +
            " from period_contribution pc " +
            " join period_contribution_type pct on pc.period_contribution_type_id = pct.id " +
            " join church c on pc.church_id = c.id " +
            " join period p on pc.period_id = p.id " +
            " where c.id =:churchId and p.id =:periodId " +
            " group by pct.id ", ContributionDTO.class)
            .setParameter("periodId", periodId)
            .setParameter("churchId", churchId)
            .getResultList();

    }

}
