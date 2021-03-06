package org.church.web.rest;

import org.church.domain.ContributionDashboard;
import org.church.domain.MemberContributionDashboard;
import org.church.repository.DashboardRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardResource {

    private final
    DashboardRepository dashboardRepository;

    public DashboardResource(DashboardRepository dashboardRepository) {
        this.dashboardRepository = dashboardRepository;
    }

    @GetMapping("/contribution/{churchId}/{financialYearId}")
    public List<ContributionDashboard> getContr(@PathVariable Long churchId,
                                                @PathVariable Long financialYearId) {
        return dashboardRepository.getContribution(churchId, financialYearId);
    }

    @GetMapping("/member-contribution/{churchId}/{financialYearId}/{typeId}")
    public List<MemberContributionDashboard> getMemberContr(@PathVariable Long churchId,
                                                            @PathVariable Long financialYearId,
                                                            @PathVariable Long typeId,
                                                            @RequestParam(defaultValue = "false") Boolean overDue,
                                                            Pageable pageable) {
        int pageNumber = pageable.getPageNumber() + 1;
        return dashboardRepository.getMemberContr(churchId,
            financialYearId,typeId, pageNumber, pageable.getPageSize(), overDue);
    }
}
