package org.church.service;

import org.church.domain.ChuchService;
import org.church.repository.ChuchServiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ChuchService}.
 */
@Service
@Transactional
public class ChuchServiceService {

    private final Logger log = LoggerFactory.getLogger(ChuchServiceService.class);

    private final ChuchServiceRepository chuchServiceRepository;

    public ChuchServiceService(ChuchServiceRepository chuchServiceRepository) {
        this.chuchServiceRepository = chuchServiceRepository;
    }

    /**
     * Save a chuchService.
     *
     * @param chuchService the entity to save.
     * @return the persisted entity.
     */
    public ChuchService save(ChuchService chuchService) {
        log.debug("Request to save ChuchService : {}", chuchService);
        return chuchServiceRepository.save(chuchService);
    }

    /**
     * Get all the chuchServices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ChuchService> findAll(Pageable pageable) {
        log.debug("Request to get all ChuchServices");
        return chuchServiceRepository.findAll(pageable);
    }


    /**
     * Get one chuchService by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ChuchService> findOne(Long id) {
        log.debug("Request to get ChuchService : {}", id);
        return chuchServiceRepository.findById(id);
    }

    /**
     * Delete the chuchService by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ChuchService : {}", id);
        chuchServiceRepository.deleteById(id);
    }
}
