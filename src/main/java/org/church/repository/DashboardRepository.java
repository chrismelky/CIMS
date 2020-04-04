package org.church.repository;

import org.church.domain.ContributionDashboard;
import org.church.domain.MemberContributionDashboard;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@SuppressWarnings("unchecked")
@Repository
public class DashboardRepository {

    @PersistenceContext
    public EntityManager em;


    public List<ContributionDashboard> getContribution(
        Long churchId,
        Long financialYearId) {

       return em.createNativeQuery("select " +
           "  pct.name, " +
           "  sum(pc.amount_promised) as promise, " +
           "  sum(pc.amount_contributed) as collection " +
           " from period_contribution_type pct " +
           " left join period_contribution pc on pc.period_contribution_type_id = pct.id " +
           " join period as p on p.id = pc.period_id and p.financial_year_id=:financialYearId" +
           " where pct.church_id =:churchId " +
           "group by pct.id order by pct.name", ContributionDashboard.class)
           .setParameter("churchId", churchId)
           .setParameter("financialYearId", financialYearId)
           .getResultList();
    }


    public List<MemberContributionDashboard> getMemberContr(
        Long churchId,
        Long periodId,
        Long typeId,
        int pageNumber,
        int perPage,
        boolean overDue) {

        String q = "select " +
            "       m.first_name || ' ' || coalesce(m.middle_name, '') || ' ' || m.last_name as name, " +
            "       m.phone_number, " +
            "       coalesce(pc.amount_promised,0) as promise, " +
            "       coalesce(pc.amount_contributed,0) as contribution, " +
            "       pc.due_date, " +
            "       case when pc.due_date <= now() then true else false end as over_due " +
            "from member m " +
            "left join period_contribution pc on m.id = pc.member_id " +
            "and pc.period_contribution_type_id =:typeId and pc.church_id=:churchId and " +
            " pc.period_id=:periodId " +
            "where m.church_id =:churchId ";
            if (overDue) {
                q = q + " and pc.due_date <= now()";
            }
            q =q + "group by m.id, pc.id " +
            "order by m.first_name, m.last_name ";

        return em.createNativeQuery(q, MemberContributionDashboard.class)
            .setParameter("churchId", churchId)
            .setParameter("periodId", periodId)
            .setParameter("typeId", typeId)
            .setMaxResults(perPage)
            .setFirstResult((pageNumber-1)*perPage)
            .getResultList();

    }


}
