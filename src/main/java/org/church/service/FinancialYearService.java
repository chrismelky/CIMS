package org.church.service;

import org.church.domain.FinancialYear;
import org.church.repository.FinancialYearRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link FinancialYear}.
 */
@Service
@Transactional
public class FinancialYearService {

    private final Logger log = LoggerFactory.getLogger(FinancialYearService.class);

    private final FinancialYearRepository financialYearRepository;

    public FinancialYearService(FinancialYearRepository financialYearRepository) {
        this.financialYearRepository = financialYearRepository;
    }

    /**
     * Save a financialYear.
     *
     * @param financialYear the entity to save.
     * @return the persisted entity.
     */
    public FinancialYear save(FinancialYear financialYear) {
        log.debug("Request to save FinancialYear : {}", financialYear);
        return financialYearRepository.save(financialYear);
    }

    /**
     * Get all the financialYears.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<FinancialYear> findAll(Pageable pageable) {
        log.debug("Request to get all FinancialYears");
        return financialYearRepository.findAll(pageable);
    }

    /**
     * Get one financialYear by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FinancialYear> findOne(Long id) {
        log.debug("Request to get FinancialYear : {}", id);
        return financialYearRepository.findById(id);
    }

    /**
     * Delete the financialYear by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete FinancialYear : {}", id);
        financialYearRepository.deleteById(id);
    }
}
