package org.church.service;

import org.church.domain.MemberPromise;
import org.church.domain.Period;
import org.church.domain.PeriodContribution;
import org.church.domain.PeriodContributionType;
import org.church.repository.MemberPromiseRepository;
import org.church.repository.PeriodContributionRepository;
import org.church.repository.PeriodContributionTypeRepository;
import org.church.repository.PeriodRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link MemberPromise}.
 */
@Service
@Transactional
public class MemberPromiseService {

    private final Logger log = LoggerFactory.getLogger(MemberPromiseService.class);

    private final MemberPromiseRepository memberPromiseRepository;

    private final PeriodRepository periodRepository;

    private final PeriodContributionTypeRepository contributionTypeRepository;

    private final PeriodContributionRepository contributionRepository;

    public MemberPromiseService(MemberPromiseRepository memberPromiseRepository, PeriodRepository periodRepository, PeriodContributionTypeRepository contributionTypeRepository, PeriodContributionRepository contributionRepository) {
        this.memberPromiseRepository = memberPromiseRepository;
        this.periodRepository = periodRepository;
        this.contributionTypeRepository = contributionTypeRepository;
        this.contributionRepository = contributionRepository;
    }

    /**
     * Save a memberPromise.
     *
     * @param memberPromise the entity to save.
     * @return the persisted entity.
     */
    public MemberPromise save(MemberPromise memberPromise) {
        log.debug("Request to save MemberPromise : {}", memberPromise);
        return memberPromiseRepository.save(memberPromise);
    }

    public MemberPromise savePeriodPromises(MemberPromise promise) {
        PeriodContributionType contributionType = contributionTypeRepository.getOne(promise.getPeriodContributionType().getId());
        List<Period> periods = periodRepository
            .findByFinancialYear_IdAndType_Id(promise.getFinancialYear().getId(), contributionType.getPeriodType().getId());

        BigDecimal amount = BigDecimal.valueOf(0);
        if (periods.size() > 0) {
            amount = promise.getAmount().divide(BigDecimal.valueOf(periods.size()), 2);
        }
        final BigDecimal  finalAmount = amount;
        periods.forEach(p -> {
            PeriodContribution periodContribution = new PeriodContribution(finalAmount, p.getEndDate(), p, promise);
            contributionRepository.save(periodContribution);
            promise.getPeriodContributions().add(periodContribution);
        });

        return promise;
    }

    /**
     * Get all the memberPromises.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MemberPromise> findAll(Pageable pageable) {
        log.debug("Request to get all MemberPromises");
        return memberPromiseRepository.findAll(pageable);
    }

    /**
     * Get one memberPromise by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MemberPromise> findOne(Long id) {
        log.debug("Request to get MemberPromise : {}", id);
        return memberPromiseRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<MemberPromise> findOne(Long churchId, Long memberId, Long typeId, Long fyId) {
        log.debug("Request to get MemberPromise : {}", fyId);
        return memberPromiseRepository.findFirstByChurch_IdAndMember_IdAndPeriodContributionType_IdAndFinancialYear_Id(churchId, memberId, typeId, fyId);
    }

    /**
     * Delete the memberPromise by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete MemberPromise : {}", id);
        memberPromiseRepository.deleteById(id);
    }
}
