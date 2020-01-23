package org.church.service;

import org.church.domain.Period;
import org.church.repository.PeriodRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Period}.
 */
@Service
@Transactional
public class PeriodService {

    private final Logger log = LoggerFactory.getLogger(PeriodService.class);

    private final PeriodRepository periodRepository;

    public PeriodService(PeriodRepository periodRepository) {
        this.periodRepository = periodRepository;
    }

    /**
     * Save a period.
     *
     * @param period the entity to save.
     * @return the persisted entity.
     */
    public Period save(Period period) {
        log.debug("Request to save Period : {}", period);
        return periodRepository.save(period);
    }

    /**
     * Get all the periods.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Period> findAll(Pageable pageable) {
        log.debug("Request to get all Periods");
        return periodRepository.findAll(pageable);
    }


    /**
     * Get one period by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Period> findOne(Long id) {
        log.debug("Request to get Period : {}", id);
        return periodRepository.findById(id);
    }

    /**
     * Delete the period by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Period : {}", id);
        periodRepository.deleteById(id);
    }
}
