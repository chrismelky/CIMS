package org.church.service;

import org.church.domain.PeriodContributionType;
import org.church.repository.PeriodContributionTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link PeriodContributionType}.
 */
@Service
@Transactional
public class PeriodContributionTypeService {

    private final Logger log = LoggerFactory.getLogger(PeriodContributionTypeService.class);

    private final PeriodContributionTypeRepository periodContributionTypeRepository;

    public PeriodContributionTypeService(PeriodContributionTypeRepository periodContributionTypeRepository) {
        this.periodContributionTypeRepository = periodContributionTypeRepository;
    }

    /**
     * Save a periodContributionType.
     *
     * @param periodContributionType the entity to save.
     * @return the persisted entity.
     */
    public PeriodContributionType save(PeriodContributionType periodContributionType) {
        log.debug("Request to save PeriodContributionType : {}", periodContributionType);
        return periodContributionTypeRepository.save(periodContributionType);
    }

    /**
     * Get all the periodContributionTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PeriodContributionType> findAll(Pageable pageable) {
        log.debug("Request to get all PeriodContributionTypes");
        return periodContributionTypeRepository.findAll(pageable);
    }


    /**
     * Get one periodContributionType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PeriodContributionType> findOne(Long id) {
        log.debug("Request to get PeriodContributionType : {}", id);
        return periodContributionTypeRepository.findById(id);
    }

    /**
     * Delete the periodContributionType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PeriodContributionType : {}", id);
        periodContributionTypeRepository.deleteById(id);
    }
}
