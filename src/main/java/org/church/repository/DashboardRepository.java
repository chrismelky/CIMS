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
           "  sum(mp.amount) as promise, " +
           "  sum(mp.total_contribution) as collection " +
           " from period_contribution_type pct " +
           " left join member_promise mp on mp.period_contribution_type_id = pct.id  and mp.financial_year_id=:financialYearId" +
           " where pct.church_id =:churchId " +
           "group by pct.id order by pct.name", ContributionDashboard.class)
           .setParameter("churchId", churchId)
           .setParameter("financialYearId", financialYearId)
           .getResultList();
    }


    public List<MemberContributionDashboard> getMemberContr(
        Long churchId,
        Long financialYearId,
        Long typeId,
        int pageNumber,
        int perPage,
        boolean overDue) {

        String q = "select " +
            "       m.first_name || ' ' || coalesce(m.middle_name, '') || ' ' || m.last_name as name, " +
            "       m.phone_number, " +
            "       coalesce(mp.amount,0) as promise, " +
            "       coalesce(mp.total_contribution,0) as contribution, " +
            "       mp.fulfillment_date as due_date, " +
            "       case when mp.fulfillment_date <= now() then true else false end as over_due " +
            " from member m " +
            " left join member_promise mp on m.id = mp.member_id " +
            " and mp.period_contribution_type_id =:typeId and mp.church_id=:churchId and " +
            " mp.financial_year_id=:financialYearId " +
            " where m.church_id =:churchId ";
            if (overDue) {
                q = q + " and mp.fulfillment_date <= now()";
            }
            q =q + "group by m.id, mp.id " +
            "order by m.first_name, m.last_name ";

        return em.createNativeQuery(q, MemberContributionDashboard.class)
            .setParameter("churchId", churchId)
            .setParameter("financialYearId", financialYearId)
            .setParameter("typeId", typeId)
            .setMaxResults(perPage)
            .setFirstResult((pageNumber-1)*perPage)
            .getResultList();

    }


}
