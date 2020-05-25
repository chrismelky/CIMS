package org.church.service;

import org.church.domain.PeriodContribution;
import org.church.repository.PeriodContributionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link PeriodContribution}.
 */
@Service
@Transactional
public class PeriodContributionService {

    private final Logger log = LoggerFactory.getLogger(PeriodContributionService.class);

    private final PeriodContributionRepository periodContributionRepository;

    public PeriodContributionService(PeriodContributionRepository periodContributionRepository) {
        this.periodContributionRepository = periodContributionRepository;
    }

    /**
     * Save a periodContribution.
     *
     * @param periodContribution the entity to save.
     * @return the persisted entity.
     */
    public PeriodContribution save(PeriodContribution periodContribution) {
        log.debug("Request to save PeriodContribution : {}", periodContribution);
        return periodContributionRepository.save(periodContribution);
    }

    /**
     * Get all the periodContributions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PeriodContribution> findAll(Pageable pageable) {
        log.debug("Request to get all PeriodContributions");
        return periodContributionRepository.findAll(pageable);
    }

    /**
     * Get one periodContribution by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PeriodContribution> findOne(Long id) {
        log.debug("Request to get PeriodContribution : {}", id);
        return periodContributionRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<PeriodContribution> findByPromise(Long memberPromiseId) {
        log.debug("Request to get PeriodContribution : {}", memberPromiseId);
        return periodContributionRepository.findByMemberPromise_Id(memberPromiseId);
    }

    /**
     * Delete the periodContribution by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PeriodContribution : {}", id);
        periodContributionRepository.deleteById(id);
    }
}
