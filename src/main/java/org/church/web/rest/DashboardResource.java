package org.church.web.rest;

import org.church.domain.ContributionDashboard;
import org.church.domain.MemberContributionDashboard;
import org.church.repository.DashboardRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardResource {

    private final
    DashboardRepository dashboardRepository;

    public DashboardResource(DashboardRepository dashboardRepository) {
        this.dashboardRepository = dashboardRepository;
    }

    @GetMapping("/contribution/{churchId}/{periodId}")
    public List<ContributionDashboard> getContr(@PathVariable Long churchId,
                                                                @PathVariable Long periodId) {
        return dashboardRepository.getContribution(churchId, periodId);
    }

    @GetMapping("/member-contribution/{churchId}/{periodId}/{typeId}")
    public List<MemberContributionDashboard> getMemberContr(@PathVariable Long churchId,
                                                            @PathVariable Long periodId,
                                                            @PathVariable Long typeId,
                                                            Pageable pageable) {
        System.out.println(pageable.getPageNumber());
        int pageNumber = pageable.getPageNumber() + 1;
        return dashboardRepository.getMemberContr(churchId,
            periodId,typeId, pageNumber, pageable.getPageSize());
    }
}
