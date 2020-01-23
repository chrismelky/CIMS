package org.church.service;

import org.church.domain.PeriodType;
import org.church.repository.PeriodTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link PeriodType}.
 */
@Service
@Transactional
public class PeriodTypeService {

    private final Logger log = LoggerFactory.getLogger(PeriodTypeService.class);

    private final PeriodTypeRepository periodTypeRepository;

    public PeriodTypeService(PeriodTypeRepository periodTypeRepository) {
        this.periodTypeRepository = periodTypeRepository;
    }

    /**
     * Save a periodType.
     *
     * @param periodType the entity to save.
     * @return the persisted entity.
     */
    public PeriodType save(PeriodType periodType) {
        log.debug("Request to save PeriodType : {}", periodType);
        return periodTypeRepository.save(periodType);
    }

    /**
     * Get all the periodTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PeriodType> findAll(Pageable pageable) {
        log.debug("Request to get all PeriodTypes");
        return periodTypeRepository.findAll(pageable);
    }


    /**
     * Get one periodType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PeriodType> findOne(Long id) {
        log.debug("Request to get PeriodType : {}", id);
        return periodTypeRepository.findById(id);
    }

    /**
     * Delete the periodType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PeriodType : {}", id);
        periodTypeRepository.deleteById(id);
    }
}
